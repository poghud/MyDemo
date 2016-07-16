package com.lle.mydemo.manager;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {

    private ThreadPoolProxy mShortPool;
    private ThreadPoolProxy mLongPool;

    private ThreadManager(){}
    private static ThreadManager sThreadManager = new ThreadManager();
    public static ThreadManager getThreadManager(){
        return sThreadManager;
    }

    //创建耗时长的线程池
    public ThreadPoolProxy createLongTimePool(){
        if(mLongPool == null) {
            mLongPool = new ThreadPoolProxy(5, 5, 5000L);
        }
        return mLongPool;
    }

    //创建耗时短的线程池
    @SuppressWarnings("unused")
    public ThreadPoolProxy createShortTimePool(){
        if(mShortPool == null) {
            mShortPool = new ThreadPoolProxy(3, 3, 5000L);
        }
        return mShortPool;
    }

    public class ThreadPoolProxy {

        private ThreadPoolExecutor sThreadPool;
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;

        public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime){
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        //执行任务
        public void execute(Runnable runnable) {
            // 创建线程池
        /*
		1. 线程池里面管理多少个线程
		2. 最大线程数
		3. 如果线程池没有要执行的任务 存活多久
		4. 时间的单位
		5  如果 线程池里管理的线程都已经用了,剩下的任务 临时存到LinkedBlockingQueue对象中 排队
		*/
            if (sThreadPool == null) {
                sThreadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                        TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10));
            }
            sThreadPool.execute(runnable);
        }

        //取消任务
        @SuppressWarnings("unused")
        public void cancel(Runnable runnable) {
            if (sThreadPool != null && !sThreadPool.isShutdown() && !sThreadPool.isTerminated()) {
                sThreadPool.remove(runnable);
            }
        }
    }
}
