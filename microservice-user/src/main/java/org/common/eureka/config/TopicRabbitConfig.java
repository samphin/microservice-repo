package org.common.eureka.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 在档案系统启动时，就将相关的RabbitMQ元素创建好，交换机、路由键、队列
 * @author samphin
 */
@Configuration
public class TopicRabbitConfig {

	final static String message = "topic.user.catalog";
    final static String messages = "topic.user.catalogs";
 
    @Bean
    public Queue queueMessage() {
        return new Queue(TopicRabbitConfig.message);
    }
 
    @Bean
    public Queue queueMessages() {
        return new Queue(TopicRabbitConfig.messages);
    }
 
    @Bean
    TopicExchange exchange() {
        return new TopicExchange("exchange");
    }
 
    @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.user.catalog");
    }
 
    @Bean
    Binding bindingExchangeMessages(Queue queueMessages, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessages).to(exchange).with("topic.#");
    }
}
