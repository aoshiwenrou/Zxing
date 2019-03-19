package com.alwaysnb.scan;

import android.app.Activity;
import android.os.Bundle;

public class CaptureFragmentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        getFragmentManager().beginTransaction()
                .replace(R.id.layout_content, new CaptureFragment())
                .commitAllowingStateLoss();
    }
}
