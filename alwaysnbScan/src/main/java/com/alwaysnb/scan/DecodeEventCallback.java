package com.alwaysnb.scan;

import android.graphics.Bitmap;

import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;

public interface DecodeEventCallback {
    PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height);

    ViewfinderView getViewfinderView();

    void onDecodeSuccess(Result rawResult, Bitmap barcode, float scaleFactor);
}
