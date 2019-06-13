package org.common.eureka;

import org.common.eureka.config.RibbonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableHystrix
@RibbonClients(defaultConfiguration= {RibbonConfiguration.class})
public class ZuulApplication{
	
	public static void main(String[] args) {
		SpringApplication.run(ZuulApplication.class, args);
	}
}
