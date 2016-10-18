package com.rsen.angcyo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
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
    private static float WIDTH_STEP = 40;//每次宽度修改的步长
    private static float HEIGHT_STEP = 40;//每次高度修改的步长
    private static RDebugWindow sRDebugWindow;
    private static float MIN_WIDTH = 100;
    private static float MIN_HEIGHT = 60;
    float downX, downY;
    private float DEFAULT_BUTTON_SIZE = 20;
    private float DEFAULT_BUTTON_BG_WIDTH = 2;
    private RBaseViewHolder mBaseViewHolder;
    private Context mContext;
    private WindowManager mWindowManager;
    @IdRes
    private int rootViewId, recyclerViewId;
    private boolean isAdd = false;
    private boolean autoScroll = true;//默认激活自动滚动
    private WindowManager.LayoutParams mLayoutParams;

    private RDebugWindow(Context context) {
        if (context != null) {
            mContext = context.getApplicationContext();
            DEFAULT_BUTTON_SIZE = dpToPx(context, DEFAULT_BUTTON_SIZE);
            MIN_WIDTH = dpToPx(context, MIN_WIDTH);
            MIN_HEIGHT = dpToPx(context, MIN_HEIGHT);
            WIDTH_STEP = dpToPx(context, WIDTH_STEP);
            HEIGHT_STEP = dpToPx(context, HEIGHT_STEP);
            DEFAULT_BUTTON_BG_WIDTH = dpToPx(context, DEFAULT_BUTTON_BG_WIDTH);

            if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
                init();
            } else {
                ThreadExecutor.instance().onMain(() -> init());
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                }
            }
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

    public static Drawable generateCircleBgDrawable(float width, int color) {
        StateListDrawable bgStateDrawable = new StateListDrawable();//状态shape

        //按下状态
        Shape arcShape = new ArcShape(0, 360);
        ShapeDrawable shopDrawablePress = new ShapeDrawable(arcShape);//圆形shape
        shopDrawablePress.getPaint().setColor(color);//设置颜色
        shopDrawablePress.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);//设置颜色
        shopDrawablePress.getPaint().setStrokeWidth(width);//设置颜色
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{shopDrawablePress});
        layerDrawable.setLayerInset(0, (int) (width / 2), (int) (width / 2), (int) (width / 2), (int) (width / 2));

        bgStateDrawable.addState(new int[]{android.R.attr.state_pressed}, layerDrawable);//按下状态

        //正常状态
        arcShape = new ArcShape(0, 360);
        shopDrawablePress = new ShapeDrawable(arcShape);//圆形shape
        shopDrawablePress.getPaint().setColor(color);//设置颜色
        shopDrawablePress.getPaint().setStyle(Paint.Style.STROKE);//设置边框绘制模式
        shopDrawablePress.getPaint().setStrokeWidth(width);//设置宽度
        layerDrawable = new LayerDrawable(new Drawable[]{shopDrawablePress});
        layerDrawable.setLayerInset(0, (int) (width / 2), (int) (width / 2), (int) (width / 2), (int) (width / 2));

        bgStateDrawable.addState(new int[]{}, layerDrawable);//其他状态

        return bgStateDrawable;
    }

    public static float dpToPx(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    private void init() {
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mBaseViewHolder = new RBaseViewHolder(initView(mContext));
    }

    /**
     * 添加文本到窗口显示
     */
    public synchronized void addText(String text) {
        addText(text, -1);
    }

    /**
     * 添加文本到窗口显示
     */
    public synchronized void addText(String text, @ColorInt int color) {
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            addTextInternal(text, color);
        } else {
            ThreadExecutor.instance().onMain(() -> addTextInternal(text, color));
        }
    }

    private void addTextInternal(String text, @ColorInt int color) {
        if (mWindowManager == null) {
            return;
        }

        if (!isAdd) {
            mWindowManager.addView(mBaseViewHolder.itemView, initWindowParams());
            isAdd = true;
        }

        if (mBaseViewHolder != null) {
            RecyclerView recyclerView = mBaseViewHolder.reV(recyclerViewId);
            RBaseAdapter adapter = (RBaseAdapter) recyclerView.getAdapter();
            adapter.addLastItem(new Bean(text, color));
            if (autoScroll) {
                //滚动至列表末尾
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        }
    }

    /**
     * 设置窗口布局
     */
    private View initView(Context context) {
        FrameLayout frame = new FrameLayout(context);
        frame.setId(rootViewId = View.generateViewId());
        frame.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        frame.setBackgroundColor(Color.parseColor("#40000000"));

//        TextView textView = new TextView(context);
//        textView.setTextColor(Color.WHITE);
//        textView.setText("拖动此处改变位置,双击退出");
//        textView.setSingleLine();
//        textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        RRecyclerView recycler = new RRecyclerView(context);
        recycler.setTag("V");
        recycler.setId(recyclerViewId = View.generateViewId());
        recycler.setAdapter(new RAdapter(context, new ArrayList<>()));
        initRecyclerTouch(recycler);

        frame.addView(initControlLayout(context));//添加控制按钮布局
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = (int) (DEFAULT_BUTTON_SIZE + 10);
        frame.addView(recycler, layoutParams);

        positionTouch(frame);
        return frame;
    }

    private void initRecyclerTouch(RecyclerView recyclerView) {
        recyclerView.setOnTouchListener((v, event) -> {
            autoScroll = false;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if ((lastVisibleItemPosition + 1) == layoutManager.getItemCount()) {
                    //如果当前Item在最底下,开启自动滚动.
                    autoScroll = true;
                }
            }
            return false;
        });
    }

    /**
     * 设置窗口控制按钮布局
     */
    private View initControlLayout(Context context) {
        LinearLayoutCompat layoutCompat = new LinearLayoutCompat(context);
        layoutCompat.setOrientation(LinearLayoutCompat.HORIZONTAL);
        final int padding = 2;
        final int size = (int) DEFAULT_BUTTON_SIZE;
        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(size, size);
        layoutParams.setMargins(10, 2, 2, 2);

        //改变位置
        TextView posView = new TextView(context);
        posView.setTextColor(Color.WHITE);
        posView.setText("X");
        posView.setGravity(Gravity.CENTER);
        posView.setLayoutParams(new LinearLayoutCompat.LayoutParams(layoutParams));
        posView.setBackground(generateCircleBgDrawable(DEFAULT_BUTTON_BG_WIDTH, Color.RED));
        posView.setPadding(padding, padding, padding, padding);
        posView.setClickable(true);

        //改变宽度
        TextView widthView = new TextView(context);
        widthView.setTextColor(Color.WHITE);
        widthView.setText("宽");
        widthView.setGravity(Gravity.CENTER);
        widthView.setLayoutParams(new LinearLayoutCompat.LayoutParams(layoutParams));
        widthView.setBackground(generateCircleBgDrawable(DEFAULT_BUTTON_BG_WIDTH, Color.GREEN));
        widthView.setPadding(padding, padding, padding, padding);
        widthView.setClickable(true);

        //改变高度
        TextView heightView = new TextView(context);
        heightView.setTextColor(Color.WHITE);
        heightView.setText("高");
        heightView.setGravity(Gravity.CENTER);
        heightView.setLayoutParams(new LinearLayoutCompat.LayoutParams(layoutParams));
        heightView.setBackground(generateCircleBgDrawable(DEFAULT_BUTTON_BG_WIDTH, Color.YELLOW));
        heightView.setPadding(padding, padding, padding, padding);
        heightView.setClickable(true);

        //清理数据
        TextView cleanView = new TextView(context);
        cleanView.setTextColor(Color.WHITE);
        cleanView.setText("清");
        cleanView.setGravity(Gravity.CENTER);
        cleanView.setLayoutParams(new LinearLayoutCompat.LayoutParams(layoutParams));
        cleanView.setBackground(generateCircleBgDrawable(DEFAULT_BUTTON_BG_WIDTH, Color.BLUE));
        cleanView.setPadding(padding, padding, padding, padding);
        cleanView.setClickable(true);

        layoutCompat.addView(posView);
        layoutCompat.addView(widthView);
        layoutCompat.addView(heightView);
        layoutCompat.addView(cleanView);

        quitTouch(posView);
        widthTouch(widthView);
        heightTouch(heightView);
        cleanTouch(cleanView);
        return layoutCompat;
    }

    /**
     * 处理移动位置的事件
     */
    private void positionTouch(View view) {
        view.setOnTouchListener((v, event) -> {
            boolean handle = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getRawX();
                    downY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float moveX = event.getRawX();
                    final float moveY = event.getRawY();
                    updateLayoutParamsPosition(moveX - downX, moveY - downY);
                    downX = moveX;
                    downY = moveY;
                    handle = true;
                    break;
            }
            return handle;
        });
//        final GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onDoubleTap(MotionEvent e) {
//                removeLayout();
//                Log.e(TAG, "onDoubleTap: ");
//                return true;
//            }
//
//            @Override
//            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
////                updateLayoutParamsPosition(distanceX, distanceY);
//                updateLayoutParamsPosition(e2.getRawX() - e1.getRawX(), e2.getRawY() - e1.getRawY());
//                Log.e(TAG, "onScroll: " + distanceX + " " + distanceY);
//                return true;
//            }
//
//        };
//        final GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(view.getContext(), simpleOnGestureListener);
//        view.setOnTouchListener((v, event) -> gestureDetectorCompat.onTouchEvent(event));
    }

    private void quitTouch(View view) {
        final GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                removeLayout();
                return true;
            }
        };
        final GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(view.getContext(), simpleOnGestureListener);
        view.setOnTouchListener((v, event) -> gestureDetectorCompat.onTouchEvent(event));
    }

    /**
     * 处理宽度事件
     */
    private void widthTouch(View view) {
        final GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                updateLayoutParamsWidth(WIDTH_STEP);//双击变大20像素
                Log.e(TAG, "onDoubleTap: ");
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                updateLayoutParamsWidth(WIDTH_STEP);//双击变大20像素
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                updateLayoutParamsWidth(-WIDTH_STEP);//单击变小20像素
                Log.e(TAG, "onSingleTapConfirmed: ");
                return true;
            }
        };
        final GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(view.getContext(), simpleOnGestureListener);
        view.setOnTouchListener((v, event) -> gestureDetectorCompat.onTouchEvent(event));
    }

    /**
     * 处理高度事件
     */
    private void heightTouch(View view) {
        final GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                updateLayoutParamsHeight(HEIGHT_STEP);//双击变大20像素
                Log.e(TAG, "onDoubleTap: ");
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                updateLayoutParamsHeight(HEIGHT_STEP);//双击变大20像素
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                updateLayoutParamsHeight(-HEIGHT_STEP);//单击变小20像素
                Log.e(TAG, "onSingleTapConfirmed: ");
                return true;
            }
        };
        final GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(view.getContext(), simpleOnGestureListener);
        view.setOnTouchListener((v, event) -> gestureDetectorCompat.onTouchEvent(event));
    }

    /**
     * 处理清理事件
     */
    private void cleanTouch(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView = mBaseViewHolder.reV(recyclerViewId);
                RAdapter adapter = (RAdapter) recyclerView.getAdapter();
                adapter.resetData(new ArrayList<>());
            }
        });
    }

    private void removeLayout() {
        mWindowManager.removeView(mBaseViewHolder.itemView);
        isAdd = false;
    }

    private void updateLayoutParamsPosition(float x, float y) {
        mWindowManager.updateViewLayout(mBaseViewHolder.itemView, createMoveParams((int) x, (int) y));
    }

    private void updateLayoutParamsWidth(float widthStep) {
        mWindowManager.updateViewLayout(mBaseViewHolder.itemView, createWidthParams((int) widthStep));
    }

    private void updateLayoutParamsHeight(float heightStep) {
        mWindowManager.updateViewLayout(mBaseViewHolder.itemView, createHeightParams((int) heightStep));
    }

    private WindowManager.LayoutParams createWidthParams(int widthStep) {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) mBaseViewHolder.itemView.getLayoutParams();
        params.width += widthStep;

        if (widthStep < 0) {
            params.width = (int) Math.max(params.width, MIN_WIDTH);
        }

        return params;
    }

    private WindowManager.LayoutParams createHeightParams(int heightStep) {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) mBaseViewHolder.itemView.getLayoutParams();
        params.height += heightStep;

        if (heightStep < 0) {
            params.height = (int) Math.max(params.height, MIN_HEIGHT);
        }

        return params;
    }

    private WindowManager.LayoutParams createMoveParams(int x, int y) {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) mBaseViewHolder.itemView.getLayoutParams();

//        params.x += x;
//        params.y += y;

        if ((params.gravity & Gravity.RIGHT) == Gravity.RIGHT) {
            //右对齐的话, 坐标从右边开始计算
//            params.x = getWindowWidth() - x;
            params.x -= x;
        } else {
            params.x += x;
        }
//
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
            mLayoutParams.width = getWindowWidth() * 2 / 3;
            mLayoutParams.height = getWindowHeight() / 2;
            mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
            mLayoutParams.x = (int) MIN_WIDTH;
            mLayoutParams.y = (int) MIN_HEIGHT;
            mLayoutParams.format = PixelFormat.RGBA_8888;
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;//窗口类型
            mLayoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN// 覆盖状态栏
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL//窗口外可以点击
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE//不监听按键事件
//                    | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
//                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS//突破窗口限制
//                    | WindowManager.LayoutParams.FLAG_FULLSCREEN
            ;
        }
        return mLayoutParams;
    }

    static class Bean {
        public String text;
        @ColorInt
        public int color = -1;

        public Bean(String text, @ColorInt int color) {
            this.text = text;
            this.color = color;
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
        protected View createContentView(ViewGroup parent, int viewType) {
            RelativeLayout layout = new RelativeLayout(mContext);
            layout.setId(this.itemViewId = View.generateViewId());
            layout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView textView = new TextView(mContext);
            textView.setId(textViewId = View.generateViewId());
            layout.addView(textView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);

            return layout;
        }

        @Override
        protected void onBindView(RBaseViewHolder holder, int position, Bean bean) {
//                holder.tV(textViewId).setText(bean.text);
            //动态创建的View Id,在这个地方会出现空指针BUG,
            View childAt = ((ViewGroup) holder.itemView).getChildAt(0);
            if (childAt instanceof TextView) {
                final TextView textView = (TextView) childAt;
                textView.setText((position + 1) + ":" + bean.text);

                int color = bean.color;
                if (color == -1) {
                    if (position % 2 == 0) {
                        color = Color.WHITE;
                    } else {
                        color = Color.MAGENTA;
                    }
                }
                textView.setTextColor(color);
            }
        }
    }
}
