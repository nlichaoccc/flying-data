package com.flyingdata.core.config;

import lombok.Data;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/19
 */
@Data
public class StorageProperties {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String urls;
    private String indexName;
    private Map<String, Object> extend; // 扩展配置
}
