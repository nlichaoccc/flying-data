package com.flyingdata.core.listener;

import com.flyingdata.core.config.FlyingDataSyncProperties;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public interface DataSyncListener extends Runnable {

    void init(FlyingDataSyncProperties properties);

    void run();

    void stop();

}
