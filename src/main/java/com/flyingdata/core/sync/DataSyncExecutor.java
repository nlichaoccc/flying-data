package com.flyingdata.core.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public class DataSyncExecutor {
    protected final static Logger logger = LoggerFactory.getLogger(DataSyncExecutor.class);
    protected static final ExecutorService executor;

    protected static Thread.UncaughtExceptionHandler handler = (t, e) -> logger.error("parse events has an error",
            e);

    static {
        executor = new ThreadPoolExecutor(1, Integer.MAX_VALUE,
                10L, TimeUnit.SECONDS,
                new SynchronousQueue<>(), new ThreadFactory() {
            private final AtomicInteger poolNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("flying-data-thread-pool-" + poolNumber.getAndIncrement());
                thread.setUncaughtExceptionHandler(handler);
                return thread;
            }

        });



    }
}
