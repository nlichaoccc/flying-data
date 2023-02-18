package com.flyingdata.core.handler;

import com.flyingdata.core.result.SyncResult;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/16
 */
public class DefaultDataSyncHandler extends AbstractDataSyncHandler {

    @Override
    public SyncResult createInsertResult(CreateInsertResultParam param) {
        SyncResult syncResult = new SyncResult();
        syncResult.setData(param.getData());
        return syncResult;
    }

    @Override
    public SyncResult createUpdateResult(CreateUpdateResultParam param) {
        SyncResult syncResult = new SyncResult();
        syncResult.setData(param.getData());
        return syncResult;
    }

    @Override
    public SyncResult createDeleteResult(CreateDeleteResultParam param) {
        SyncResult syncResult = new SyncResult();
        syncResult.setData(new HashMap<>(2));
        return syncResult;
    }
}
