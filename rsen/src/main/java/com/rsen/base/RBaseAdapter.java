package com.rsen.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angcyo on 16-01-18-018.
 */
public abstract class RBaseAdapter<T> extends RecyclerView.Adapter<RBaseViewHolder> {

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
        int itemLayoutId = getItemLayoutId(viewType);
        View item;
        if (itemLayoutId == 0) {
            item = createContentView(viewType);
        } else {
            item = View.inflate(parent.getContext(), itemLayoutId, null);
        }

        return new RBaseViewHolder(item);
    }

    protected View createContentView(int viewType) {
        return null;
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
    public void addLatItem(T bean) {
        mAllDatas.add(bean);
        notifyItemInserted(mAllDatas.size() - 1);
    }

    public void addFirstItem(T bean) {
        List<T> tempBeans = new ArrayList<>();
        tempBeans.add(bean);
        tempBeans.addAll(mAllDatas);
        mAllDatas.clear();
        mAllDatas = tempBeans;
        notifyItemInserted(0);
    }

    public void removeFirstItem() {
        mAllDatas.remove(0);
        notifyItemRemoved(0);
    }

    public void removeLastItem() {
        int last = mAllDatas.size() - 1;
        mAllDatas.remove(last);
        notifyItemRemoved(last);
    }

    /**
     * 重置数据
     */
    public void resetData(List<T> datas) {
        this.mAllDatas = datas;
        notifyDataSetChanged();
    }

    /**
     * 追加数据
     */
    public void appendData(List<T> datas) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        if (this.mAllDatas == null) {
            this.mAllDatas = new ArrayList<>();
        }
        int startPosition = this.mAllDatas.size();
        this.mAllDatas.addAll(datas);
        notifyItemRangeInserted(startPosition, datas.size());
    }

    public List<T> getAllDatas() {
        return mAllDatas;
    }
}
