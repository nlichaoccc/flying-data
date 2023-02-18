package com.flyingdata.core.config;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/18
 */
@Data
public class CanalConsumeProperties {

    // ------------------------------ 公共参数 begin ----------------------------------------
    /**
     * 对应canal的实例或者MQ的topic
     */
    private String destination;
    /**
     * 订阅消息数量
     */
    private int batchSize = 1000;
    /**
     * 订阅监听超时时间
     */
    private long timeoutMillis = 100L;

    // ------------------------------ 公共参数 end ----------------------------------------



    // ------------------------------ tcp config begin ----------------------------------------
    private String host = "localhost";
    private int port = 11111;
    private String username = "";
    private String password = "";
    private String subscribe;

    // ------------------------------ tcp config end ----------------------------------------




    // ------------------------------ kafka config begin ----------------------------------------
    private String kafkaServers = "localhost:9092";
    private String groupId;                                 // 对应mq的group id
    private Integer partition = 0;                          // 分区
    // ------------------------------ kafka config end ----------------------------------------

//    private String  zkServers = "localhost:2181";

}
