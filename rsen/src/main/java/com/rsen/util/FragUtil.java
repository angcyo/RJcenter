package com.rsen.util;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.rsen.base.RBaseFragment;

/**
 * Created by angcyo on 15-12-25 025 13:45 下午.
 */
public class FragUtil {

    public static void addFragment(FragmentActivity activity, RBaseFragment fragment, @IdRes int resId) {
        addFragment(activity, resId, fragment, true);
    }

    public static void addFragment(FragmentActivity activity, RBaseFragment fragment, @IdRes int resId, boolean toBackStack) {
        addFragment(activity, resId, fragment, toBackStack);
    }

    public static void addFragment(FragmentActivity activity, @IdRes int resId, RBaseFragment fragment, boolean toBackStack) {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations();//添加动画
        String tag = fragment.getClass().getSimpleName();
        fragmentTransaction.add(resId, fragment, tag);
        if (toBackStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }

}
