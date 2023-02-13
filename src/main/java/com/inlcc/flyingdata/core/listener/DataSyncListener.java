package com.inlcc.flyingdata.core.listener;

import com.inlcc.flyingdata.core.handler.DataSyncHandler;
import com.inlcc.flyingdata.core.storage.DataSyncResultStorage;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public interface DataSyncListener extends Runnable {

    void init(List<DataSyncHandler> handlers, List<DataSyncResultStorage> resultStorages);

}
