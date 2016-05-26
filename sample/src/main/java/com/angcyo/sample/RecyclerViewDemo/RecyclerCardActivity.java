package com.angcyo.sample.RecyclerViewDemo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.base.RBaseAdapter;
import com.rsen.base.RBaseViewHolder;

import java.util.ArrayList;

public class RecyclerCardActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_recycler_card;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ArrayList<String> datas = new ArrayList<>();
        datas.add("");
        datas.add("");
        datas.add("");
        datas.add("");
        datas.add("");
        RBaseAdapter<String> rBaseAdapter = new RBaseAdapter<String>(this, datas) {
            @Override
            protected int getItemLayoutId(int viewType) {
                return R.layout.activity_recycler_card_item;
            }

            @Override
            protected void onBindView(RBaseViewHolder holder, int position, String bean) {
            }

        };
        mViewHolder.reV(R.id.recyclerView).setAdapter(rBaseAdapter);

        mViewHolder.eV(R.id.editText).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                datas.add("");
                rBaseAdapter.resetData(datas);
            }
        });
    }
}
