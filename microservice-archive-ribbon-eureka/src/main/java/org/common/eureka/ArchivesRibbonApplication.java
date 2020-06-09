package org.common.eureka;

import org.common.eureka.config.RibbonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableEurekaClient
@SpringBootApplication
/*@EnableAutoConfiguration(exclude={
		DataSourceAutoConfiguration.class,
		JpaRepositoriesAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class})*/
@RibbonClient(name = "microservice-user", configuration = RibbonConfiguration.class)
public class ArchivesRibbonApplication {

    /**
     * 实现均衡负载注解
     *
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(ArchivesRibbonApplication.class, args);
    }
}
