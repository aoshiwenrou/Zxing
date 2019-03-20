/*
 * Copyright (C) 2010 ZXing authors
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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

final class DecodeHandler extends Handler {

    private static final String TAG = DecodeHandler.class.getSimpleName();

    private final DecodeCallback callback;
    private final MultiFormatReader multiFormatReader;
    private boolean isNeedRotate;
    private boolean running = true;

    DecodeHandler(DecodeCallback callback, Map<DecodeHintType, Object> hints, boolean needRotate) {
        multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);
        isNeedRotate = needRotate;
        this.callback = callback;
    }

    @Override
    public void handleMessage(Message message) {
        if (message == null || !running) {
            return;
        }
        if (message.what == R.id.decode) {
            decode((byte[]) message.obj, message.arg1, message.arg2);
        } else if (message.what == R.id.quit) {
            running = false;
            Looper.myLooper().quit();
        }
    }

    /**
     * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency,
     * reuse the same reader objects from one decode to the next.
     *
     * @param data   The YUV preview frame.
     * @param width  The width of the preview frame.
     * @param height The height of the preview frame.
     */
    private void decode(byte[] data, int width, int height) {
        if (callback == null)
            return;

        // TODO 再只进行二维码的时候，不需要翻转，翻转增加了解析时间。根据CameraManager中的TODO进行完善判断
        if(isNeedRotate){
            byte[] rotatedData = new byte[data.length];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    rotatedData[x * height + height - y - 1] = data[x + y * width];
                }
            }
            int tmp = width; // Here we are swapping, that's the difference to #11
            width = height;
            height = tmp;
            data = rotatedData;
        }

        long start = System.nanoTime();
        Result rawResult = null;
        PlanarYUVLuminanceSource source = callback.buildLuminanceSource(data, width, height);
        if (source != null) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                rawResult = multiFormatReader.decodeWithState(bitmap);
            } catch (ReaderException re) {
                // continue
            } finally {
                multiFormatReader.reset();
            }
        }

        if (rawResult != null) {
            // Don't log the barcode contents for security.
            long end = System.nanoTime();
            Log.d(TAG, "Found barcode in " + TimeUnit.NANOSECONDS.toMillis(end - start) + " ms");
            Bundle bundle = new Bundle();
            bundleThumbnail(source, bundle);
            callback.onDecodeSuccess(rawResult, bundle);
        } else {
            callback.onDecodeFail();
        }
    }

    private static void bundleThumbnail(PlanarYUVLuminanceSource source, Bundle bundle) {
        int[] pixels = source.renderThumbnail();
        int width = source.getThumbnailWidth();
        int height = source.getThumbnailHeight();
        Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
        bundle.putFloat(DecodeThread.BARCODE_SCALED_FACTOR, (float) width / source.getWidth());
    }

    public interface DecodeCallback {
        PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height);

        void onDecodeSuccess(Result result, Bundle bundle);

        void onDecodeFail();
    }
}
