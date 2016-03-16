package com.angcyo.sample.SurfaceViewMathDemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.angcyo.sample.R;

public class SurfaceViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view);

        findViewById(R.id.rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ShowCamera) findViewById(R.id.surfaceView)).rotate();
            }
        });
    }
}
