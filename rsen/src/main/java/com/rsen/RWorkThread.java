package com.rsen;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by angcyo on 15-09-12-012.
 */
public class RWorkThread extends Thread {

    private Vector<TaskRunnable> workTask;

    private static ExecutorService service = null;

    static {
        service = Executors.newCachedThreadPool();
    }

    public RWorkThread() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        workTask = new Vector<>();
    }

    public void addTask(TaskRunnable task) {
        workTask.add(task);
        synchronized (RWorkThread.this) {//如果没有正在等待的线程,将不会往下执行
            RWorkThread.this.notify();
        }
    }

    private TaskRunnable getTask() {
        if (workTask.size() > 0) {
            return workTask.remove(0);
        }
        return null;
    }

    @Override
    public void run() {
        TaskRunnable task;
        synchronized (RWorkThread.this) {
            while (!isInterrupted()) {
                task = getTask();//取到任务
                try {
                    if (task == null) {
                        RWorkThread.this.wait();//任务为空,等待添加任务
                    } else {
//                        task.run();//否则执行任务
                        service.execute(task);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static abstract class TaskRunnable implements Runnable {

    }
}
