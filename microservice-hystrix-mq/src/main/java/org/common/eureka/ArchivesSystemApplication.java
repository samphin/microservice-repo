package org.common.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 描述：将档案系统作为微服务注册到服务中心
 * 包名：org.common.eureka
 * 作者：chenyongfeng
 * 时间：2018年11月27日
 */
//@EnableEurekaClient 该注解是spring-cloud-netflix中的，只能与eureka一起工作。在eureka项目中，这两个注解作用相同
//声明自己是一个eureka客户端，@EnableDiscoveryClient=@EnableEurekaClient
@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
@EnableCircuitBreaker
public class ArchivesSystemApplication{
	
	@Bean
	@LoadBalanced//实现均衡负载注解
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(ArchivesSystemApplication.class, args);
	}
}
