package com.angcyo.sample.SurfaceViewMathDemo;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.angcyo.sample.R;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class TextureViewActivity extends Activity implements TextureView.SurfaceTextureListener, Runnable {
    float alpha;
    boolean isCreate;
    Canvas canvas;
    private Camera mCamera;
    private TextureView mTextureView, mTextureView2;
    private Button mRotate;
    private float rotate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//        mTextureView = new TextureView(this);
        mTextureView = (TextureView) findViewById(R.id.textureView);
        mRotate = (Button) findViewById(R.id.rotate);

        mTextureView.setSurfaceTextureListener(this);

//        mTextureView.setAlpha(0.5f);
//        mTextureView.setOpaque(false);

        mRotate.setOnClickListener(v -> {
            rotate += 90;
            mTextureView.setRotation(rotate % 360);
        });


        mTextureView2 = (TextureView) findViewById(R.id.textureView2);
        mTextureView2.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                isCreate = true;
                new Thread(TextureViewActivity.this).start();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                isCreate = false;

                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(1920, 1080);
        parameters.setPictureSize(1920, 1080);
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//        parameters.setRotation(90);
//        parameters.setPreviewFormat();
        mCamera.setParameters(parameters);
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
            mCamera.autoFocus((success, camera) -> {

            });
            mCamera.cancelAutoFocus();
//            mCamera.setDisplayOrientation(Surface.ROTATION_90);
        } catch (IOException ioe) {
            // Something bad happened
        }

    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Ignored, Camera does all the work for us
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mCamera == null) {
            return false;
        }
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Invoked every time there's a new Camera preview frame
    }

    public void getBitmap(View view) {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(mTextureView.getBitmap(imageView.getWidth(), imageView.getHeight()));
    }

    public void demo(View view) {
        mTextureView.setOpaque(!mTextureView.isOpaque());
    }

    public void setAlpha(View view) {
        alpha++;
        mTextureView.setAlpha(alpha % 10 / 10f);
    }

    private int getMeasuredHeight() {
        return mTextureView2.getHeight();

    }

    private int getMeasuredWidth() {
        return mTextureView2.getWidth();
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
                canvas = mTextureView2.lockCanvas();
//                canvas.drawColor(Color.WHITE);

                //正弦函数
                canvas.drawLine(0, offset, getMeasuredWidth(), offset, paint);
                canvas.drawText("Sin 函数曲线↓", 0, offset - 100 - 60, textPaint);
                y = (int) (100 * Math.sin(x * Math.PI / 180));
                path.lineTo(x, siny - y);
                paint.setColor(Color.RED);
                canvas.drawPath(path, paint);
                paint.setColor(Color.BLUE);

                //余弦函数
                canvas.drawLine(0, offset * 2, getMeasuredWidth(), offset * 2, paint);
                canvas.drawText("Cos 函数曲线↓", 0, offset * 2 - 100 - 60, textPaint);
                y = (int) (100 * Math.cos(x * Math.PI / 180));
                path2.lineTo(x, offset * 2 - y);
                paint.setColor(Color.RED);
                canvas.drawPath(path2, paint);
                paint.setColor(Color.BLUE);

                x++;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    mTextureView2.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

}
