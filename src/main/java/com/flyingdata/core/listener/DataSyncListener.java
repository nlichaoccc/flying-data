package com.flyingdata.core.listener;

import com.flyingdata.core.handler.DataSyncHandler;
import com.flyingdata.core.storage.DataSyncResultStorage;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public interface DataSyncListener extends Runnable {

    List<DataSyncHandler> getHandlers();

    List<DataSyncResultStorage> getResultStorages();

    void stop();

}
