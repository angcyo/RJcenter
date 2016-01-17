package com.angcyo.sample.viewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.angcyo.sample.R;
import com.rsen.util.T;

public class PathButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_button);

        initView();
    }

    private void initView() {

    }

    public void pathButton(View view) {
        T.show(this, ((Button) view).getText().toString());
    }

    public void button(View view) {
        T.show(this, ((Button) view).getText().toString());
    }
}
