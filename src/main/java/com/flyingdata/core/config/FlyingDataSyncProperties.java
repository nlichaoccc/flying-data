package com.flyingdata.core.config;

import com.flyingdata.core.handler.DataSyncHandler;
import com.flyingdata.core.listener.DataSyncListener;
import com.flyingdata.core.storage.DataSyncResultStorage;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/18
 */
@Data
public class FlyingDataSyncProperties {

    /**
     * 订阅类型
     *  canal: tcp,kafka(kafka仅支持flatMessage=true)
     */
    private String subscribeType;

    /**
     * 数据输入端
     *  负责监听上游数据，并构建数据处理易读的上下文对象（DataSyncContext）
     */
    private CanalConnectProperties connect;

    private Class<? extends DataSyncListener> listener;

    /**
     * 数据处理端
     * 负责数据处理，可以有0个或多个
     */
    private List<Class<? extends DataSyncHandler>> handlers = new LinkedList<>();

    /**
     * 结果存储器
     *  负责存储数据处理后的结果，可以有0个或多个
     */
    private List<Class<? extends DataSyncResultStorage>> resultStorages = new LinkedList<>();

}
