package org.common.eureka.config;

import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign配置
 */
@Configuration
public class FeignClientConfig {

    @Bean
    public BasicAuthRequestInterceptor getBasicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("samphin", "samphin123");
    }

    //日志级别NONE、BASIC、HEADERS、FULL
    @Bean
    public Logger.Level getFeignLoggerLevel() {
        //可以定义自己feign的日志级别，只在application.yml文件中添加Logger日志配置对feign来说是不起作用的。feign默认是NONE，不打印任何日志
        return feign.Logger.Level.FULL;
    }
}
