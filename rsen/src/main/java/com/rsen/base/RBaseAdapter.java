package com.rsen.base;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angcyo on 16-01-18-018.
 */
public abstract class RBaseAdapter<T> extends RecyclerView.Adapter<RBaseAdapter.RBaseViewHolder> {

    protected List<T> mAllDatas;
    protected Context mContext;


    public RBaseAdapter(Context context) {
        mAllDatas = new ArrayList<>();
        this.mContext = context;
    }

    public RBaseAdapter(Context context, List<T> datas) {
        this.mAllDatas = datas;
        this.mContext = context;

//        new View(context).setSelected();
    }

    @Override
    public RBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = View.inflate(parent.getContext(), getItemLayoutId(viewType), null);
        return new RBaseViewHolder(item);
    }

    protected abstract int getItemLayoutId(int viewType);

    @Override
    public void onBindViewHolder(RBaseViewHolder holder, int position) {
        onBindView(holder, position, mAllDatas.get(position));
    }

    protected abstract void onBindView(RBaseViewHolder holder, int position, T bean);

    @Override
    public int getItemCount() {
        return mAllDatas == null ? 0 : mAllDatas.size();
    }

    /**
     * 在最后的位置插入数据
     */
    public void addItemLast(T bean) {
        mAllDatas.add(bean);
        notifyItemInserted(mAllDatas.size() - 1);
    }

    /**
     * 重置数据
     */
    public void resetData(List<T> datas) {
        this.mAllDatas = datas;
        notifyDataSetChanged();
    }

    public List<T> getAllDatas() {
        return mAllDatas;
    }

    /**
     * 通用ViewHolder
     */
    public static class RBaseViewHolder extends RecyclerView.ViewHolder {
        public RBaseViewHolder(View itemView) {
            super(itemView);
        }

        public View v(@IdRes int resId) {
            return itemView.findViewById(resId);
        }

        public View view(@IdRes int resId) {
            return v(resId);
        }


        /**
         * 返回 TextView
         */
        public TextView tV(@IdRes int resId) {
            return (TextView) v(resId);
        }

        public TextView textView(@IdRes int resId) {
            return tV(resId);
        }

        /**
         * 返回 ImageView
         */
        public ImageView imgV(@IdRes int resId) {
            return (ImageView) v(resId);
        }

        public ImageView imageView(@IdRes int resId) {
            return imgV(resId);
        }

        /**
         * 返回 ViewGroup
         */
        public ViewGroup groupV(@IdRes int resId) {
            return (ViewGroup) v(resId);
        }

        public ViewGroup viewGroup(@IdRes int resId) {
            return groupV(resId);
        }
    }

}
