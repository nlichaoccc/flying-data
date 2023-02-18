package com.flyingdata.core;


import com.flyingdata.core.config.CanalConsumeProperties;
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

    public static void main(String[] args) {

        FlyingDataSyncProperties syncProperties = new FlyingDataSyncProperties();

        syncProperties.setSubscribeType("kafka");

        CanalConsumeProperties canalConsumeProperties = new CanalConsumeProperties();
        canalConsumeProperties.setHost("hk-host.inlcc.cn");
        canalConsumeProperties.setPort(11111);
        canalConsumeProperties.setDestination("user");
        canalConsumeProperties.setPartition(0);
        canalConsumeProperties.setGroupId("g1");
        canalConsumeProperties.setKafkaServers("ka-host.inlcc.cn:9092");
        syncProperties.setConsume(canalConsumeProperties);

        syncProperties.setHandlers(new LinkedList<>());
        syncProperties.getHandlers().add(PrintJsonDataSyncHandler.class);

        DataSynchronizer synchronizer = new DataSynchronizer(syncProperties);

        synchronizer.start();

    }

}