package com.angcyo.demo.logback;

import android.os.Bundle;

import com.angcyo.sample.R;
import com.lib.common.RLog;
import com.rsen.base.RBaseActivity;

public class LogbackDemoActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_logback_demo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        RLog log = RLog.getLog(LogbackDemoActivity.class);
        log.trace("trace {}", true);
        log.debug("debug {}", true);
        log.info("info {}", true);
        log.warn("warn {}", true);
        log.error("error {}", true);

        RLog logtest = RLog.getLog("logtest");
        logtest.trace("trace {}", true);
        logtest.debug("debug {}", true);
        logtest.info("info {}", true);
        logtest.warn("warn {}", true);
        logtest.error("error {}", true);
    }
}
