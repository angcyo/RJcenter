package com.rsen.base;

/**
 * Created by angcyo on 2016-01-30.
 */

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rsen.viewgroup.RRecyclerView;

import java.lang.reflect.Field;

/**
 * 通用ViewHolder
 */
public class RBaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> sparseArray;
    private int viewType = -1;

    public RBaseViewHolder(View itemView) {
        super(itemView);
        sparseArray = new SparseArray();
    }

    public RBaseViewHolder(View itemView, int viewType) {
        super(itemView);
        sparseArray = new SparseArray();
        this.viewType = viewType;
    }

    /**
     * 填充两个字段相同的数据对象
     */
    public static void fill(Object from, Object to) {
        Field[] fromFields = from.getClass().getDeclaredFields();
        Field[] toFields = to.getClass().getDeclaredFields();
        for (Field f : fromFields) {
            String name = f.getName();
            for (Field t : toFields) {
                String tName = t.getName();
                if (name.equalsIgnoreCase(tName)) {
                    try {
                        f.setAccessible(true);
                        t.setAccessible(true);
                        t.set(to, f.get(from));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

        }
    }

    public int getViewType() {
        return viewType;
    }

    public <T extends View> T v(@IdRes int resId) {
        View view = sparseArray.get(resId);
        if (view == null) {
            view = itemView.findViewById(resId);
            sparseArray.put(resId, view);
        }
        return (T) view;
    }

    public <T extends View> T v(String idName) {
        return (T) viewByName(idName);
    }

    public View view(@IdRes int resId) {
        return v(resId);
    }

    public RRecyclerView reV(@IdRes int resId) {
        return (RRecyclerView) v(resId);
    }

    public RRecyclerView reV(String idName) {
        return (RRecyclerView) viewByName(idName);
    }

    /**
     * 返回 TextView
     */
    public TextView tV(@IdRes int resId) {
        return (TextView) v(resId);
    }

    public TextView tv(@IdRes int resId) {
        return (TextView) v(resId);
    }

    public TextView tV(String idName) {
        return (TextView) v(idName);
    }

    public TextView textView(@IdRes int resId) {
        return tV(resId);
    }

    /**
     * 返回 CompoundButton
     */
    public CompoundButton cV(@IdRes int resId) {
        return (CompoundButton) v(resId);
    }

    public CompoundButton cV(String idName) {
        return (CompoundButton) v(idName);
    }

    /**
     * 返回 EditText
     */
    public EditText eV(@IdRes int resId) {
        return (EditText) v(resId);
    }

    public EditText editView(@IdRes int resId) {
        return eV(resId);
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

    public RecyclerView r(@IdRes int resId) {
        return (RecyclerView) v(resId);
    }

    public View viewByName(String name) {
        View view = v(getIdByName(name, "id"));
        return view;
    }

    /**
     * 根据name, 在主题中 寻找资源id
     */
    private int getIdByName(String name, String type) {
        Context context = itemView.getContext();
        return context.getResources().getIdentifier(name, type, context.getPackageName());
    }

    public void fillView(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            String name = f.getName();
            try {
                View view = viewByName(name);
                if (view instanceof TextView) {
                    ((TextView) view).setText(f.get(bean).toString());
                } else if (view instanceof ImageView) {

                }

            } catch (Exception e) {
            }
        }
    }

    public void post(Runnable runnable) {
        itemView.post(runnable);
    }

    public void postDelay(Runnable runnable, long delayMillis) {
        itemView.postDelayed(runnable, delayMillis);
    }
}
