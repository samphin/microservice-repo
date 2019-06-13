package org.samphin.stu.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.samphin.stu.bean.ESClientSpringFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Elasticsearch 核心配置类
 */
@Configuration
@ComponentScan(basePackageClasses = ESClientSpringFactory.class)
public class ESConfig {
    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;

    @Value("${elasticsearch.schema}")
    private String schema;

    @Value("${elasticsearch.connectNum}")
    private Integer connectNum;

    @Value("${elasticsearch.connectPerRoute}")
    private Integer connectPerRoute;

    @Bean
    public HttpHost httpHost() {
        return new HttpHost(host, port, schema);
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public ESClientSpringFactory getFactory() {
        return ESClientSpringFactory.build(httpHost(), connectNum, connectPerRoute);
    }

    @Bean
    @Scope("singleton")
    public RestClient getRestClient() {
        return getFactory().getClient();
    }

    @Bean
    @Scope("singleton")
    public RestHighLevelClient getRHLClient() {
        return getFactory().getRhlClient();
    }
}
