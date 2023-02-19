package com.flyingdata.core.listener;

import com.flyingdata.core.config.FlyingDataSyncProperties;
import com.flyingdata.core.context.DataSyncContext;
import com.flyingdata.core.process.DefaultDataSyncProcessor;
import com.flyingdata.core.storage.DataSyncResultStorage;
import com.flyingdata.core.sync.DataSyncExecutor;
import com.flyingdata.core.utils.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public abstract class AbstractCanalDataSyncListener implements DataSyncListener {

    protected final static Logger logger = LoggerFactory.getLogger(DataSyncExecutor.class);

    private boolean running = true;


    private DefaultDataSyncProcessor processor;

    public AbstractCanalDataSyncListener() {
    }

    @Override
    public void init(FlyingDataSyncProperties properties) {
        DataSyncResultStorage storage = ObjectUtil.newNoArgInstance(properties.getStorage());
        // 初始化结果查询器
        storage.init(properties);
        this.processor = new DefaultDataSyncProcessor(ObjectUtil.newNoArgInstance(properties.getHandler()), storage);
    }


    @Override
    public void run() {
        while (running) { // 断线重连

            connect();

            while (running) { // 重复拉去消息

                // 拉取数据
                DataSyncContext context;
                try {
                    // 请求消费数据
                    context = request();
                } catch (Exception e) {
                    // 可能由于网络问题，导致断开连接，请求数据失败
                    logger.error("{} request error!", this.getClass().getSimpleName(), e);
                    try {
                        disconnect(); // 断开连接
                    } catch (Exception e2) {
                        logger.error("{} disconnect error!", this.getClass().getSimpleName(), e);
                    } finally {
                        try {
                            Thread.sleep(200L); // 休眠200ms，重新尝试连接
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    break;
                }

                // 消费数据
                try {

                    // 数据为空
                    if (context.getRdbMessages() == null || context.getRdbMessages().isEmpty()) {
                        continue;
                    }

                    // 数据处理
                    processor.process(context);

                    ack(context);

                } catch (Throwable e) {
                    logger.error("{} process error!", this.getClass().getSimpleName(), e);
                    rollback(); // 处理失败, 回滚数据
                }

            }

        }
    }

    @Override
    public void stop() {
        if (!running) {
            return;
        }
        running = false;
    }

    protected abstract void connect();

    protected abstract DataSyncContext request();

    protected abstract void ack(DataSyncContext entry);

    protected abstract void rollback();

    protected abstract void disconnect();


}
