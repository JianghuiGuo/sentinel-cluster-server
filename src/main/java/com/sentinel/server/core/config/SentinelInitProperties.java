package com.sentinel.server.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * Sentinel 初始化配置
 *
 * @author licong
 * @date 2019-12-18 2:03 下午
 */
@Configuration
@Data
public class SentinelInitProperties {

    @Value("${sentinel.init.port}")
    private Integer port;

    @Value("${sentinel.init.namespaces}")
    private Set<String> namespaces;

    @Value("${sentinel.init.nacos.namespace}")
    private String nacosNamespace;

    @Value("${sentinel.init.nacos-host}")
    private String nacosHost;

    @Value("${sentinel.init.group-id}")
    private String groupId;
}
