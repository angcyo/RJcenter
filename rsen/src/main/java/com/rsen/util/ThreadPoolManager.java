package com.rsen.util;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ThreadPoolManager {


    private ThreadPoolExecutor threadPool;//线程池
    private Map<String, List<WeakReference<Future<?>>>> taskMap; //可以控制任务生命周期的任务队列
    private String className;
    private static ThreadPoolManager instance = null;

    /**
     * 创建一个新的实例 ThreadPoolManager.
     *
     * @param className 包含包名的类的全名称
     */
    public ThreadPoolManager(String className) {
        this.className = className;
        threadPool = (ThreadPoolExecutor) Executors.newScheduledThreadPool(20);
        taskMap = new WeakHashMap<String, List<WeakReference<Future<?>>>>();
    }

    /**
     * 创建一个新的实例 ThreadPoolManager. 如果poolSize >0 创建在给定的延迟后运行或定期执行任务 的线程池
     * @param className 包含包名的类的全名称
     * @param poolSize  线程池的大小 如果大于零 
     */
    public ThreadPoolManager(String className, int poolSize){
    	this.className = className;
    	if (poolSize <= 0) {
    		threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		}else{
			threadPool = (ThreadPoolExecutor) Executors.newScheduledThreadPool(poolSize);
		}
    	taskMap = new WeakHashMap<String, List<WeakReference<Future<?>>>>();
    }

    /**
     * 获取ThreadPoolManager单例对象
     *
     * @param className 上下文
     * @return
     * @author caobuqing
     * @date 2014-1-11
     */
    public static ThreadPoolManager getInstance(String className) {
        if (instance == null) {
            synchronized (ThreadPoolManager.class) {
                if (instance == null) {
                    instance = new ThreadPoolManager(className);
                }
            }
        }
        instance.className = className;
        return instance;
    }

    /**
     * 释放ThreadPoolManager单例对象
     * void
     *
     * @throws
     * @since 1.0.0
     */
    public static void release() {
        synchronized (ThreadPoolManager.class) {
            if (instance != null) {
                instance.cancelAllTaskThreads();
            }
            instance = null;
        }
    }

    /**
     * 执行在给定延迟后启用的一次性操作
     *
     * @param command 要执行的任务
     * @param delay   从现在开始延迟执行的时间
     * @param unit    延迟参数的时间单位
     *                void
     * @throws
     * @since 1.0.0
     */
    public void schedule(Runnable command, long delay, TimeUnit unit) {
        if (threadPool instanceof ScheduledExecutorService) {
            Future<?> request = ((ScheduledExecutorService) threadPool).schedule(command, delay, unit);
            addTask(request);
        }
    }

    /**
     * 执行一个在给定初始延迟后首次启用的定期操作，后续操作具有给定的周期
     *
     * @param command      要执行的任务
     * @param initialDelay 首次执行的延迟时间
     * @param period       连续执行之间的周期
     * @param unit         参数的时间单位
     *                     void
     * @throws
     * @since 1.0.0
     */
    public void scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        if (threadPool instanceof ScheduledExecutorService) {
            Future<?> request = ((ScheduledExecutorService) threadPool).scheduleAtFixedRate(command, initialDelay, period, unit);
            addTask(request);
        }
    }

    /**
     * 开启线程
     *
     * @param runnable
     * @throws
     * @since 1.0.0
     */
    public void startTaskThread(Runnable runnable) {
        Future<?> request = threadPool.submit(runnable);
        addTask(request);
    }

    /**
     * 添加执行任务到队列中
     *
     * @param request void
     * @throws
     * @since 1.0.0
     */
    private void addTask(Future<?> request) {
        synchronized (ThreadPoolManager.class) {
            if (className != null) {
                // 在请求集中添加本次请求
                List<WeakReference<Future<?>>> requestList = taskMap.get(className);
                if (requestList == null) {
                    requestList = new LinkedList<WeakReference<Future<?>>>();
                    taskMap.put(className, requestList);
                }
                requestList.add(new WeakReference<Future<?>>(request));
            }
        }
    }

    /**
     * 在OnDestroy中结束所有开启的线程
     *
     * @param className           对应的包含包名的类名
     * @param mayInterruptIfRunning 是否关闭正在运行的线程标志
     *                              void
     * @throws
     * @since 1.0.0
     */
    public void cancelTaskThreads(String className,
                                  boolean mayInterruptIfRunning) {
        synchronized (ThreadPoolManager.class) {
            List<WeakReference<Future<?>>> requestList = taskMap
                    .get(className);
            if (requestList != null) {
                for (WeakReference<Future<?>> requestRef : requestList) {
                    Future<?> request = requestRef.get();
                    if (request != null) {
                        request.cancel(mayInterruptIfRunning);
                    }
                }
            }
            taskMap.remove(className);
        }
    }

    /**
     * 取消所有的任务
     * void
     *
     * @throws
     * @since 1.0.0
     */
    private void cancelAllTaskThreads() {
        for (String clsName : taskMap.keySet()) {
            List<WeakReference<Future<?>>> requestList = taskMap.get(clsName);
            if (requestList != null) {
                Iterator<WeakReference<Future<?>>> iterator = requestList.iterator();
                while (iterator.hasNext()) {
                    Future<?> request = iterator.next().get();
                    if (request != null) {
                        request.cancel(true);
                    }

                }
            }
        }
        taskMap.clear();
    }
}
