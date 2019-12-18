package com.sentinel.server.core.init;

import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterParamFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.server.ClusterTokenServer;
import com.alibaba.csp.sentinel.cluster.server.SentinelDefaultTokenServer;
import com.alibaba.csp.sentinel.cluster.server.config.ClusterServerConfigManager;
import com.alibaba.csp.sentinel.cluster.server.config.ServerTransportConfig;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.misyi.architecture.commons.core.context.SpringContextHolder;
import com.sentinel.server.core.config.SentinelInitProperties;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * Sentinel 初始化
 *
 * @author licong
 * @date 2019-12-18 11:21 上午
 */
@Log4j2
public class SentinelInit implements InitFunc {

    @Override
    public void init() throws Exception {
        SentinelInitProperties sentinelInitProperties = SpringContextHolder.getBean(SentinelInitProperties.class);
        // 创建集群动态规则源
        log.info("[Sentinel] 加载流控规则配置");
        ClusterFlowRuleManager.setPropertySupplier(namespace -> {
            ReadableDataSource<String, List<FlowRule>> ds =
                    new NacosDataSource<>(sentinelInitProperties.getNacosHost()
                            , sentinelInitProperties.getGroupId()
                            ,sentinelInitProperties.getNacosNamespace() + ":flow-rules"
                            , source
                            -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
            return ds.getProperty();
        });
        log.info("[Sentinel] 加载热点规则配置");
        ClusterParamFlowRuleManager.setPropertySupplier(namespace -> {
            ReadableDataSource<String, List<ParamFlowRule>> ds =
                    new NacosDataSource<>(sentinelInitProperties.getNacosHost()
                            , sentinelInitProperties.getGroupId()
                            ,sentinelInitProperties.getNacosNamespace() + ":param-flow-rules"
                            , source
                            -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {}));
            return ds.getProperty();
        });


//        // Server namespace set (scope) data source.
//        ReadableDataSource<String, Set<String>> namespaceDs = new NacosDataSource<>(remoteAddress, groupId,
//                namespaceSetDataId, source -> JSON.parseObject(source, new TypeReference<Set<String>>() {}));
//        ClusterServerConfigManager.registerNamespaceSetProperty(namespaceDs.getProperty());
//        // Server transport configuration data source.
//        ReadableDataSource<String, ServerTransportConfig> transportConfigDs = new NacosDataSource<>(remoteAddress,
//                groupId, serverTransportDataId,
//                source -> JSON.parseObject(source, new TypeReference<ServerTransportConfig>() {}));
//        ClusterServerConfigManager.registerServerTransportProperty(transportConfigDs.getProperty());

        log.info("[Sentinel] 加载集群namespace配置");
        // 加载Sentinel namespace
        ClusterServerConfigManager.loadServerNamespaceSet(sentinelInitProperties.getNamespaces());
        // 加载ServerTransportConfig
        ClusterServerConfigManager.loadGlobalTransportConfig(new ServerTransportConfig()
                .setIdleSeconds(600)
                .setPort(sentinelInitProperties.getPort()));
        // 创建一个 ClusterTokenServer 的实例，独立模式
        ClusterTokenServer tokenServer = new SentinelDefaultTokenServer();
        // 启动
        tokenServer.start();
    }
}
