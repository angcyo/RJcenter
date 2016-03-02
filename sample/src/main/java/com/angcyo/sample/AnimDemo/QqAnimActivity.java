package com.angcyo.sample.AnimDemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.TextView;

import com.angcyo.sample.R;

public class QqAnimActivity extends AppCompatActivity {

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq_anim);

        text = (TextView) findViewById(R.id.text);
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim();
            }
        });
    }

    private void startAnim() {
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                text.setY(interpolatedTime * 500);
            }
        };
        animation.setDuration(500);
        animation.setInterpolator(new LinearInterpolator());
        text.startAnimation(animation);
    }
}
