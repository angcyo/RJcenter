package com.angcyo.sample.SurfaceViewMathDemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by angcyo on 2016-01-25.
 */
public class SurfaceMathView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private boolean isCreate;

    public SurfaceMathView(Context context) {
        this(context, null);
    }

    public SurfaceMathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceMathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isCreate = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isCreate = false;
    }

    @Override
    public void run() {
        int x = 0;
        int y;
        Path path;
        Path path2;
        Paint paint;
        TextPaint textPaint;
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);

        textPaint = new TextPaint();
        textPaint.setTextSize(12);
        textPaint.setColor(Color.GREEN);
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 22f, getResources().getDisplayMetrics()));

        path = new Path();
        path2 = new Path();

        int offset = getMeasuredHeight() / 4;
        int siny = (int) (100 * Math.sin(0 * Math.PI / 180) + offset);
        int cosy = offset * 2 - (int) (100 * Math.cos(0 * Math.PI / 180));

        path.moveTo(x, siny);
        path2.moveTo(x, cosy);
        while (isCreate) {
            try {
                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.WHITE);

                //正弦函数
                y = (int) (100 * Math.sin(x * Math.PI / 180));
                path.lineTo(x, siny - y);
                paint.setColor(Color.RED);
                canvas.drawPath(path, paint);
                paint.setColor(Color.BLUE);
                canvas.drawLine(0, offset, getMeasuredWidth(), offset, paint);
                canvas.drawText("Sin 函数曲线↓", 0, offset - 100 - 60, textPaint);

                //余弦函数
                y = (int) (100 * Math.cos(x * Math.PI / 180));
                path2.lineTo(x, offset * 2 - y);
                paint.setColor(Color.RED);
                canvas.drawPath(path2, paint);
                paint.setColor(Color.BLUE);
                canvas.drawLine(0, offset * 2, getMeasuredWidth(), offset * 2, paint);
                canvas.drawText("Cos 函数曲线↓", 0, offset * 2 - 100 - 60, textPaint);

                x++;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
