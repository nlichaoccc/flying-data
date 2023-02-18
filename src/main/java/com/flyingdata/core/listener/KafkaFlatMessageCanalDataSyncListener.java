package com.flyingdata.core.listener;

import com.alibaba.otter.canal.client.kafka.KafkaCanalConnector;
import com.alibaba.otter.canal.protocol.FlatMessage;
import com.flyingdata.core.config.CanalConnectProperties;
import com.flyingdata.core.config.FlyingDataSyncProperties;
import com.flyingdata.core.entry.DataSyncContext;
import com.flyingdata.core.utils.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/14
 */
public class KafkaFlatMessageCanalDataSyncListener extends AbstractCanalDataSyncListener {

    protected final static Logger logger = LoggerFactory.getLogger(KafkaFlatMessageCanalDataSyncListener.class);

    private KafkaCanalConnector connector;

    private CanalConnectProperties connectProperties;

//    private String  topic     = "user";
//    private Integer partition = 0;
//    private String  groupId   = "group";
//    private String  servers   = "ka-host.inlcc.cn:9092";
//    private String  zkServers = "ka-host.inlcc.cn:2181";
//    private Integer batchSize = null;
//    private long timeoutMillis = 100L;


    @Override
    public void init(FlyingDataSyncProperties properties) {
        super.init(properties);
        connectProperties = properties.getConnect();
    }


    @Override
    protected void connect() {
        logger.info("CanalKafkaDataSyncListener connect begin.");
        connector = new KafkaCanalConnector(connectProperties.getKafkaServers(), connectProperties.getDestination(), connectProperties.getPartition(), connectProperties.getGroupId(), connectProperties.getBatchSize(), true);
        connector.connect();
        connector.subscribe();
        logger.info("CanalKafkaDataSyncListener connect end.");
    }

    @Override
    protected DataSyncContext request() {

        DataSyncContext dataSyncContext = new DataSyncContext();

//        List<Message> list = connector.getList(timeoutMillis, TimeUnit.MILLISECONDS);
//        List<RdbMessage> rdbMessages = new ArrayList<>();
//        list.forEach(message -> rdbMessages.addAll(MessageUtil.message2RdbMessage(topic, groupId, message)));
//        dataSyncEntry.setRdbMessages(rdbMessages);

        List<FlatMessage> flatMessages = connector.getFlatList(connectProperties.getTimeoutMillis(), TimeUnit.MILLISECONDS);
        dataSyncContext.setRdbMessages(MessageUtil.flatMessage2RdbMessage(connectProperties.getDestination(), connectProperties.getGroupId(), flatMessages));

        return dataSyncContext;
    }

    @Override
    protected void ack(DataSyncContext context) {
        connector.ack();
    }

    @Override
    protected void rollback() {
        connector.rollback();
    }

    @Override
    protected void disconnect() {
        connector.disconnect();
    }
}
