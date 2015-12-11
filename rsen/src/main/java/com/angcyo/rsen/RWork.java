package com.angcyo.rsen;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by angcyo on 15-12-11 011 09:58 上午.
 */
public class RWork {
    private static ExecutorService service = null;

    static {
        service = Executors.newCachedThreadPool();
    }

    /**
     * 调用此方法,运行在后台线程
     */
    public static void work(Runnable task) {
        service.execute(task);
    }

    /**
     * 调用此方法,可以构建后台任务和主线程任务
     */
    public static Builder on() {
        return new Builder();
    }

    /**
     * 退出调用
     */
    public static void quit() {
        service.shutdown();
    }

    public static class Builder {
        private Task bgTask, uiTask;

        /**
         * 主线程回调
         */
        public Builder onUi(Task uiTask) {
            this.uiTask = uiTask;
            return this;
        }

        /**
         * 后台线程回调
         */
        public Builder onBg(Task bgTask) {
            this.bgTask = bgTask;
            return this;
        }

        /**
         * 必须调用此方法,才会执行
         */
        public void work() {
            RWork.work(new WorkTask(bgTask, uiTask));
        }
    }

    /**
     * 封装的类
     */
    private static class WorkTask implements Runnable {
        Task uiTask, bgTask;
        Handler handler;

        public WorkTask(Task bgTask, Task uiTask) {
            this.uiTask = uiTask;
            this.bgTask = bgTask;
            handler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void run() {
            Object result = null;
            if (bgTask != null) {
                result = bgTask.onTask(null);
            }
            if (uiTask != null) {
                handler.post(new UiTask(result, uiTask));
            }
        }
    }

    /**
     * 封装的类,用于参数传递
     */
    public static abstract class Task implements Runnable {

        public abstract Object onTask(Object object);

        @Override
        public void run() {
        }
    }

    private static class UiTask implements Runnable {
        Object arg;
        Task task;

        public UiTask(Object arg, Task task) {
            this.arg = arg;
            this.task = task;
        }

        @Override
        public void run() {
            task.onTask(arg);
        }
    }

}
