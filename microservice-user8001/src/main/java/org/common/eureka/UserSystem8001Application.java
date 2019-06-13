package org.common.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 描述：将用户系统作为微服务注册到服务中心
 * 包名：org.common.eureka
 * 作者：chenyongfeng
 * 时间：2018年11月27日
 */
//@EnableEurekaClient 该注解是spring-cloud-netflix中的，只能与eureka一起工作。在eureka项目中，这两个注解作用相同
//声明自己是一个eureka客户端，@EnableDiscoveryClient=@EnableEurekaClient
@EnableDiscoveryClient
@SpringBootApplication
public class UserSystem8001Application {

    public static void main(String[] args) {
        SpringApplication.run(UserSystem8001Application.class, args);
    }
}
