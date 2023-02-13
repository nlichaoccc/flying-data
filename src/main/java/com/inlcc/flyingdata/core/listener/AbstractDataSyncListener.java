package com.inlcc.flyingdata.core.listener;

import com.inlcc.flyingdata.core.handler.DataSyncHandler;
import com.inlcc.flyingdata.core.storage.DataSyncResultStorage;

import java.util.List;


/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public abstract class AbstractDataSyncListener implements DataSyncListener {


    /**
     * 数据处理端
     *  负责数据处理，可以有0个或多个
     */
    private List<DataSyncHandler> handlers;


    /**
     * 结果存储器
     *  负责存储数据处理后的结果，可以有0个或多个
     */
    private List<DataSyncResultStorage> resultStorages;


    public List<DataSyncHandler> getHandlers() {
        return handlers;
    }

    public List<DataSyncResultStorage> getResultStorages() {
        return resultStorages;
    }

    @Override
    public void init(List<DataSyncHandler> handlers, List<DataSyncResultStorage> resultStorages) {
        this.handlers = handlers;
        this.resultStorages = resultStorages;
    }
}
