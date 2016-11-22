package com.angcyo.sample.RecyclerViewDemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.base.RBaseAdapter;
import com.rsen.base.RBaseItemDecoration;
import com.rsen.base.RBaseViewHolder;
import com.rsen.util.ClipBoardUtil;
import com.rsen.util.T;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewItemDecorationDemo extends RBaseActivity {

    public static final String TAG = "demo-->";

    @Override
    protected int getContentView() {
        return R.layout.activity_recycler_view_item_decoration_demo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        final RecyclerView recyclerView = mViewHolder.reV(R.id.recycler_view);
        recyclerView.addItemDecoration(new RBaseItemDecoration());
        recyclerView.setAdapter(new DemoAdapter(this, getDatas()));

//        recyclerView.setLayoutTransition(new LayoutTransition());
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f);
        translateAnimation.setDuration(300);
        final LayoutAnimationController layoutAnimationController = new LayoutAnimationController(translateAnimation);
        recyclerView.setLayoutAnimation(layoutAnimationController);
//        recyclerView.startLayoutAnimation();

        ClipBoardUtil.openApp(this, "com.angcyo.angcyo.angcyo");
    }

    private List<String> getDatas() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("" + i);
        }
        return list;
    }

    public class DemoAdapter extends RBaseAdapter<String> {

        public DemoAdapter(Context context, List<String> datas) {
            super(context, datas);
        }

        @Override
        protected int getItemLayoutId(int viewType) {
            return 0;
        }

        @Override
        protected View createContentView(ViewGroup parent, int viewType) {
            TextView textView = new TextView(mContext);
            textView.setText(System.currentTimeMillis() + "\n" + "--------------------------------" +
                    "by angcyo.......................");

            textView.setBackgroundColor(Color.parseColor("#30000000"));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    T.show(RecyclerViewItemDecorationDemo.this, "Hello");
                }
            });
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(-2, -2);
            params.setMargins(10, 10, 10, 10);
            textView.setLayoutParams(params);
            return textView;
        }

        @Override
        protected void onBindView(RBaseViewHolder holder, int position, String bean) {

        }
    }

    public class Test extends RecyclerView.ItemDecoration {
        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }
}
