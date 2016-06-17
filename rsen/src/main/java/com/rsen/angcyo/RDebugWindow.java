package com.rsen.angcyo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rsen.base.RBaseAdapter;
import com.rsen.base.RBaseViewHolder;
import com.rsen.util.ThreadExecutor;
import com.rsen.viewgroup.RRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robi on 2016-06-17 11:09.
 */
public class RDebugWindow {

    private static final String TAG = "DebugWindow";
    private static RDebugWindow sRDebugWindow;
    float downX, downY;
    long downTime;//按下时间
    private RBaseViewHolder mBaseViewHolder;
    private Context mContext;
    private WindowManager mWindowManager;
    @IdRes
    private int rootViewId, recyclerViewId;
    private boolean isAdd = false;
    private WindowManager.LayoutParams mLayoutParams;

    private RDebugWindow(Context context) {
        mContext = context;
        if (mContext != null) {
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            mBaseViewHolder = new RBaseViewHolder(initView(mContext));

            mWindowManager.addView(mBaseViewHolder.itemView, initWindowParams());
            isAdd = true;
        }
    }

    /**
     * 获取实例
     */
    public static RDebugWindow instance(Context context) {
        if (sRDebugWindow == null) {
            synchronized (RDebugWindow.class) {
                if (sRDebugWindow == null) {
                    sRDebugWindow = new RDebugWindow(context);
                }
            }
        }
        return sRDebugWindow;
    }

    /**
     * 添加文本到窗口显示
     */
    public synchronized void addText(String text) {
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            addTextInternal(text);
        } else {
            ThreadExecutor.instance().onMain(() -> addTextInternal(text));
        }
    }

    private void addTextInternal(String text) {
        if (!isAdd) {
            mWindowManager.addView(mBaseViewHolder.itemView, initWindowParams());
            isAdd = true;
        }

        if (mBaseViewHolder != null) {
            RecyclerView recyclerView = mBaseViewHolder.reV(recyclerViewId);
            RBaseAdapter adapter = (RBaseAdapter) recyclerView.getAdapter();
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            boolean needScroll = false;
            if ((lastVisibleItemPosition + 1) == adapter.getItemCount()) {
                //如果当前Item在最底下,开启自动滚动.
                needScroll = true;
            }
            adapter.addLatItem(new Bean(text));
            if (needScroll) {
                //滚动至列表末尾
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        }
    }

    private View initView(Context context) {
        FrameLayout frame = new FrameLayout(context);
        frame.setId(rootViewId = View.generateViewId());
        frame.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        frame.setBackgroundColor(Color.parseColor("#40000000"));

        TextView textView = new TextView(context);
        textView.setTextColor(Color.WHITE);
        textView.setText("拖动此处改变位置,双击退出");
        textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        RRecyclerView recycler = new RRecyclerView(context);
        recycler.setTag("V");
        recycler.setId(recyclerViewId = View.generateViewId());
        recycler.setAdapter(new RAdapter(context, new ArrayList<>()));

        frame.addView(textView);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = 40;
        frame.addView(recycler, layoutParams);

        frame.setOnTouchListener((v, event) -> {
            boolean handle = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    final long time = System.currentTimeMillis();
                    downX = event.getRawX();
                    downY = event.getRawY();

                    if ((time - downTime) < 500) {
                        removeLayout();
                        handle = true;
                    }
                    downTime = time;
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float moveX = event.getRawX();
                    final float moveY = event.getRawY();
                    updateLayoutParams(moveX - downX, moveY - downY);
                    downX = moveX;
                    downY = moveY;
                    handle = true;
                    break;
            }
            return handle;
        });
        return frame;
    }

    private void removeLayout() {
        mWindowManager.removeView(mBaseViewHolder.itemView);
        isAdd = false;
    }

    private void updateLayoutParams(float x, float y) {
        mWindowManager.updateViewLayout(mBaseViewHolder.itemView, createMoveParams((int) x, (int) y));
    }

    private WindowManager.LayoutParams createMoveParams(int x, int y) {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) mBaseViewHolder.itemView.getLayoutParams();

        if ((params.gravity & Gravity.RIGHT) == Gravity.RIGHT) {
            //右对齐的话, 坐标从右边开始计算
//            params.x = getWindowWidth() - x;
            params.x -= x;
        } else {
            params.x += x;
        }

        if ((params.gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
//            params.y = getWindowHeight() - y;
            params.y -= y;
        } else {
            params.y += y;
        }
        return params;
    }

    /**
     * 获取窗口宽度
     */
    private int getWindowWidth() {
        Point point = new Point();
        mWindowManager.getDefaultDisplay().getSize(point);
        return point.x;
    }

    /**
     * 获取窗口高度
     */
    private int getWindowHeight() {
        Point point = new Point();
        mWindowManager.getDefaultDisplay().getSize(point);
        return point.y;
    }

    /**
     * 创建浮窗参数
     */
    private WindowManager.LayoutParams initWindowParams() {
        if (mLayoutParams == null) {
            mLayoutParams = new WindowManager.LayoutParams();
            mLayoutParams.width = 500;
            mLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            mLayoutParams.gravity = Gravity.RIGHT;
            mLayoutParams.format = PixelFormat.RGBA_8888;
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;//窗口类型
            mLayoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN// 覆盖状态栏
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL//窗口外可以点击
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE//不监听按键事件
            ;
        }
        return mLayoutParams;
    }

    static class Bean {
        public String text;

        public Bean(String text) {
            this.text = text;
        }
    }

    class RAdapter extends RBaseAdapter<Bean> {

        @IdRes
        private int itemViewId, textViewId;

        public RAdapter(Context context, List<Bean> datas) {
            super(context, datas);
        }

        @Override
        protected int getItemLayoutId(int viewType) {
            return 0;
        }

        @Override
        protected View createContentView(int viewType) {
            RelativeLayout layout = new RelativeLayout(mContext);
            layout.setId(this.itemViewId = View.generateViewId());
            layout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView textView = new TextView(mContext);
            textView.setId(textViewId = View.generateViewId());
            layout.addView(textView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextColor(Color.WHITE);

            return layout;
        }

        @Override
        protected void onBindView(RBaseViewHolder holder, int position, Bean bean) {
//                holder.tV(textViewId).setText(bean.text);
            //动态创建的View Id,在这个地方会出现空指针BUG,
            View childAt = ((ViewGroup) holder.itemView).getChildAt(0);
            if (childAt instanceof TextView) {
                ((TextView) childAt).setText(bean.text);
            }
        }
    }
}
