package com.angcyo.demo.dialview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.angcyo.sample.R;
import com.cocosw.query.CocoQuery;
import com.rsen.util.ResUtil;
import com.rsen.view.DialSurfaceView;
import com.rsen.view.DialView;

/**
 * Created by angcyo on 15-12-18-018.
 */
public class DialDemoActivity extends AppCompatActivity {

    int index = 0;
    CocoQuery q;
    private DialView dialView;
    private DialSurfaceView dialSurfaceView;
    private View target;
    private TextView xV, yV, rotate;
    private float x, y, rotation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.dial_demo_layout);
        dialView = (DialView) findViewById(R.id.dial_view);
        dialSurfaceView = (DialSurfaceView) findViewById(R.id.dial_view2);
        dialView.setTexts(new String[]{"一等奖", "二等奖", "三等奖", "四等奖", "五等奖", "六等奖", "安慰奖"});
        dialView.setColors(new int[]{Color.RED, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GREEN, Color.LTGRAY, Color.MAGENTA});
//        dialView.setRatios(new float[]{1, 1f, 1f, 1f, 1f, 1f, 1f});
        dialView.setRatios(new float[]{1, 1.2f, 1.8f, 2f, 3f, 4f, 7f});
        dialView.setStartOffsetAngle(0f);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        dialView.setIcons(new Bitmap[]{bitmap, bitmap, bitmap, bitmap, bitmap, bitmap, bitmap});

        dialSurfaceView.setIcons(new Bitmap[]{bitmap, bitmap, bitmap, bitmap, bitmap, bitmap, bitmap});
        dialSurfaceView.setMean(true);
        dialSurfaceView.setTexts(new String[]{"一等奖", "二等奖", "三等奖", "四等奖", "五等奖", "六等奖", "安慰奖"});
        dialSurfaceView.setColors(new int[]{Color.RED, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GREEN, Color.LTGRAY, Color.MAGENTA});
    }

    public void onClick(View view) {
        setContentView(R.layout.activity_view_rotation);
        initCreate();


//        dialView.startDial(2);

//        dialView.rotateNumber(1, 200, new Runnable() {
//            @Override
//            public void run() {
//                Log.e("tag", "end............");
//            }
//        });

//        dialView.rotateNumber(0, 700);

//        dialView.start(1);
//        int n = index % dialView.getDialNum();
//        ((Button) view).setText("目标--> " + dialSurfaceView.getText(n));
//        ((Button) view).setText("目标--> " + dialView.getText(n));
//        dialSurfaceView.start(n, null);
////        dialView.start(n, null);
//        index++;

//        startActivity(new Intent(this, OnePxActivity.class));
//        showOnePxWindow();
    }

    public void showOnePxWindow() {
        TextView textView = new TextView(this);
        textView.setText("one px");
        textView.setBackgroundColor(Color.BLUE);

        WindowManager wm = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.format = PixelFormat.RGBA_8888;
        params.width = 1;
        params.height = 1;

        params.x = 1;
        params.y = 1;

        wm.addView(textView, params);
    }

    protected void initCreate() {
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
        e("x:" + x + " y:" + y + " r:" + rotation);
    }

    private float dp(float v) {
        return ResUtil.pxToDp(getResources(), v);
    }

    private void e(String msg) {
        Log.e("angcyo", msg + "");
    }
}
