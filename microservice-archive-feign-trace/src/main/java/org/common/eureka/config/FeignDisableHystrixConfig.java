package org.common.eureka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import feign.Feign;

/**
 * 如果引用@FeignClient的类想要禁用Hystrix，则可用
 * @FeignClient(name="user",configuration=FeignDisableHystrixConfig.class)
 * public interface UserFeignClient(){
 * }
 * @author samphin
 *
 */
//@Configuration
public class FeignDisableHystrixConfig {

	@Bean
	@Scope("prototype")
	public Feign.Builder feignBuilder(){
		return Feign.builder();
	}
}
