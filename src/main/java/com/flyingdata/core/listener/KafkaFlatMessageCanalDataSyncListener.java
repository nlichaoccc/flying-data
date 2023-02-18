package com.flyingdata.core.listener;

import com.alibaba.otter.canal.client.kafka.KafkaCanalConnector;
import com.alibaba.otter.canal.protocol.FlatMessage;
import com.flyingdata.core.config.CanalConsumeProperties;
import com.flyingdata.core.config.FlyingDataSyncProperties;
import com.flyingdata.core.context.DataSyncContext;
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

    private CanalConsumeProperties consumeProperties;

    @Override
    public void init(FlyingDataSyncProperties properties) {
        super.init(properties);
        consumeProperties = properties.getConsume();
    }


    @Override
    protected void connect() {
        logger.info("CanalKafkaDataSyncListener connect begin.");
        connector = new KafkaCanalConnector(consumeProperties.getKafkaServers(), consumeProperties.getDestination(), consumeProperties.getPartition(), consumeProperties.getGroupId(), consumeProperties.getBatchSize(), true);
        connector.connect();
        connector.subscribe();
        logger.info("CanalKafkaDataSyncListener connect end.");
    }

    @Override
    protected DataSyncContext request() {

        DataSyncContext dataSyncContext = new DataSyncContext();

        List<FlatMessage> flatMessages = connector.getFlatListWithoutAck(consumeProperties.getTimeoutMillis(), TimeUnit.MILLISECONDS);
        dataSyncContext.setRdbMessages(MessageUtil.flatMessage2RdbMessage(consumeProperties.getDestination(), consumeProperties.getGroupId(), flatMessages));

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
