package com.angcyo.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cocosw.query.CocoQuery;

public class ViewRotationActivity extends AppCompatActivity {

    CocoQuery q;
    private View target;
    private TextView xV, yV;
    private float x, y, rotation = 20f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rotation);
        q = new CocoQuery(this);
        target = q.id(R.id.target).getView();
        xV = (TextView) q.id(R.id.x).getView();
        yV = (TextView) q.id(R.id.x).getView();

    }

    public void setRotation(View view) {
        init();
        target.setRotation(rotation);
    }

    public void setRotationX(View view) {
        init();
        target.setRotationX(rotation);
    }

    public void setRotationY(View view) {
        init();
        target.setRotationY(rotation);
    }

    private void init() {
        String xs = xV.getText().toString();
        String ys = yV.getText().toString();
        if (TextUtils.isEmpty(xs)) {
            x = target.getWidth() / 2f;
        } else {
            x = Float.valueOf(xs);
        }
        if (TextUtils.isEmpty(ys)) {
            y = target.getHeight() / 2f;
        } else {
            y = Float.valueOf(ys);
        }
    }
}
