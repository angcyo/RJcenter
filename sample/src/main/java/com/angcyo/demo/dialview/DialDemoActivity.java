package com.angcyo.demo.dialview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.angcyo.sample.R;
import com.rsen.view.DialView;

/**
 * Created by angcyo on 15-12-18-018.
 */
public class DialDemoActivity extends AppCompatActivity {

    int index = 0;
    private DialView dialView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dial_demo_layout);
        dialView = (DialView) findViewById(R.id.dial_view);
        dialView.setTexts(new String[]{"一等奖", "二等奖", "三等奖", "四等奖", "五等奖", "六等奖", "安慰奖"});
        dialView.setColors(new int[]{Color.RED, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GREEN, Color.LTGRAY, Color.MAGENTA});
//        dialView.setRatios(new float[]{1, 1f, 1f, 1f, 1f, 1f, 1f});
        dialView.setRatios(new float[]{1, 1.2f, 1.8f, 2f, 3f, 4f, 7f});
        dialView.setStartOffsetAngle(0f);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        dialView.setIcons(new Bitmap[]{bitmap, bitmap, bitmap, bitmap, bitmap, bitmap, bitmap});
    }

    public void onClick(View view) {
//        dialView.startDial(2);

//        dialView.rotateNumber(1, 200, new Runnable() {
//            @Override
//            public void run() {
//                Log.e("tag", "end............");
//            }
//        });

//        dialView.rotateNumber(0, 700);

//        dialView.start(1);
        int n = index % dialView.getDialNum();
        ((Button) view).setText("目标--> " + dialView.getText(n));
        dialView.start(n, null);
        index++;
    }
}
