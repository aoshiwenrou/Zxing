package com.jcking.scan.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jcking.scan.CaptureActivity;

/**
 * @author Jcking
 * @time 2019/3/20 22:06
 */
public class DemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button btn = new Button(this);
        btn.setText("扫描");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoActivity.this, CaptureActivity.class);
                startActivity(intent);
            }
        });

        setContentView(btn);
    }
}
