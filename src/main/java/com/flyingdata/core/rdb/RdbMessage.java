package com.flyingdata.core.rdb;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *  参考 com.alibaba.otter.canal.client.adapter.support.Dml
 *
 * @author nlichaoccc
 * @since 2023/2/17
 */
@Data
public class RdbMessage implements Serializable {

    private String destination;                             // 对应canal的实例或者MQ的topic
    private String groupId;                                 // 对应mq的group id
    private String database;                                // 数据库或schema
    private String table;                                   // 表名
    private List<String> pkNames;                           // 主键字段列表
    private Map<String, Integer> columnTypes;               // 字段类型列表,value见java.sql.Types中的类型
    private Boolean isDdl;                                  // 是否为ddl语句
    private String sql;                                     // 执行的sql, dml sql为空
    private String type;                                    // 类型: INSERT UPDATE DELETE
    // binlog executeTime
    private Long es;                                        // sql执行时间，毫秒
    // dml build timeStamp
    private Long ts;                                        // 数据订阅同步时间，毫秒

    private List<Map<String, Object>> data;                 // 数据列表
    private List<Map<String, Object>> old;                  // 旧数据列表, 用于update, size和data的size一一对应
}
