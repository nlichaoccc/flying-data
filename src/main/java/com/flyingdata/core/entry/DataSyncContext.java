package com.flyingdata.core.entry;

import com.flyingdata.core.rdb.RdbMessage;
import lombok.Data;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
@Data
public class DataSyncContext {
    private List<RdbMessage> rdbMessages;

}
