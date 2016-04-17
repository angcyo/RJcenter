package com.angcyo.sample.dudu;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.angcyo.sample.R;
import com.rsen.base.RBaseAdapter;
import com.rsen.base.RBaseViewHolder;
import com.rsen.dudu.DuduUploadBarLayout;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class DuduLayoutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dudu_layout_acivity);

        List<Drawable> list = new ArrayList<>();
        list.add(getResources().getDrawable(R.drawable.upload_1));
        list.add(getResources().getDrawable(R.drawable.upload_2));
        list.add(getResources().getDrawable(R.drawable.upload_3));
        list.add(getResources().getDrawable(R.drawable.upload_4));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        List<DuduBean> datas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            datas.add(new DuduBean(0));
        }
        recyclerView.setAdapter(new RBaseAdapter<DuduBean>(DuduLayoutActivity.this, datas) {
            @Override
            protected int getItemLayoutId(int viewType) {
                return R.layout.activity_dudu_layout_activity_item;
            }

            @Override
            protected void onBindView(RBaseViewHolder holder, int position, DuduBean bean) {
                DuduUploadBarLayout duduUpload = (DuduUploadBarLayout) holder.v("duduUpload");
                duduUpload.addFrame(list);
                duduUpload.setOnUploadChangeListener(new DuduUploadBarLayout.OnUploadChangeListener() {
                    @Override
                    public void onStateChange(View view, int oldState, int newState) {

                    }

                    @Override
                    public void onUploadClick(View view, View uploadView) {
                        bean.state = DuduUploadBarLayout.STATE_UPING;
                    }

                    @Override
                    public void onCancelClick(View view, View cancelView) {
                        bean.state = DuduUploadBarLayout.STATE_NORMAL;
                    }
                });


                if (bean.state == 0) {
                    duduUpload.setUpState(DuduUploadBarLayout.STATE_NORMAL);
                } else if (bean.state == 1) {
                    duduUpload.setUpState(DuduUploadBarLayout.STATE_UPING);
                } else if (bean.state == 2) {
                    duduUpload.setUpState(DuduUploadBarLayout.STATE_FINISH);
                }
            }

        });

        recyclerView.postDelayed(() -> {
            for (int i = 0; i < 5; i++) {
                datas.get(i + 10).state = DuduUploadBarLayout.STATE_FINISH;
            }
            recyclerView.getAdapter().notifyItemRangeChanged(10, 10 + 5);
        }, 2000);

    }

    public static class DuduBean {
        public int state = 0;

        public DuduBean(int state) {
            this.state = state;
        }
    }
}
