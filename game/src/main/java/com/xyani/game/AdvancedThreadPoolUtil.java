package com.xyani.game;

/**
 * User: 1241734684@qq.com
 * Description:线程池
 * Date:2023-09-12 11
 * Time:25
 */
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AdvancedThreadPoolUtil {

    private static ThreadPoolExecutor threadPoolExecutor;

    public static void initThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                      BlockingQueue<Runnable> workQueue) {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    // 默认配置线程池，核心线程数为 CPU 核心数，最大线程数为 2 * CPU 核心数，无限等待队列
    public static void initDefaultThreadPool() {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        int maximumPoolSize = corePoolSize * 2;
        long keepAliveTime = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        initThreadPool(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public static void executeTask(Runnable task) {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.execute(task);
        }
    }

    public static void shutdownThreadPool() {
        if (threadPoolExecutor != null && !threadPoolExecutor.isShutdown()) {
            threadPoolExecutor.shutdown();
        }
    }

    public static void cancelAllTasks() {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdownNow();
        }
    }
}

