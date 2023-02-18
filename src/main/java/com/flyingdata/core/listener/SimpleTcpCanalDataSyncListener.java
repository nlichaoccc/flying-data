package com.flyingdata.core.listener;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import com.flyingdata.core.config.CanalConsumeProperties;
import com.flyingdata.core.config.FlyingDataSyncProperties;
import com.flyingdata.core.context.DataSyncContext;
import com.flyingdata.core.context.canal.TcpCanalDataSyncContext;
import com.flyingdata.core.utils.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public class SimpleTcpCanalDataSyncListener extends AbstractCanalDataSyncListener {

    protected final static Logger logger = LoggerFactory.getLogger(SimpleTcpCanalDataSyncListener.class);

    private CanalConnector connector;

    private CanalConsumeProperties consumeProperties;

    @Override
    public void init(FlyingDataSyncProperties properties) {
        super.init(properties);
        consumeProperties = properties.getConsume();
    }

    @Override
    protected void connect() {
        logger.info("CanalTcpDataSyncListener connect begin");
        connector = CanalConnectors.newSingleConnector(new InetSocketAddress(consumeProperties.getHost(),
                consumeProperties.getPort()), consumeProperties.getDestination(), consumeProperties.getUsername(), consumeProperties.getPassword());
        // 建立连接
        connector.connect(); // 建立连接
        String subscribe = consumeProperties.getSubscribe();
        //  监听过滤
        if (subscribe == null || subscribe.equals("")) {
            // 以服务端为准
            connector.subscribe();
        } else {
            // 以客户端为准
            connector.subscribe(subscribe);
        }
        connector.rollback(); // 回滚到上次的位置
        logger.info("CanalTcpDataSyncListener connect end");
    }

    @Override
    protected DataSyncContext request() {
        TcpCanalDataSyncContext context = new TcpCanalDataSyncContext();
        Message message = connector.getWithoutAck(consumeProperties.getBatchSize(), consumeProperties.getTimeoutMillis(), TimeUnit.MILLISECONDS); // timeoutMillis == 0时，阻塞监听
        context.setBatchId(message.getId());
        context.setSize(message.getEntries().size());
        if (context.getBatchId() == -1 || context.getSize() == 0) {
            logger.debug("The request result is empty.");
        } else {
            context.setRdbMessages(MessageUtil.message2RdbMessage(consumeProperties.getDestination(), "", message));
        }
        return context;
    }

    @Override
    protected void ack(DataSyncContext context) {
        TcpCanalDataSyncContext canalDataSyncContext = (TcpCanalDataSyncContext) context;
        if (canalDataSyncContext.getBatchId() != -1) {
            connector.ack(canalDataSyncContext.getBatchId());
        }
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
