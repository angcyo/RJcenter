package com.rsen.github.tablayout.listener;

import android.support.annotation.DrawableRes;

public interface CustomTabEntity {
    String getTabTitle();

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();
}
