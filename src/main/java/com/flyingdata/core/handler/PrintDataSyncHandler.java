package com.flyingdata.core.handler;

import com.flyingdata.core.entry.DataSyncContext;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/16
 */
public class PrintDataSyncHandler implements DataSyncHandler {

    @Override
    public void handle(DataSyncContext context) {
        System.out.println(context);
    }
}
