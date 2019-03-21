/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jcking.scan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jcking.scan.camera.CameraManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;
import java.util.Collection;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * This activity opens the camera and does the actual scanning on a background thread. It draws a
 * viewfinder to help the user place the barcode correctly, shows feedback as the image processing
 * is happening, and then overlays the results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public class CaptureActivity extends FragmentActivity implements SurfaceHolder.Callback, DecodeEventCallback {

    private static final String TAG = CaptureActivity.class.getSimpleName();

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Collection<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(getLayoutResource());

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
    }

    protected int getLayoutResource() {
        return R.layout.activity_capture;
    }

    protected void startCamera() {
        // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        viewfinderView = getViewfinderView();
        viewfinderView.setCameraManager(cameraManager);

        handler = null;

        resetStatusView();

        beepManager.updatePrefs();

        inactivityTimer.onResume();

        decodeFormats = null;
        characterSet = null;

        SurfaceView surfaceView = getSurfaceView();
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            surfaceHolder.addCallback(this);
        }
    }

    protected void stopCamera() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        //historyManager = null; // Keep for onActivityResult
        if (!hasSurface) {
            SurfaceView surfaceView = getSurfaceView();
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCamera();
    }

    @Override
    protected void onPause() {
        stopCamera();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // do nothing
    }

    /**
     * A valid barcode has been found, so give an indication of success and show the results.
     *
     * @param rawResult   The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode     A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();

        ScrollTextView stv = findViewById(R.id.stv);
        if (stv == null) {
            Toast.makeText(this, rawResult.getText(), Toast.LENGTH_SHORT).show();
        } else {
            stv.appendWithTime(rawResult.getText());
        }

        restartPreviewAfterDelay(100);
    }

    @SuppressLint("CheckResult")
    private void initCamera(final SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.CAMERA).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(@NonNull Permission permission) throws Exception {
                if (permission.granted) {
                    try {
                        cameraManager.openDriver(surfaceHolder);
                        // Creating the handler starts the preview, which can also throw a RuntimeException.
                        if (handler == null) {
                            handler = new CaptureActivityHandler(CaptureActivity.this, decodeFormats, null, characterSet, cameraManager);
                        }
                    } catch (IOException ioe) {
                        Log.w(TAG, ioe);
                        displayFrameworkBugMessageAndExit();
                    } catch (RuntimeException e) {
                        // Barcode Scanner has seen crashes in the wild of this variety:
                        // java.?lang.?RuntimeException: Fail to connect to camera service
                        Log.w(TAG, "Unexpected error initializing camera", e);
                        displayFrameworkBugMessageAndExit();
                    }
                } else if (permission.shouldShowRequestPermissionRationale) {
                    //拒绝权限请求
                    finish();
                } else {
                    // 拒绝权限请求,并不再询问
                    // 需要进入设置界面去设置权限
                    displayFrameworkBugMessageAndExit();
                }
            }
        });
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    private void resetStatusView() {
        viewfinderView.setVisibility(View.VISIBLE);
    }

    public SurfaceView getSurfaceView() {
        return (SurfaceView) findViewById(R.id.preview_view);
    }

    @Override
    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        return cameraManager.buildLuminanceSource(data, width, height);
    }

    @Override
    public ViewfinderView getViewfinderView() {
        return (ViewfinderView) findViewById(R.id.viewfinder_view);
    }

    @Override
    public void onDecodeSuccess(Result rawResult, Bitmap barcode, float scaleFactor) {
        handleDecode(rawResult, barcode, scaleFactor);
    }
}
