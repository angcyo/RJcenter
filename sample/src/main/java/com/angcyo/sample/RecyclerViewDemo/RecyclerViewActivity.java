package com.angcyo.sample.RecyclerViewDemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.base.RBaseAdapter;
import com.rsen.base.RBaseViewHolder;
import com.rsen.viewgroup.RRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_recycler_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//        getTheme().applyStyle(R.style.Test1, true);
//        setTheme(com.angcyo.rsen.R.style.Test1);
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20ffff00")));

        final List<String> datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add(new String("" + i));
        }
        mViewHolder.reV("recycler").setAdapter(new RBaseAdapter<String>(this, datas) {

            @Override
            protected int getItemLayoutId(int viewType) {
                return 0;
            }

            @Override
            protected View createContentView(int viewType) {
                if (viewType == 1) {
                    RRecyclerView recyclerView = new RRecyclerView(mContext);
                    recyclerView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
                    return recyclerView;
                }

                TextView textView = new TextView(mContext);
                textView.setBackgroundColor(Color.BLUE);
                textView.setLayoutParams(new ViewGroup.LayoutParams(-1, 100));
                return textView;
            }

            @Override
            public int getItemViewType(int position) {
                if (position == 0) {
                    return 1;
                }
                return super.getItemViewType(position);
            }

            @Override
            protected void onBindView(RBaseViewHolder holder, int position, String bean) {
                if (position == 0) {
                    ((RRecyclerView) holder.itemView).setAdapter(new RBaseAdapter<String>(mContext, datas) {

                        @Override
                        protected int getItemLayoutId(int viewType) {
                            return 0;
                        }

                        @Override
                        protected View createContentView(int viewType) {
                            TextView textView = new TextView(mContext);
                            textView.setBackgroundColor(Color.RED);
                            textView.setLayoutParams(new ViewGroup.LayoutParams(-1, 300));
                            return textView;
                        }

                        @Override
                        protected void onBindView(RBaseViewHolder holder, int position, String bean) {
                            ((TextView) holder.itemView).setText("sub    --->" + position);
                        }
                    });
                } else {
                    ((TextView) holder.itemView).setText("--->" + position);
                }
            }
        });
    }

    @Override
    protected void initAfter() {
//        Slidr.attach(this);
        Intent intent = new Intent(this, RecyclerViewActivity2.class);
        startActivity(intent);
    }

    @Override
    protected boolean enableSlidr() {
        return true;
    }
}
