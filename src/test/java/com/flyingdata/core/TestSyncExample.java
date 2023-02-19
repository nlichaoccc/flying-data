package com.flyingdata.core;


import com.flyingdata.core.config.CanalConsumeProperties;
import com.flyingdata.core.config.FlyingDataSyncProperties;
import com.flyingdata.core.config.StorageProperties;
import com.flyingdata.core.handler.DefaultDataSyncHandler;
import com.flyingdata.core.storage.Es7xDataSyncResultStorage;
import com.flyingdata.core.sync.DataSynchronizer;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public class TestSyncExample {

    @Test
    public void testSync() {

        FlyingDataSyncProperties syncProperties = new FlyingDataSyncProperties();

        // 数据监听方式，目前仅支持 CanalTCP 和 Canal to kafka方式（kafka需要在canal-server设置flatMessage = true）
        syncProperties.setSubscribeType("kafka");

        CanalConsumeProperties canalConsumeProperties = new CanalConsumeProperties();
//        canalConsumeProperties.setHost("localhost");
//        canalConsumeProperties.setPort(11111);
        canalConsumeProperties.setDestination("user");
        canalConsumeProperties.setPartition(0);
        canalConsumeProperties.setGroupId("g1");
        canalConsumeProperties.setKafkaServers("localhost:9092");
        syncProperties.setConsume(canalConsumeProperties);

        // 数据处理，可自定义
        syncProperties.setHandler(DefaultDataSyncHandler.class);

        // 结果存储，可自定义
        // 同步至es7.x es8.x
        syncProperties.setStorage(Es7xDataSyncResultStorage.class);

        StorageProperties storageProperties = new StorageProperties();
        storageProperties.setHost("localhost");
        storageProperties.setPort(9200);
        storageProperties.setUsername("elastic");
        storageProperties.setPassword("");
        storageProperties.setIndexName("");
        syncProperties.setStorageProperties(storageProperties);


        // 创建同步器
        DataSynchronizer synchronizer = new DataSynchronizer(syncProperties);

        // 启动同步器
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