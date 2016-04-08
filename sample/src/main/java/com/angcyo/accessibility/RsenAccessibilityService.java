package com.angcyo.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

public class RsenAccessibilityService extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        // setServiceInfo();//这个方法同样可以实现xml中的配置信息
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //关闭服务时,调用
        return super.onUnbind(intent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //当窗口发生的事件是我们配置监听的事件时,会回调此方法.会被调用多次
    }

    @Override
    public void onInterrupt() {
        //当服务要被中断时调用.会被调用多次
    }
}
