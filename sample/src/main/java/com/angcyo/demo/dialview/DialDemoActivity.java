package com.angcyo.demo.dialview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.angcyo.sample.R;
import com.rsen.view.DialView;

/**
 * Created by angcyo on 15-12-18-018.
 */
public class DialDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dial_demo_layout);

    }

    public void onClick(View view) {
        DialView dialView = (DialView) findViewById(R.id.dial_view);
//        dialView.startDial(2);

//        dialView.rotateNumber(1, 200, new Runnable() {
//            @Override
//            public void run() {
//                Log.e("tag", "end............");
//            }
//        });

        dialView.rotateNumber(0, 2000);
    }
}
