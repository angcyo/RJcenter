package com.angcyo.sample.RecyclerViewDemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class RecyclerViewScrollerDemo extends RBaseActivity {

    RRecyclerView mRecyclerView;
    String TAG = "angcyo";
    LinearLayoutManager layoutManager;

    @Override
    protected int getContentView() {
        return R.layout.activity_recycler_view_scroller_demo;
    }

    @Override
    protected boolean enableStatusColor() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        hideToolbar(true);

        mRecyclerView = (RRecyclerView) mViewHolder.v(R.id.recyclerView);
        layoutManager = new ScrollerLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void initAfter() {
        mRecyclerView.setAdapter(new ScrollerAdapter(this, null));

//        mViewHolder.postDelay(() -> {
//            int visibleItem = layoutManager.findFirstVisibleItemPosition();
//            if (visibleItem > -1) {
//                View child = layoutManager.findViewByPosition(visibleItem);
////                child.setLayoutParams(new RecyclerView.LayoutParams(100, 100));
//                child.setBackgroundColor(Color.RED);
//            }
//
////            layoutManager.requestLayout();
//        }, 1000);
    }

    @Override
    protected void initViewData() {
        ArrayList<ScrollerBean> datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add(new ScrollerBean());
        }
        mRecyclerView.getAdapterRaw().resetData(datas);
    }

    protected class ScrollerAdapter extends RBaseAdapter<ScrollerBean> {

        public ScrollerAdapter(Context context, List<ScrollerBean> datas) {
            super(context, datas);
        }

        @Override
        protected int getItemLayoutId(int viewType) {
            return 0;
        }

        @Override
        protected View createContentView(int viewType) {
            View view = new TextView(mContext);

/*            int visibleItem = layoutManager.findFirstVisibleItemPosition();
            if (visibleItem > -1) {
                for (int i = visibleItem; i < layoutManager.getChildCount(); i++) {
                    View child = layoutManager.findViewByPosition(i);
                    final int top = child.getTop();
                    final int bottom = child.getBottom();

                    Log.i(TAG, "createContentView: " + top + "  " + bottom);
                    child.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (top * 0.75)));
                }
            }*/
//            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
            return view;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
//            Log.d(TAG, "onScrolled: ");
//
/*            int visibleItem = layoutManager.findFirstVisibleItemPosition();
            if (visibleItem > -1) {
                View child = layoutManager.findViewByPosition(visibleItem);
//                child.setLayoutParams(new RecyclerView.LayoutParams(100, 100));
                child.setBackgroundColor(Color.RED);
            }*/


//            int visibleItem = layoutManager.findFirstVisibleItemPosition();
//            if (visibleItem > -1) {
//                for (int i = 0; i < layoutManager.getChildCount(); i++) {
//                    View child = layoutManager.findViewByPosition(visibleItem + i);
//                    if (child != null) {
//
//                        final int top = child.getTop();
//                        final int bottom = child.getBottom();
//
//                        Log.i(TAG, i + " onScrolled: " + top + "  " + bottom);
////                    child.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (top * 0.75)));
//                        child.getLayoutParams().height = (int) (top * 0.75);
//                        child.getLayoutParams().width = getScreenWidth();
//                    }
//                }
//                layoutManager.requestLayout();
//            } else {
//                Log.e(TAG, "onScrolled: xxxxxx");
//            }
            setItemSize();
            layoutManager.requestLayout();
        }

        private void setItemSize() {
            int item = layoutManager.findFirstCompletelyVisibleItemPosition();
            int screenWidth = getScreenWidth();
            for (int i = 0; layoutManager.getChildCount() > 3 && i < layoutManager.getChildCount(); i++) {
                final View view = layoutManager.findViewByPosition(item + i);
                if (view == null) {
                    continue;
                }
                if (i < 3) {
                    Log.i(TAG, "setItemSize: " + view.getTop() + " " + view.getBottom());
                    view.getLayoutParams().height = 200 + (3 - i) * 100;
                    view.getLayoutParams().width = screenWidth;
                } else {
                    view.getLayoutParams().height = 100;
                    view.getLayoutParams().width = screenWidth;
                }
            }

//                int item = layoutManager.findFirstCompletelyVisibleItemPosition();
//                final View view = layoutManager.findViewByPosition(item);
//                if (view != null) {
//                    view.getLayoutParams().height = 400;
//                    view.getLayoutParams().width = getScreenWidth();
//                }
//                final View view2 = layoutManager.findViewByPosition(item + 1);
//                if (view2 != null) {
//                    view2.getLayoutParams().height = 300;
//                    view2.getLayoutParams().width = getScreenWidth();
//                }
//                final View view3 = layoutManager.findViewByPosition(item + 2);
//                if (view3 != null) {
//                    view3.getLayoutParams().height = 200;
//                    view3.getLayoutParams().width = getScreenWidth();
//                }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
//            Log.d(TAG, "onScrollStateChanged: ");

/*            int visibleItem = layoutManager.findFirstVisibleItemPosition();
            if (visibleItem > -1) {
                View child = layoutManager.getChildAt(visibleItem);
                if (child != null) {
                    child.setLayoutParams(new RecyclerView.LayoutParams(100, 100));
                }
            }

            layoutManager.requestLayout();*/

//            layoutManager.requestLayout();

        }

        @Override
        protected void onBindView(RBaseViewHolder holder, int position, ScrollerBean bean) {
            Log.i(TAG, "onBindView: " + position);
            TextView textView = ((TextView) holder.itemView);
            textView.setText(position + "");
//            textView.setScaleX(1);
//            textView.setScaleY(1);
            if (position % 6 == 0) {
                textView.setBackgroundColor(Color.GRAY);
            } else if (position % 5 == 0) {
                textView.setBackgroundColor(Color.BLUE);
            } else if (position % 4 == 0) {
                textView.setBackgroundColor(Color.CYAN);
//                textView.animate().scaleX(2).scaleY(2).setDuration(300).start();
            } else if (position % 3 == 0) {
                textView.setBackgroundColor(Color.DKGRAY);
            } else if (position % 2 == 0) {
                textView.setBackgroundColor(Color.MAGENTA);
//                textView.setScaleX(2);
//                textView.setScaleY(2);
            } else {
                textView.setBackgroundColor(Color.LTGRAY);
            }

//            int visibleItem = layoutManager.findFirstVisibleItemPosition();
//            if (visibleItem > -1) {
//                for (int i = visibleItem; i < layoutManager.getChildCount(); i++) {
//                    View child = layoutManager.findViewByPosition(i);
//                    final int top = child.getTop();
//                    final int bottom = child.getBottom();
//
//                    Log.i(TAG, "createContentView: " + top + "  " + bottom);
//                    child.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (top * 0.75)));
//                }
//            }

//            try {
//                Log.i(TAG, "createContentView: " + textView.getTop() + "  " + textView.getBottom());
//                Log.i(TAG, "createContentView: " + layoutManager.getChildAt(position).getTop() + "  " + layoutManager.getChildAt(position).getBottom());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            textView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
        }
    }

    protected class ScrollerBean {

    }

    protected class ScrollerLayoutManager extends LinearLayoutManager {

        public ScrollerLayoutManager(Context context) {
            super(context);
        }

//        @Override
//        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
////            super.onMeasure(recycler, state, widthSpec, heightSpec);
//            Log.i(TAG, "onMeasure: childCount:" + getChildCount() + "  itemCount:" + getItemCount() +
//                    "  CV:" + findFirstCompletelyVisibleItemPosition() + " V:" + findFirstVisibleItemPosition());
//
//         /*   int visibleItem = findFirstVisibleItemPosition();
//            if (visibleItem > -1) {
//                View child = recycler.getViewForPosition(findFirstVisibleItemPosition());
//                child.setLayoutParams(new RecyclerView.LayoutParams(100, 100));
//            }*/
//            setMeasuredDimension(800, 1600);
//        }
//
//        @Override
//        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//       /*     int visibleItem = findFirstVisibleItemPosition();
//            if (visibleItem > -1) {
//                View child = recycler.getViewForPosition(findFirstVisibleItemPosition());
//                child.setLayoutParams(new RecyclerView.LayoutParams(100, 100));
//            }*/
//
//            super.onLayoutChildren(recycler, state);
//            Log.w(TAG, "onLayoutChildren: ");
//        }


        @Override
        public RecyclerView.LayoutParams generateDefaultLayoutParams() {
            Log.w(TAG, "generateDefaultLayoutParams: ........");
//            return super.generateDefaultLayoutParams();
            return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        }

//        @Override
//        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//            Log.i(TAG, "onLayoutChildren: ");
//            int visibleItem = findFirstVisibleItemPosition();
//            if (visibleItem > -1) {
//                View child = recycler.getViewForPosition(visibleItem);
//                child.measure(View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.EXACTLY),
//                        View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.EXACTLY));
//
//            }
//            super.onLayoutChildren(recycler, state);
//        }
    }

    class TestLayoutManager extends RecyclerView.LayoutManager {

        @Override
        public RecyclerView.LayoutParams generateDefaultLayoutParams() {
            return null;
        }
    }
}
