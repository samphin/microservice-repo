package org.common.eureka.config;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@RibbonClient(name="microservice-provider-archives",configuration=RibbonConfiguration.class)
public class TestConfiguration {

}
