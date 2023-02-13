package com.inlcc.flyingdata.core;


import com.inlcc.flyingdata.core.listener.CanalTcpDataSyncListener;
import com.inlcc.flyingdata.core.sync.DataSynchronizer;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
class SyncTest {

    public static void main(String[] args) throws InterruptedException {
        DataSynchronizer synchronizer = DataSynchronizer.builder(new CanalTcpDataSyncListener())
                .build();
        synchronizer.start();

        Thread.sleep(10000000l);
    }

}