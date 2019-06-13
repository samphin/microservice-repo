package org.common.eureka.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DwCatalogQueueConfig {
	
	@Value("${spring.rabbitmq.host}")
	private String host;
	@Value("${spring.rabbitmq.port}")
	private String port;
	
	@Bean
	public ConnectionFactory cachingConnectionFactory() {
		ConnectionFactory factory = new CachingConnectionFactory(host,Integer.valueOf(port));
		return factory;
	}
	
	@Bean
	public AmqpAdmin amqpAdmin() {
		AmqpAdmin amqpAdmin = new RabbitAdmin(cachingConnectionFactory());
		amqpAdmin.declareQueue(queue());
		return amqpAdmin;
	}
	
	@Bean
	public AmqpTemplate amqpTemplate() {
		return new RabbitTemplate(cachingConnectionFactory());
	}

	@Bean
	public Queue queue() {
		return new Queue("catalog-list");
	}
}
