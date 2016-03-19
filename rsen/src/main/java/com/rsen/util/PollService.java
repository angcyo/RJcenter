package com.rsen.util;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 轮询服务
 * <p/>
 * Created by angcyo on 15-12-29 029 09:52 上午.
 */
public class PollService extends Thread {
    /**
     * The constant HZ.
     */
    public static final int HZ = 1000;//避免误差, 取值1秒
    private static PollService instance;

    private Vector<PollTask> tasks;
    private long count = 0;

    private ExecutorService executorService;

    private boolean isQuit = false;

    private PollService() {
        executorService = Executors.newCachedThreadPool();
        tasks = new Vector<PollTask>();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static PollService getInstance() {
        if (instance == null) {
            synchronized (PollService.class) {
                if (instance == null) {
                    PollService poll = new PollService();
                    poll.start();
                    return poll;
                }
            }
        }

        return instance;
    }

    /**
     * Exit.
     */
    public static void exit() {
        if (instance != null) {
            instance.quit();
        }
    }

    /**
     * 创建一个默认每秒都执行的任务
     *
     * @param task the task
     */
    public synchronized void addTask(final Runnable task) {
        long id = count++;
        long time = 1;
        PollTask pollTask = new PollTask(id, time, null, null) {
            @Override
            public void run() {
                task.run();
            }
        };

        addTask(pollTask);
    }

    /**
     * Add task.
     *
     * @param task the task
     */
    public synchronized void addTask(PollTask task) {
        for (PollTask polltask : tasks) {
            if (polltask.getId() == task.getId()) {
                polltask.setTime(task.time);
                return;
            }
        }

        tasks.add(task);
    }

    /**
     * Cancel task.
     *
     * @param id the id
     */
    public synchronized void cancelTask(long id) {
        for (PollTask polltask : tasks) {
            if (polltask.getId() == id) {
                tasks.remove(polltask);
            }
        }
    }

    /**
     * Cancel task.
     *
     * @param task the task
     */
    public synchronized void cancelTask(PollTask task) {
        cancelTask(task.getId());
    }

    /**
     * Exist task boolean.
     *
     * @param task the task
     * @return the boolean
     */
    public synchronized boolean existTask(PollTask task) {
        for (PollTask polltask : tasks) {
            if (polltask.getId() == task.getId()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Quit.
     */
    public synchronized void quit() {
        isQuit = true;
        tasks.removeAllElements();
        tasks = null;
        executorService.shutdown();
        executorService = null;
        instance = null;
    }

    @Override
    public void run() {
        super.run();
        while (!isQuit) {
            for (PollTask polltask : tasks) {
                if (isQuit) {
                    break;
                }
                long nowTime = System.currentTimeMillis() / 1000;
                if (nowTime % polltask.getTime() == 0) {
                    executorService.execute(polltask);
                }
            }

            try {
                Thread.sleep(HZ);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The type Poll task.
     */
    public static abstract class PollTask implements Runnable {

        private long id;//任务id
        private String tag;
        private Object data;//任务附加数据
        private long time;//任务轮询时间间隔, 单位秒;

        /**
         * Instantiates a new Poll task.
         *
         * @param id   the id
         * @param time the time
         * @param tag  the tag
         * @param data the data
         */
        public PollTask(long id, long time, String tag, Object data) {
            this.id = id;
            this.time = time;
            this.tag = tag;
            this.data = data;
        }

        /**
         * Gets id.
         *
         * @return the id
         */
        public long getId() {
            return id;
        }

        /**
         * Gets time.
         *
         * @return the time
         */
        public long getTime() {
            return time;
        }

        /**
         * Sets time.
         *
         * @param time the time
         */
        public void setTime(long time) {
            this.time = time;
        }

        @Override
        public boolean equals(Object o) {
            return ((PollTask) o).getId() == this.getId();
        }

        @Override
        public abstract void run();
    }
}
