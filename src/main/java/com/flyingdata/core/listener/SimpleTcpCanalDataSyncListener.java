package com.flyingdata.core.listener;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.flyingdata.core.entry.DataSyncContext;
import com.flyingdata.core.entry.canal.TcpCanalDataSyncContext;
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

    private String host = "hk-host.inlcc.cn";

    private int port = 11111;

    private String destination = "test";

    private String username = "";

    private String password = "";

    private int batchSize = 100;

    private long timeoutMillis = 1000L;

    private String subscribe = ".*\\..*";

    @Override
    protected void connect() {
        logger.info("CanalTcpDataSyncListener connect begin");
        connector = CanalConnectors.newSingleConnector(new InetSocketAddress(host,
                port), destination, username, password);
        // 建立连接
        connector.connect(); // 建立连接
        connector.subscribe(subscribe); //  监听过滤
        connector.rollback(); // 回滚到上次的位置
        logger.info("CanalTcpDataSyncListener connect end");
    }

    @Override
    protected DataSyncContext request() {
        TcpCanalDataSyncContext context = new TcpCanalDataSyncContext();
        Message message = connector.getWithoutAck(batchSize, timeoutMillis, TimeUnit.MILLISECONDS); // timeoutMillis == 0时，阻塞监听
        context.setBatchId(message.getId());
        context.setSize(message.getEntries().size());
        if (context.getBatchId() == -1 || context.getSize() == 0) {
            logger.debug("The request result is empty.");
        } else {
            context.setRdbMessages(MessageUtil.message2RdbMessage(destination, "", message));
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
