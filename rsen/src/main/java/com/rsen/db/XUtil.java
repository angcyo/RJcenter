package com.rsen.db;

import android.app.Application;

/**
 * Created by wyouflf on 15/6/10.
 * 任务控制中心, http, image, db, view注入等接口的入口.
 * 需要在在application的onCreate中初始化: XUtil.Ext.init(this);
 */
public final class XUtil {

    private XUtil() {
    }

    public static boolean isDebug() {
        return Ext.debug;
    }


    public static DbManager getDb(DbManager.DaoConfig daoConfig) {
        return DbManagerImpl.getInstance(daoConfig);
    }

    public static Application app() {
        if (Ext.app == null) {
            throw new RuntimeException("please invoke XUtil.Ext.init(app) on Application#onCreate()"
                    + " and register your Application in manifest.");
        }
        return Ext.app;
    }

    public static class Ext {
        private static boolean debug;
        private static Application app;

        private Ext() {
        }

        public static void init(Application app) {
            if (Ext.app == null) {
                Ext.app = app;
            }
        }

        public static void setDebug(boolean debug) {
            Ext.debug = debug;
        }
    }
}
