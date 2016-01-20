package com.angcyo.sample.ShapeDemo;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.angcyo.sample.R;

public class ShapeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionDrawable transitionDrawable = (TransitionDrawable) findViewById(R.id.edit_text).getBackground();
                transitionDrawable.startTransition(1000);
            }
        });
    }

    public void button(View view) {
        finish();
        overridePendingTransition(com.angcyo.rsen.R.anim.scale_0to1, com.angcyo.rsen.R.anim.scale_1to0);
    }

}
