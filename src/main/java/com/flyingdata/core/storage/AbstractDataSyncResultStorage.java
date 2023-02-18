package com.flyingdata.core.storage;

import com.flyingdata.core.result.SyncResult;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/18
 */
public abstract class AbstractDataSyncResultStorage implements DataSyncResultStorage {


    @Override
    public void store(List<SyncResult> results) {

    }
}
