package com.flyingdata.core.storage;

import com.flyingdata.core.result.SyncResult;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public interface DataSyncResultStorage {
    void store(List<SyncResult> results);

}
