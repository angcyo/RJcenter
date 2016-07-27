package com.angcyo.demo.logback;

import android.os.Bundle;
import android.view.View;

import com.angcyo.sample.R;
import com.lib.common.RLog;
import com.rsen.base.RBaseActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hugo.weaving.DebugLog;

public class LogbackDemoActivity extends RBaseActivity {

    @DebugLog
    @Override
    protected int getContentView() {
        return R.layout.activity_logback_demo;
    }

    @DebugLog
    @Override
    protected void initView(Bundle savedInstanceState) {
        mViewHolder.v("textView").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger logger = LoggerFactory.getLogger("");
            }
        });
    }

    @DebugLog
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
