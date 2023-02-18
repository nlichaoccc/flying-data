package com.flyingdata.core;


import com.flyingdata.core.config.CanalConnectProperties;
import com.flyingdata.core.config.FlyingDataSyncProperties;
import com.flyingdata.core.handler.PrintJsonDataSyncHandler;
import com.flyingdata.core.sync.DataSynchronizer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
class SyncTest {

    public static void main(String[] args) throws InterruptedException {

        FlyingDataSyncProperties syncProperties = new FlyingDataSyncProperties();

        syncProperties.setSubscribeType("kafka");

        CanalConnectProperties canalConnectProperties = new CanalConnectProperties();
        canalConnectProperties.setHost("hk-host.inlcc.cn");
        canalConnectProperties.setPort(11111);
        canalConnectProperties.setDestination("user");
        canalConnectProperties.setPartition(0);
        canalConnectProperties.setGroupId("g1");
        canalConnectProperties.setKafkaServers("ka-host.inlcc.cn:9092");
        syncProperties.setConnect(canalConnectProperties);

        syncProperties.setHandlers(new LinkedList<>());
        syncProperties.getHandlers().add(PrintJsonDataSyncHandler.class);

        DataSynchronizer synchronizer = new DataSynchronizer(syncProperties);

        synchronizer.start();

        Thread.sleep(300000L);
    }

}