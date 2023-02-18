package com.flyingdata.core.process;

import com.flyingdata.core.context.DataSyncContext;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/18
 */
public interface DataSyncProcessor {

    void process(DataSyncContext context);

}
