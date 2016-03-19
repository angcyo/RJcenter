package com.angcyo.sample.SpanDemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.MetricAffectingSpan;
import android.text.style.ReplacementSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.angcyo.sample.R;
import com.rsen.util.ResUtil;

public class SpanDemoActivity extends AppCompatActivity {

    TextView textView1, textView2, textView3, textView4, textView5, textView6,textView7;

    static void e(String log) {
        Log.e("angcyo", log);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_span_demo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initView();
    }

    private void initView() {
        textView1 = (TextView) findViewById(R.id.text_view1);
        textView2 = (TextView) findViewById(R.id.text_view2);
        textView3 = (TextView) findViewById(R.id.text_view3);
        textView4 = (TextView) findViewById(R.id.text_view4);
        textView5 = (TextView) findViewById(R.id.text_view5);
        textView6 = (TextView) findViewById(R.id.text_view6);
        textView7 = (TextView) findViewById(R.id.text_view7);
        ResUtil.setBgDrawable(textView1, ResUtil.generateRoundBorderDrawable(getResources(), 10, Color.BLUE, Color.CYAN));
        ResUtil.setBgDrawable(textView2, ResUtil.generateRoundBorderDrawable(getResources(), 10, Color.BLUE, Color.CYAN));
        ResUtil.setBgDrawable(textView3, ResUtil.generateRoundBorderDrawable(getResources(), 10, Color.BLUE, Color.CYAN));
        ResUtil.setBgDrawable(textView4, ResUtil.generateRoundBorderDrawable(getResources(), 10, Color.BLUE, Color.CYAN));
        ResUtil.setBgDrawable(textView5, ResUtil.generateRoundBorderDrawable(getResources(), 10, Color.BLUE, Color.CYAN));
        ResUtil.setBgDrawable(textView6, ResUtil.generateRoundBorderDrawable(getResources(), 10, Color.BLUE, Color.CYAN));
        ResUtil.setBgDrawable(textView7, ResUtil.generateRoundBorderDrawable(getResources(), 10, Color.BLUE, Color.CYAN));

        textView1.setGravity(Gravity.CENTER);
        textView2.setGravity(Gravity.CENTER);
        textView3.setGravity(Gravity.CENTER);
        textView4.setGravity(Gravity.CENTER);
        textView5.setGravity(Gravity.CENTER);
        textView6.setGravity(Gravity.CENTER);

        textView1.setText(getViewText(1));
        textView2.setText(getViewText(2));
        textView3.setText(getViewText(3));
        textView4.setText(getViewText(4));
        textView5.setText(getViewText(5));
        textView6.setText(getViewText(6));
    }

    private CharSequence getViewText(int n) {
        SpannableString spannableString = new SpannableString("20元\n加送10元");
        if (n == 1) {
            spannableString.setSpan(getSpan1(), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (n == 2) {
            spannableString.setSpan(getSpan2(), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (n == 3) {
            spannableString.setSpan(getSpan3(), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (n == 4) {
            spannableString.setSpan(getSpan4(), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (n == 5) {
            spannableString.setSpan(getSpan5(), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (n == 6) {
            spannableString.setSpan(getSpan6(), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    private CharacterStyle getSpan1() {
        return new TextAppearanceSpan(this, R.style.SpanTextStyle);
    }

    private CharacterStyle getSpan2() {
        return new AbsoluteSizeSpan(40, true);
    }

    private CharacterStyle getSpan3() {
        return new MySpan();
    }

    private CharacterStyle getSpan4() {
        return new MyAbsoluteSizeSpan(40, true);
    }

    private CharacterStyle getSpan5() {
        return new TextAppearanceSpan(this, R.style.SpanTextStyle);
    }

    private CharacterStyle getSpan6() {
        return new MySpan();
    }

    static class MySpan extends ReplacementSpan {

        private final int mSize = 100;
        int s;

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
//            paint.setTextSize(mSize);
            s = (int) paint.measureText(text, start, end);
            e("start-->" + start + " end-->" + end + " mSize-->" + mSize + " s-->" + s);
            return s;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            e("text-->" + text + " start-->" + start + " end-->" + end + " x-->" + x + " top-->" + top + " y-->" + y + " bottom-->" + bottom);
            paint.setColor(Color.RED);
//            paint.setTextSize(mSize);
//            canvas.drawCircle(x, y, y, paint);
            canvas.drawRect(x, top, x + s, bottom, paint);
            paint.setColor(Color.YELLOW);
            canvas.drawText(text, start, end, s/2, bottom/2 + y/2 , paint);
        }
    }

    public static class MyAbsoluteSizeSpan extends MetricAffectingSpan {

        private final int mSize;
        private boolean mDip;

        public MyAbsoluteSizeSpan(int size) {
            mSize = size;
        }
        public MyAbsoluteSizeSpan(int size, boolean dip) {
            mSize = size;
            mDip = dip;
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            if (mDip) {
                ds.setTextSize(mSize * ds.density);
            } else {
                ds.setTextSize(mSize);
            }
            ds.setColor(Color.LTGRAY);
        }

        @Override
        public void updateMeasureState(TextPaint ds) {
            if (mDip) {
                ds.setTextSize(mSize * ds.density);
            } else {
                ds.setTextSize(mSize);
            }
        }
    }
}
