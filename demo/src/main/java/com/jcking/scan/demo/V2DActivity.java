package com.jcking.scan.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.jcking.scan.CaptureFragment;
import com.jcking.scan.CaptureSwitcher;

public class V2DActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.jcking.scan.R.layout.activity_fragment);

        CaptureSwitcher.get().resetDefault();
        CaptureSwitcher.get().setDisableContinuousFocus(false);
        CaptureSwitcher.get().setDecode1dIndustrial(false);
        CaptureSwitcher.get().setDecode1dProduct(false);

        getSupportFragmentManager().beginTransaction()
                .replace(com.jcking.scan.R.id.layout_content, new CaptureFragment())
                .commitAllowingStateLoss();
    }
}
