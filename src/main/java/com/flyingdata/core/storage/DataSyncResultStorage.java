package com.flyingdata.core.storage;

import com.flyingdata.core.config.FlyingDataSyncProperties;
import com.flyingdata.core.result.SyncResult;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 *  TODO 实现类
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public interface DataSyncResultStorage {
    void init(FlyingDataSyncProperties properties);
    void store(List<SyncResult> results);

}
