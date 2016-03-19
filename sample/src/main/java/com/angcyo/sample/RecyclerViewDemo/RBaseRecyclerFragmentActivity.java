package com.angcyo.sample.RecyclerViewDemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rsen.base.RBaseActivity;
import com.rsen.base.RBaseAdapter;
import com.rsen.base.RBaseRecyclerFragment;
import com.rsen.base.RBaseViewHolder;
import com.rsen.effect.MyItemAnimator2;
import com.rsen.effect.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class RBaseRecyclerFragmentActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return 0;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        replaceFragment(new DemoRecyclerFragment());
    }

    @Override
    protected void initAfter() {

    }


    public static class DemoRecyclerFragment extends RBaseRecyclerFragment {
        @Override
        protected RBaseAdapter makeAdapter() {
            ArrayList<DemoBean> beans = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                beans.add(new DemoBean());
            }
            return new DemoAdapter(mBaseActivity, beans);
        }

        @Override
        protected void initRecyclerView() {
            super.initRecyclerView();
            ViewUtils.init(mBaseActivity);
//            mRecyclerView.setItemAnimator(new RPackageAnimator(
//                    new StandUpIn().setAnimationDivider(200), new FlipXOut()));
//            mRecyclerView.setItemAnimator(new MyItemAnimator());
            mRecyclerView.setItemAnimator(new MyItemAnimator2());
        }
    }

    public static class DemoBean {

    }

    public static class DemoAdapter extends RBaseAdapter<DemoBean> {

        public DemoAdapter(Context context, List<DemoBean> datas) {
            super(context, datas);
        }

        @Override
        protected int getItemLayoutId(int viewType) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        protected View createContentView(int viewType) {
            LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, 300));

            TextView textView = new TextView(mContext);
            textView.setBackgroundColor(Color.LTGRAY);

            Button button = new Button(mContext);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(v.getId() + "  " + v.toString() + "  " + v.hashCode());
//                    addLatItem(new DemoBean());
//                    removeLastItem();
                    addFirstItem(new DemoBean());
                }
            });
            button.setText("点我,点我...");
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.setMargins(10, 10, 10, 10);
            button.setLayoutParams(layoutParams);
            button.setElegantTextHeight(true);
            button.setHeight(0);

            linearLayout.addView(textView);
            linearLayout.addView(button);

            Button button1 = new Button(mContext, null, 0);
            button1.setLayoutParams(layoutParams);
            button1.setText("点我,点我, 点我");
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(v.getId() + "  " + v.toString() + "  " + v.hashCode());
//                    addFirstItem(new DemoBean());
                    removeFirstItem();
                }
            });

            linearLayout.addView(button1);
            return linearLayout;
        }

        @Override
        protected void onBindView(RBaseViewHolder holder, int position, DemoBean bean) {
            ((TextView) ((LinearLayout) holder.itemView).getChildAt(0)).setText("很长的文本-->" + position);
        }
    }

}

