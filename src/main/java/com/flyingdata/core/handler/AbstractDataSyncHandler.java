package com.flyingdata.core.handler;

import com.flyingdata.core.rdb.RdbMessage;
import com.flyingdata.core.result.SyncResult;
import com.flyingdata.core.utils.ObjectUtil;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/18
 */
public abstract class AbstractDataSyncHandler implements DataSyncHandler{

    @Override
    public List<SyncResult> insert(RdbMessage message) {
        List<SyncResult> results = new ArrayList<>();

        for (int i = 0; i < message.getData().size(); i++) {
            Map<String, Object> data = message.getData().get(i);

            SyncResult syncResult = createInsertResult(CreateInsertResultParam.builder()
                    .database(message.getDatabase())
                    .table(message.getTable())
                    .data(data)
                    .build());

            syncResult.setType(Optional.ofNullable(syncResult.getType()).orElse(message.getType()));
            syncResult.setPkNames(Optional.ofNullable(syncResult.getPkNames()).orElse(message.getPkNames()));
            syncResult.setPkValues(Optional.ofNullable(syncResult.getPkValues()).orElse(searchPkValues(message.getPkNames(), data)));
            syncResult.set_id(Optional.ofNullable(syncResult.get_id()).orElse(ObjectUtil.join2String(syncResult.getPkValues(),"-")));

            results.add(syncResult);
        }

        return results;
    }

    @Override
    public List<SyncResult> update(RdbMessage message) {
        List<SyncResult> results = new ArrayList<>();

        for (int i = 0; i < message.getData().size(); i++) {
            Map<String, Object> data = message.getData().get(i);
            Map<String, Object> old = message.getOld().get(i);

            SyncResult syncResult = createUpdateResult(CreateUpdateResultParam.builder()
                    .database(message.getDatabase())
                    .table(message.getTable())
                    .data(data)
                    .old(old)
                    .build());

            syncResult.setType(Optional.ofNullable(syncResult.getType()).orElse(message.getType()));
            syncResult.setPkNames(Optional.ofNullable(syncResult.getPkNames()).orElse(message.getPkNames()));
            syncResult.setPkValues(Optional.ofNullable(syncResult.getPkValues()).orElse(searchPkUpdateValues(message.getPkNames(), data, old)));
            syncResult.set_id(Optional.ofNullable(syncResult.get_id()).orElse(ObjectUtil.join2String(syncResult.getPkValues(),"-")));

            results.add(syncResult);
        }

        return results;
    }

    @Override
    public List<SyncResult> delete(RdbMessage message) {
        List<SyncResult> results = new ArrayList<>();

        for (int i = 0; i < message.getData().size(); i++) {
            // 删除的数据在data里存放
            Map<String, Object> old = message.getData().get(i);

            SyncResult syncResult = createDeleteResult(CreateDeleteResultParam.builder()
                    .database(message.getDatabase())
                    .table(message.getTable())
                    .old(old)
                    .build());
            syncResult.setType(Optional.ofNullable(syncResult.getType()).orElse(message.getType()));
            syncResult.setPkNames(Optional.ofNullable(syncResult.getPkNames()).orElse(message.getPkNames()));
            syncResult.setPkValues(Optional.ofNullable(syncResult.getPkValues()).orElse(searchPkValues(message.getPkNames(), old)));
            syncResult.set_id(Optional.ofNullable(syncResult.get_id()).orElse(ObjectUtil.join2String(syncResult.getPkValues(),"-")));

            results.add(syncResult);
        }

        return results;
    }

    public abstract SyncResult createInsertResult(CreateInsertResultParam param);
    public abstract SyncResult createUpdateResult(CreateUpdateResultParam param);
    public abstract SyncResult createDeleteResult(CreateDeleteResultParam param);


    private List<Object> searchPkValues(List<String> pkNames, Map<String, Object> data) {
        List<Object> pkValues = new ArrayList<>();
        for (String pkName : pkNames) {
            pkValues.add(data.get(pkName));
        }
        return pkValues;
    }

    private List<Object> searchPkUpdateValues(List<String> pkNames, Map<String, Object> data, Map<String, Object> old) {
        List<Object> pkValues = new ArrayList<>();
        for (String pkName : pkNames) {
            Object d = data.get(pkName);
            Object o = old.get(pkName);
            if (o != null && o.equals(d)) { // 防止修改主键
                pkValues.add(o);
            } else {
                pkValues.add(d);
            }
        }
        return pkValues;
    }


    @Data
    @Builder
    public static class CreateInsertResultParam {
        private String database;
        private String table;
        private Map<String, Object> data;

    }

    @Data
    @Builder
    public static class CreateUpdateResultParam {
        private String database;
        private String table;
        private Map<String, Object> data;
        private Map<String, Object> old;
    }

    @Data
    @Builder
    public static class CreateDeleteResultParam {
        private String database;
        private String table;
        private Map<String, Object> old;
    }

}

