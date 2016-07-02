package com.angcyo.sample.intentfilter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.angcyo.sample.R;


public class IntentFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.base_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void button(View view) {
        Intent intent = new Intent("com.angcyo.action");
        intent.setAction("com.angcyo.action1");
        intent.addCategory("com.angcyo1");
        intent.addCategory("com.angcyo2");
        intent.setDataAndType(Uri.parse("angcyo"), "angcyo/*");
        startActivity(intent);
    }

}
