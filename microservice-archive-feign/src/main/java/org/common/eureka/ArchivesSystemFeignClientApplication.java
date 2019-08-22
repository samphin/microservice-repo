package org.common.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 描述：将档案系统作为微服务注册到服务中心
 * 包名：org.common.eureka
 * 作者：samphin
 * 时间：2018年11月27日
 */
//@EnableEurekaClient 该注解是spring-cloud-netflix中的，只能与eureka一起工作。在eureka项目中，这两个注解作用相同
//声明自己是一个eureka客户端，@EnableDiscoveryClient=@EnableEurekaClient
@EnableFeignClients
@SpringCloudApplication
@EnableHystrix
@EnableCircuitBreaker
public class ArchivesSystemFeignClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArchivesSystemFeignClientApplication.class, args);
    }
}
