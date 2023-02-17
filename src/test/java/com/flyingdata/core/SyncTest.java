package com.flyingdata.core;


import com.flyingdata.core.listener.KafkaFlatMessageCanalDataSyncListener;
import com.flyingdata.core.handler.PrintDataSyncHandler;
import com.flyingdata.core.sync.DataSynchronizer;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
class SyncTest {

    public static void main(String[] args) throws InterruptedException {
        DataSynchronizer synchronizer = DataSynchronizer.builder(new KafkaFlatMessageCanalDataSyncListener())
                .addHandler(new PrintDataSyncHandler())
                .build();

        synchronizer.start();

        Thread.sleep(300000L);
    }

}