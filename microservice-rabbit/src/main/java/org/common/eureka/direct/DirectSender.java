package org.common.eureka.direct;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@Component
public class DirectSender {
	
	public final static String QUEUENAME="mq.test_demo1";//队列名称
	public final static String HOST="localhost";   //主机名
	public final static String USERNAME="guest";   
	public final static String PASSWORD="guest";   
	public final static Integer PORT=5672;
	public final static String EXCHANGE_NAME="direct.exchange.name";   
	public final static String ROUTING_KEY1="rk1";   
	public final static String ROUTING_KEY2="rk2"; 

	public void send() throws IOException, TimeoutException{
		ConnectionFactory factory = new ConnectionFactory();//创建一个连接工厂，用于生成与RabbitMQ进行连接
		factory.setHost(HOST);//根据这个连接工厂设置RabbitMQ所在的主机，账号密码和端口号等(默认情况下就不需要账号密码和端口了)
		factory.setUsername(USERNAME);
		factory.setPassword(PASSWORD);
		factory.setPort(PORT);
		Connection connection = factory.newConnection();
		
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUENAME, false, false, false, null);
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY1, null, "这里是客户端，这是由交换机和路由键1绑定的队列信息".getBytes());
		channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY2, null, "这里是客户端，这是由交换机和路由键2绑定的队列信息".getBytes());
		channel.close();
        connection.close();
        System.out.println("客户端信息发送完毕！");
		/******************RabbitTemplate操作方法*******************/
		/*rabbitTemplate.setRoutingKey(routingKey);
		rabbitTemplate.setExchange(exchange);
		rabbitTemplate.convertAndSend(exchange, routingKey, context);
		MessageProperties props = MessagePropertiesBuilder.newInstance()
			    .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
			    .build();
		Message message = MessageBuilder.withBody(context.getBytes())
			    .andProperties(props)
			    .build();
		rabbitTemplate.send(message);*/
	}
}
