package com.angcyo.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cocosw.query.CocoQuery;
import com.rsen.util.ResUtil;

public class ViewRotationActivity extends AppCompatActivity {

    CocoQuery q;
    private View target;
    private TextView xV, yV, rotate;
    private float x, y, rotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rotation);
        q = new CocoQuery(this);
        target = q.id(R.id.target).getView();
        rotate = (TextView) q.id(R.id.rotate).getView();
        xV = (TextView) q.id(R.id.x).getView();
        yV = (TextView) q.id(R.id.y).getView();

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

    public void reset(View view) {
        target.setRotation(0);
        target.setRotationX(0);
        target.setRotationY(0);
    }

    private void init() {
        String xs = xV.getText().toString();
        String ys = yV.getText().toString();
        String rS = rotate.getText().toString();
        if (TextUtils.isEmpty(xs)) {
            x = target.getWidth() / 2f;
        } else {
            x = dp(Float.valueOf(xs));
        }
        if (TextUtils.isEmpty(ys)) {
            y = target.getHeight() / 2f;
        } else {
            y = dp(Float.valueOf(ys));
        }
        if (TextUtils.isEmpty(rS)) {
            rotation = 20f;
        } else {
            rotation = Float.valueOf(rS);
        }

        target.setPivotX(x);
        target.setPivotY(y);

        e("w:" + target.getWidth() + " h:" + target.getHeight());
        e("X:" + x + " y:" + y + " r:" + rotation);
    }

    private float dp(float v) {
        return ResUtil.dpToPx(getResources(), v);
    }

    private void e(String msg) {
        Log.e("angcyo", msg + "");
    }
}
