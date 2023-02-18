package com.flyingdata.core.sync;

import com.flyingdata.core.config.FlyingDataSyncProperties;
import com.flyingdata.core.listener.DataSyncListener;
import com.flyingdata.core.listener.KafkaFlatMessageCanalDataSyncListener;
import com.flyingdata.core.listener.SimpleTcpCanalDataSyncListener;
import com.flyingdata.core.utils.ObjectUtil;

import java.util.concurrent.ExecutorService;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public class DataSynchronizer {

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
        executor.submit(syncListener);
    }

    public void stop() {
        listener.stop();
    }


}
