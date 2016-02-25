package com.rsen.base;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.angcyo.rsen.R;

/**
 * Created by angcyo on 16-02-25-025.
 */
public abstract class RBaseRecyclerFragment extends RBaseFragment {

    protected RecyclerView mRecyclerView;
    protected RBaseAdapter mRecyclerAdapter;

    @Override
    protected int getContentView() {
        return R.layout.rsen_base_recycler_fragment_layout;
    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView(View rootView) {
        mRecyclerView = (RecyclerView) mViewHolder.viewByName("recycler_view");
        initRecyclerView();
    }

    protected void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mBaseActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerAdapter = makeAdapter();
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    protected abstract RBaseAdapter makeAdapter();
}
