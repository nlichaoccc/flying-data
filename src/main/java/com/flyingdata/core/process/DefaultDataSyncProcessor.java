package com.flyingdata.core.process;

import com.flyingdata.core.context.DataSyncContext;
import com.flyingdata.core.handler.DataSyncHandler;
import com.flyingdata.core.handler.DefaultDataSyncHandler;
import com.flyingdata.core.rdb.RdbMessage;
import com.flyingdata.core.result.SyncResult;
import com.flyingdata.core.storage.DataSyncResultStorage;
import com.flyingdata.core.utils.FastjsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/18
 */
public class DefaultDataSyncProcessor implements DataSyncProcessor {

    protected final static Logger logger = LoggerFactory.getLogger(DefaultDataSyncProcessor.class);

    /**
     * 数据处理端
     * 负责数据处理
     */
    private DataSyncHandler handler;


    /**
     * 结果存储器
     *  负责存储数据处理后的结果
     */
    private DataSyncResultStorage storage;

    public DefaultDataSyncProcessor(DataSyncHandler handler, DataSyncResultStorage storage) {
        this.handler = handler == null ? new DefaultDataSyncHandler() : handler;
        this.storage = storage;
    }

    @Override
    public void process(DataSyncContext context) {

        // 批处理
        List<SyncResult> results = new ArrayList<>();
        logger.info("process before data :{}", FastjsonUtil.toJSONString(context.getRdbMessages()));

        for (RdbMessage rdbMessage : context.getRdbMessages()) {

            if (rdbMessage.getIsDdl()) { // DDL语句跳过
                continue;
            }

            String rdbMessageType = rdbMessage.getType();
            // INSERT UPDATE DELETE
            if ("INSERT".equalsIgnoreCase(rdbMessageType)) {
                results.addAll(Optional.ofNullable(handler.insert(rdbMessage)).orElse(List.of()));
            } else if ("UPDATE".equalsIgnoreCase(rdbMessageType)) {
                results.addAll(Optional.ofNullable(handler.update(rdbMessage)).orElse(List.of()));
            } else if ("DELETE".equalsIgnoreCase(rdbMessageType)) {
                results.addAll(Optional.ofNullable(handler.delete(rdbMessage)).orElse(List.of()));
            } else {
                // ignore
            }
        }

        logger.info("process after data :{}", FastjsonUtil.toJSONString(results));

        if (storage != null) {
            storage.store(results);
        }

    }
}
