package com.flyingdata.core.storage;

import com.flyingdata.core.entry.DataSyncContext;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public interface DataSyncResultStorage {
    void store(DataSyncContext context);

}
