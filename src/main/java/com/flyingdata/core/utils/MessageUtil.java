package com.flyingdata.core.utils;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.FlatMessage;
import com.alibaba.otter.canal.protocol.Message;
import com.flyingdata.core.rdb.RdbMessage;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Message对象解析工具类
 *
 * @author rewerma 2018-8-19 下午06:14:23
 * @version 1.0.0
 */
public class MessageUtil {

    public static List<RdbMessage> flatMessage2RdbMessage(String destination, String groupId, List<FlatMessage> flatMessages) {
        if (flatMessages == null || flatMessages.isEmpty()) {
            return new ArrayList<>();
        }
        return flatMessages.stream().map(flatMessage -> flatMessage2RdbMessage(destination, groupId, flatMessage)).collect(Collectors.toList());
    }

    public static RdbMessage flatMessage2RdbMessage(String destination, String groupId,FlatMessage flatMessage) {

        RdbMessage rdbMessage = new RdbMessage();
        rdbMessage.setDestination(destination);
        rdbMessage.setGroupId(groupId);

        if (flatMessage == null) {
            return rdbMessage;
        }

        rdbMessage.setDatabase(flatMessage.getDatabase());
        rdbMessage.setTable(flatMessage.getTable());
        rdbMessage.setPkNames(flatMessage.getPkNames());
        rdbMessage.setColumnTypes(flatMessage.getSqlType());
        rdbMessage.setType(flatMessage.getType());
        rdbMessage.setIsDdl(flatMessage.getIsDdl());
        rdbMessage.setSql(flatMessage.getSql());
        rdbMessage.setEs(flatMessage.getEs());
        rdbMessage.setTs(flatMessage.getTs());
        rdbMessage.setData(MessageUtil.changeRows(flatMessage.getTable(), flatMessage.getData(), flatMessage.getSqlType(), flatMessage.getMysqlType()));
        rdbMessage.setOld(MessageUtil.changeRows(flatMessage.getTable(), flatMessage.getOld(), flatMessage.getSqlType(), flatMessage.getMysqlType()));
        return rdbMessage;
    }

    public static List<RdbMessage> message2RdbMessage(String destination, String groupId, Message message) {
        if (message == null) {
            return null;
        }
        List<CanalEntry.Entry> entries = message.getEntries();
        List<RdbMessage> rdbMessages = new ArrayList<>(entries.size());
        for (CanalEntry.Entry entry : entries) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN
                || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }

            CanalEntry.RowChange rowChange;
            try {
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                    e);
            }

            CanalEntry.EventType eventType = rowChange.getEventType();

            final RdbMessage rdbMessage = new RdbMessage();
            rdbMessage.setIsDdl(rowChange.getIsDdl());
            rdbMessage.setDestination(destination);
            rdbMessage.setGroupId(groupId);
            rdbMessage.setDatabase(entry.getHeader().getSchemaName());
            rdbMessage.setTable(entry.getHeader().getTableName());
            rdbMessage.setType(eventType.toString());
            rdbMessage.setEs(entry.getHeader().getExecuteTime());
            rdbMessage.setIsDdl(rowChange.getIsDdl());
            rdbMessage.setTs(System.currentTimeMillis());
            rdbMessage.setSql(rowChange.getSql());
            rdbMessages.add(rdbMessage);
            List<Map<String, Object>> data = new ArrayList<>();
            List<Map<String, Object>> old = new ArrayList<>();

            if (!rowChange.getIsDdl()) {
                Set<String> updateSet = new HashSet<>();
                rdbMessage.setPkNames(new ArrayList<>());
                int i = 0;
                for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                    if (eventType != CanalEntry.EventType.INSERT && eventType != CanalEntry.EventType.UPDATE
                        && eventType != CanalEntry.EventType.DELETE) {
                        continue;
                    }

                    Map<String, Object> row = new LinkedHashMap<>();
                    List<CanalEntry.Column> columns;

                    if (eventType == CanalEntry.EventType.DELETE) {
                        columns = rowData.getBeforeColumnsList();
                    } else {
                        columns = rowData.getAfterColumnsList();
                    }

                    for (CanalEntry.Column column : columns) {
                        if (i == 0) {
                            if (column.getIsKey()) {
                                rdbMessage.getPkNames().add(column.getName());
                            }
                        }
                        if (column.getIsNull()) {
                            row.put(column.getName(), null);
                        } else {
                            row.put(column.getName(),
                                JdbcTypeUtil.typeConvert(rdbMessage.getTable(),
                                    column.getName(),
                                    column.getValue(),
                                    column.getSqlType(),
                                    column.getMysqlType()));
                        }
                        // 获取update为true的字段
                        if (column.getUpdated()) {
                            updateSet.add(column.getName());
                        }
                    }
                    if (!row.isEmpty()) {
                        data.add(row);
                    }

                    if (eventType == CanalEntry.EventType.UPDATE) {
                        Map<String, Object> rowOld = new LinkedHashMap<>();
                        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
                            if (updateSet.contains(column.getName())) {
                                if (column.getIsNull()) {
                                    rowOld.put(column.getName(), null);
                                } else {
                                    rowOld.put(column.getName(),
                                        JdbcTypeUtil.typeConvert(rdbMessage.getTable(),
                                            column.getName(),
                                            column.getValue(),
                                            column.getSqlType(),
                                            column.getMysqlType()));
                                }
                            }
                        }
                        // update操作将记录修改前的值
                        if (!rowOld.isEmpty()) {
                            old.add(rowOld);
                        }
                    }

                    i++;
                }
                if (!data.isEmpty()) {
                    rdbMessage.setData(data);
                }
                if (!old.isEmpty()) {
                    rdbMessage.setOld(old);
                }
            }
        }

        return rdbMessages;
    }

    private static List<Map<String, Object>> changeRows(String table, List<Map<String, String>> rows,
                                                        Map<String, Integer> sqlTypes, Map<String, String> mysqlTypes) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, String> row : rows) {
            Map<String, Object> resultRow = new LinkedHashMap<>();
            for (Map.Entry<String, String> entry : row.entrySet()) {
                String columnName = entry.getKey();
                String columnValue = entry.getValue();

                Integer sqlType = sqlTypes.get(columnName);
                if (sqlType == null) {
                    continue;
                }

                String mysqlType = mysqlTypes.get(columnName);
                if (mysqlType == null) {
                    continue;
                }

                Object finalValue = JdbcTypeUtil.typeConvert(table, columnName, columnValue, sqlType, mysqlType);
                resultRow.put(columnName, finalValue);
            }
            result.add(resultRow);
        }
        return result;
    }
}
