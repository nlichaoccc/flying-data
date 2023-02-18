package com.flyingdata.core.result;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/18
 */
@Data
public class SyncResult {

    /**
     * 类型: INSERT UPDATE DELETE
     */
    private String type;
    /**
     * 主键字段列表
     */
    private List<String> pkNames;
    private List<Object> pkValues;
    /**
     * 数据列表
     */
    private Map<String, Object> data;

    private String _id;
}
