package com.angcyo.sample.ViewPagerDemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.angcyo.sample.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerGridViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_grid_view);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(new GridViewAdapter(this));
    }

    public static class GridViewAdapter extends PagerAdapter {

        List<View> views;
        Context context;

        public GridViewAdapter(Context context) {
            this.context = context;

            views = new ArrayList<>();
            for (int i = 0; i < getCount(); i++) {
                RelativeLayout relativeLayout = new RelativeLayout(context);
                GridView gridView = new GridView(context);

                relativeLayout.addView(gridView, new RelativeLayout.LayoutParams(-1,-1));

                gridView.setNumColumns(2);//固定2列，任意修改界面会错乱；
                gridView.setAdapter(new GridItemAdapter(context));

                relativeLayout.setBackgroundColor(Color.YELLOW);
                views.add(relativeLayout);
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View gridView = views.get(position);

            container.addView(gridView);
//            container.setBackgroundColor(Color.RED);
            return gridView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }

    public static class GridItemAdapter extends BaseAdapter{

        List<ImageView> imageViewList;
        Context context;

        public GridItemAdapter(Context context) {
            this.context = context;

            imageViewList = new ArrayList<>();
            for (int i = 0; i < getCount(); i++) {
                ImageView imageView = new ImageView(context);
                imageView.setBackgroundResource(R.mipmap.ic_launcher);
//                imageView.setBackgroundColor(Color.YELLOW);
                imageViewList.add(imageView);
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = imageViewList.get(position);
            return imageView;
        }
    }
}
