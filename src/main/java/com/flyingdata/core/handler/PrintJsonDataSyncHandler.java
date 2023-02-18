package com.flyingdata.core.handler;

import com.alibaba.fastjson.JSON;
import com.flyingdata.core.entry.DataSyncContext;
import com.flyingdata.core.rdb.RdbMessage;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/16
 */
public class PrintJsonDataSyncHandler implements DataSyncHandler {

    @Override
    public void handle(DataSyncContext context) {
        for (RdbMessage rdbMessage : context.getRdbMessages()) {
            System.out.println(JSON.toJSONString(rdbMessage));
        }
    }
}
