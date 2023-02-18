package com.flyingdata.core.config;

import lombok.Data;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/18
 */
@Data
public class FlyingDataConfigProperties {

    Map<String, FlyingDataSyncProperties> sync;

}
