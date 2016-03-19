package com.angcyo.rsen_qq;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    int activityHeight;
    int navBarHeight;
    View navBar;
    View centerButton, leftButton, rightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        centerButton = findViewById(R.id.center_bt);
        leftButton = findViewById(R.id.left_bt);
        rightButton = findViewById(R.id.right_bt);
        navBar = findViewById(R.id.nav_bar);
        navBar.post(new Runnable() {
            @Override
            public void run() {
                navBarHeight = navBar.getHeight();

                initView();
            }
        });
        initDisplay();
    }

    private void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new RBaseAdapter<ItemBean>(this, fillBean()) {

            @Override
            protected int getItemLayoutId(int viewType) {
                return R.layout.adapter_main_item;
            }

            @Override
            protected void onBindView(RBaseViewHolder holder, int position, ItemBean bean) {
                holder.v(R.id.item_layout).setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        activityHeight / 4 - navBarHeight / 4));
                holder.imgV(R.id.ico).setImageResource(bean.ico);
                holder.tV(R.id.text).setText(bean.title);//
            }
        });

        centerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(CenterFragment.newInstance());
            }
        });
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(leftButton, "首页", Snackbar.LENGTH_SHORT).show();
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(RightFragment.newInstance());
            }
        });
    }

    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String simpleName = fragment.getClass().getSimpleName();
        Fragment byTag = fragmentManager.findFragmentByTag(simpleName);
        if (byTag != null) {
            Snackbar.make(navBar, "已经添加了", Snackbar.LENGTH_SHORT).show();
            return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contain, fragment, simpleName);
        fragmentTransaction.addToBackStack(simpleName);
        fragmentTransaction.commit();
    }

    private ArrayList<ItemBean> fillBean() {
        ArrayList<ItemBean> beans = new ArrayList<>();
        beans.add(new ItemBean("积分卡"));
        beans.add(new ItemBean("积分查询"));
        beans.add(new ItemBean("钱包"));
        beans.add(new ItemBean("账单"));
        beans.add(new ItemBean("我的订单"));
        beans.add(new ItemBean("意见反驳"));
        return beans;
    }

    private void initDisplay() {
        int heightPixels = getResources().getDisplayMetrics().heightPixels;
        activityHeight = heightPixels;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class ItemBean {
        public String title;
        @DrawableRes
        public int ico;

        public ItemBean(String title) {
            this.title = title;
            this.ico = R.mipmap.ic_launcher;
        }

        public ItemBean(String title, int ico) {
            this.title = title;
            this.ico = ico;
        }
    }

    /*Fragment*/
    public static class CenterFragment extends Fragment {
        public static CenterFragment newInstance() {
            CenterFragment centerFragment = new CenterFragment();
            return centerFragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_center, container, false);
            return rootView;
        }
    }

    /*求卡*/
    public static class RightFragment extends Fragment {
        public static RightFragment newInstance() {
            RightFragment rightFragment = new RightFragment();
            return rightFragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_right, container, false);
            initView(rootView);
            return rootView;
        }

        private void initView(View rootView) {
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(new RBaseAdapter<ItemBean>(getActivity(), fillBean(20)) {

                @Override
                protected int getItemLayoutId(int viewType) {
                    return R.layout.adapter_fragment_item;
                }

                @Override
                protected void onBindView(RBaseViewHolder holder, int position, ItemBean bean) {
                }
            });
        }

        private ArrayList<ItemBean> fillBean(int count) {
            ArrayList<ItemBean> beans = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                beans.add(new ItemBean());
            }
            return beans;
        }

        public static class ItemBean {

        }
    }
}
