package com.angcyo.sample.ViewDemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.angcyo.sample.R;
import com.rsen.util.T;
import com.rsen.view.CirclePathButton;

public class PathButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_button);

        initView();

//        new Intent().putExtra("", new RemoteViews());
    }

    private void initView() {
        ((CirclePathButton) findViewById(R.id.circle_path_view)).setOnSelectChanged(new CirclePathButton.OnSelectChanged() {
            @Override
            public void onSelectChanged(View view, boolean isSelect) {
                Log.e("angcyo-->", "" + isSelect);
            }
        });

        findViewById(R.id.circle_path_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("angcyo-->", "onClick");
            }
        });
    }

    public void pathButton(View view) {
        T.show(this, ((Button) view).getText().toString());
    }

    public void button(View view) {
        T.show(this, ((Button) view).getText().toString());
    }
}
