package com.jcking.scan;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class CaptureFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content, new CaptureFragment())
                .commitAllowingStateLoss();
    }
}
