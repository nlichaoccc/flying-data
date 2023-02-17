package com.flyingdata.core.sync;

import com.flyingdata.core.exception.FlyingDataException;
import com.flyingdata.core.handler.DataSyncHandler;
import com.flyingdata.core.listener.DataSyncListener;
import com.flyingdata.core.storage.DataSyncResultStorage;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/13
 */
public class DataSynchronizer {

    /**
     * 数据输入端
     *  负责监听上游数据，并构建数据处理易读的上下文对象（DataSyncContext）
     */
    private DataSyncListener listener;

    /**
     * 数据处理端
     * 负责数据处理，可以有0个或多个
     */
    private List<DataSyncHandler> handlers = new LinkedList<>();

    /**
     * 结果存储器
     *  负责存储数据处理后的结果，可以有0个或多个
     */
    private List<DataSyncResultStorage> resultStorages = new LinkedList<>();

    private ExecutorService executor;


    private DataSynchronizer() {
    }

    public static DataSynchronizerBuilder builder(DataSyncListener listener) {
        return new DataSynchronizerBuilder(listener);
    }

    public void start() {
        handlers.forEach(dataSyncHandler -> listener.getHandlers().add(dataSyncHandler));
        resultStorages.forEach(resultStorage -> listener.getResultStorages().add(resultStorage));
        if (executor == null) {
            // 设置默认线程池
            executor = DataSyncExecutor.executor;
        }
        // 提交监听任务
        executor.submit(listener);
    }

    public void stop() {
        listener.stop();
    }


    public static class DataSynchronizerBuilder {

        private DataSynchronizer synchronizer;

        public DataSynchronizerBuilder(DataSyncListener listener) {
            this.synchronizer = new DataSynchronizer();
            this.synchronizer.listener = listener;
        }

        public DataSynchronizerBuilder addHandler(DataSyncHandler handler) {
            this.synchronizer.handlers.add(handler);
            return this;
        }

        public DataSynchronizerBuilder addResultStorage(DataSyncResultStorage resultStorage) {
            this.synchronizer.resultStorages.add(resultStorage);
            return this;
        }

        public DataSynchronizerBuilder setExecutorPool(ExecutorService executorPool) {
            this.synchronizer.executor = executorPool;
            return this;
        }

        public DataSynchronizer build() {
            if (synchronizer.listener == null) {
                throw new FlyingDataException("The DataSyncListener does not exist in current DataSynchronizer");
            }
            return synchronizer;
        }
    }
}
