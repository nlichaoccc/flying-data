package com.flyingdata.core;


import com.flyingdata.core.config.CanalConsumeProperties;
import com.flyingdata.core.config.FlyingDataSyncProperties;
import com.flyingdata.core.handler.DefaultDataSyncHandler;
import com.flyingdata.core.sync.DataSynchronizer;

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

        syncProperties.setHandler(DefaultDataSyncHandler.class);

        DataSynchronizer synchronizer = new DataSynchronizer(syncProperties);

        synchronizer.start();


        // 最多执行1分钟，防止忘记关闭进程
        try {
            Thread.sleep(60000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.exit(0);
    }

}