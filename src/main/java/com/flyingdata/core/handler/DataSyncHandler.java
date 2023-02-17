package com.flyingdata.core.handler;

import com.flyingdata.core.entry.DataSyncContext;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public interface DataSyncHandler {

    void handle(DataSyncContext context);


}
