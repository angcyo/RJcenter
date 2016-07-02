package com.angcyo.sample.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.base.RBaseFragment;
import com.rsen.github.common.L;
import com.rsen.util.T;

public class FragmentLifeCycleActivity extends RBaseActivity {


    TestFragment oneFragment;
    TestFragment twoFragment;

    @Override
    protected int getContentView() {
        return R.layout.activity_fragment_life_cycle;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mViewHolder.v(R.id.add).setOnClickListener(v -> addFragment());
        mViewHolder.v(R.id.show).setOnClickListener(v -> showFragment(String.valueOf(oneFragment.hashCode())));
        mViewHolder.v(R.id.hide).setOnClickListener(v -> hideFragment(oneFragment));
        mViewHolder.v(R.id.replace).setOnClickListener(v -> {
            replaceFragment(R.id.fragment_container, createFragment("Hello Replace Fragment"));
        });
        mViewHolder.v(R.id.scroll).setOnClickListener(v -> twoFragment.scroll());
        mViewHolder.v(R.id.remove).setOnClickListener(v -> removeFragment(twoFragment));
    }

    private void addFragment() {
        if (oneFragment == null) {
            oneFragment = (TestFragment) createFragment("One");
            addFragment(R.id.fragment_container, oneFragment);
        } else if (twoFragment == null) {
            twoFragment = (TestFragment) createFragment("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
            addFragment(R.id.fragment_container, twoFragment);
        } else {
            addFragment(R.id.fragment_container, new TestFragment());
        }
    }

    private RBaseFragment createFragment(String text) {
        TestFragment testFragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString("text", text);
        testFragment.setArguments(bundle);
        return testFragment;
    }

    public static class TestFragment extends RBaseFragment {

        public TestFragment() {
        }

        @Override
        public void onAttachFragment(Fragment childFragment) {
            super.onAttachFragment(childFragment);
            L.i("onAttachFragment");
        }

        @Override
        public void onAttach(Activity activity) {
            L.i("onAttach: Activity");
            super.onAttach(activity);
        }

        @Override
        public void onAttach(Context context) {
            L.i("onAttach: Context");
            super.onAttach(context);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            L.i("onCreate: ");
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            L.i("onCreateView: ");
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onStart() {
            super.onStart();
            L.i("onStart: ");
        }

        @Override
        public void onResume() {
            super.onResume();
            L.i("onResume: ");
        }

        @Override
        public void onPause() {
            super.onPause();
            L.i("onPause: ");
        }

        @Override
        public void onStop() {
            super.onStop();
            L.i("onStop: ");
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            L.i("onViewCreated: ");
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            L.i("onActivityCreated: ");
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            L.i("onDestroy: ");
        }

        @Override
        protected void onShow() {
            super.onShow();
            L.i("onShow: ");
        }

        @Override
        protected void onHide() {
            super.onHide();
            L.i("onHide: ");
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            L.i("setUserVisibleHint: " + isVisibleToUser);
        }

        @Override
        public void onHiddenChanged(boolean hidden) {
            super.onHiddenChanged(hidden);
            L.i("onHiddenChanged: " + hidden);
        }

        @Override
        public void onDetach() {
            super.onDetach();
            L.i("onDetach: ");
        }

        @Override
        protected int getContentView() {
            return R.layout.fragment_life_cycle_test;
        }

        @Override
        protected void initViewData() {
            final Bundle arguments = getArguments();
            if (arguments != null) {
                mViewHolder.tV(R.id.textView).setText(arguments.getString("text"));
            }
        }

        public void scroll() {
            final View view = getView();
            view.animate().translationX(200).setDuration(1000).start();
        }

        @Override
        protected void initView(View rootView) {
            mViewHolder.tV(R.id.textView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    T.show(mBaseActivity, mViewHolder.tV(R.id.textView).getText());
                }
            });
        }
    }
}
