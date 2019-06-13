package org.common.eureka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;

@Configuration
public class RibbonConfiguration {

	//负载均衡规则，改为随机
	@Bean
	public IRule ribbonRule(){
		return new RandomRule();
	}
}
