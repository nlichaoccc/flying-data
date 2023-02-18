package com.flyingdata.core.sync;

import com.flyingdata.core.config.FlyingDataSyncProperties;
import com.flyingdata.core.listener.DataSyncListener;
import com.flyingdata.core.listener.KafkaFlatMessageCanalDataSyncListener;
import com.flyingdata.core.listener.SimpleTcpCanalDataSyncListener;
import com.flyingdata.core.utils.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public class DataSynchronizer {

    protected final static Logger logger = LoggerFactory.getLogger(DataSynchronizer.class);

    private ExecutorService executor;

    private FlyingDataSyncProperties properties;

    private DataSyncListener listener;

    public DataSynchronizer(FlyingDataSyncProperties properties) {
        this.properties = properties;
    }


    public void start() {
        if (executor == null) {
            // 设置默认线程池
            executor = DataSyncExecutor.executor;
        }

        DataSyncListener syncListener;

        String subscribeType = properties.getSubscribeType();
        if ("tcp".equalsIgnoreCase(subscribeType)) {
            syncListener = new SimpleTcpCanalDataSyncListener();
        } else if ("kafka".equalsIgnoreCase(subscribeType)) {
            syncListener = new KafkaFlatMessageCanalDataSyncListener();
        } else {
            syncListener = ObjectUtil.newNoArgInstance(properties.getListener());
        }
        this.listener = syncListener;

        syncListener.init(properties);

        // 提交监听任务
        Future<?> submit = executor.submit(syncListener);

        // 监听线程优雅关闭
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            String listenerName = listener.getClass().getSimpleName();
            try {
                listener.stop();
                // 等待方法执行结束，且最多等待30s
                submit.get(30, TimeUnit.SECONDS);
            } catch (Throwable e) {
                logger.warn("something goes wrong when stopping flying data listener ({}) :", listenerName, e);
            } finally {
                logger.info("flying data listener ({}) is down.\n", listenerName);
            }
        }));
    }

}
