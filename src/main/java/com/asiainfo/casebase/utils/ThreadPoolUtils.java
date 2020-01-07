package com.asiainfo.casebase.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Desc
 **/
public class ThreadPoolUtils {

    private static volatile ThreadPoolExecutor pool = null;

    private static void init () {
        if(pool == null) {
            synchronized (ThreadPoolUtils.class){
                if (pool == null) {
                    pool = new ThreadPoolExecutor(10,20,60, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(1000));
                    pool.allowCoreThreadTimeOut(true);
                }
            }
        }
    }

    public static void submit(Runnable r) {
        if(pool == null) {
            ThreadPoolUtils.init();
        }
        pool.submit(r);
    }
}
