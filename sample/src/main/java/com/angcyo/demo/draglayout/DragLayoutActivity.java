package com.angcyo.demo.draglayout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.angcyo.sample.R;

public class DragLayoutActivity extends Activity {

    private RDragLayout dragLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_layout);

        dragLayout = (RDragLayout) findViewById(R.id.dragLayout);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        View view = findViewById(R.id.add);
        View view2 = findViewById(R.id.button2);
        View view3 = findViewById(R.id.button3);
        View view4 = findViewById(R.id.button4);
        View view5 = findViewById(R.id.button5);

        DragTouchListener dragTouchListener = new DragTouchListener();
        view.setOnTouchListener(dragTouchListener);
        imageView.setOnTouchListener(dragTouchListener);
        view2.setOnTouchListener(dragTouchListener);
        view3.setOnTouchListener(dragTouchListener);
        view4.setOnTouchListener(dragTouchListener);
        view5.setOnTouchListener(dragTouchListener);
    }

    class DragTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.e("angcyo", "onTouch");
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dragLayout.setDragView(v);
            }
            return false;
        }
    }
}
