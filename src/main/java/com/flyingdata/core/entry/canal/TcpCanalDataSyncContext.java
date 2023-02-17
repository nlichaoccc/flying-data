package com.flyingdata.core.entry.canal;

import com.flyingdata.core.entry.DataSyncContext;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/14
 */
@Data
public class TcpCanalDataSyncContext extends DataSyncContext {
    private long batchId = -1;

    private int size = 0;
}
