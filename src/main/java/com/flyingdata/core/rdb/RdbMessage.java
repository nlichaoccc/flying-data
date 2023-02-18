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
    /**
     * 对应canal的实例或者MQ的topic
     */
    private String destination;
    /**
     * 对应mq的group id
     */
    private String groupId;
    /**
     * 数据库或schema
     */
    private String database;
    /**
     * 表名
     */
    private String table;
    /**
     * 主键字段列表
     */
    private List<String> pkNames;
    /**
     * 字段类型列表,value见{@link java.sql.Types}中的类型
     */
    private Map<String, Integer> columnTypes;
    /**
     * 是否为ddl语句
     */
    private Boolean isDdl;
    /**
     * 执行的sql, dml sql为空
     */
    private String sql;
    /**
     * 类型: INSERT UPDATE DELETE
     */
    private String type;
    /**
     * binlog executeTime
     */
    private Long es;
    /**
     * dml build timeStamp
     */
    private Long ts;
    /**
     * 数据列表
     */
    private List<Map<String, Object>> data;
    /**
     * 旧数据列表, 用于update, size和data的size一一对应
     */
    private List<Map<String, Object>> old;
}
