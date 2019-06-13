package org.common.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 描述：将Rabbit消费者作为微服务注册到服务中心
 * 包名：org.common.eureka
 * 作者：chenyongfeng
 * 时间：2019-1-10 16:26:27
 */
@EnableEurekaClient
@SpringBootApplication(exclude= {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class,
		MongoDataAutoConfiguration.class,
		MongoAutoConfiguration.class
})
public class RabbitArchiveApplication{
	
	public static void main(String[] args) {
		SpringApplication.run(RabbitArchiveApplication.class, args);
	}
}
