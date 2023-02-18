package com.flyingdata.core.handler;

import com.flyingdata.core.rdb.RdbMessage;
import com.flyingdata.core.result.SyncResult;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public interface DataSyncHandler {

    List<SyncResult> insert(RdbMessage message);
    List<SyncResult> update(RdbMessage message);
    List<SyncResult> delete(RdbMessage message);

}
