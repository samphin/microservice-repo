package org.common.eureka.direct;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Component;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

@Component
public class DirectConsumer {
	public final static String QUEUENAME="mq.test_demo1";//队列名称
	public final static String HOST="localhost";   //主机名
	public final static String USERNAME="guest";   
	public final static String PASSWORD="guest";   
	public final static Integer PORT=5672;
	public final static String EXCHANGE_NAME="direct.exchange.name";   
	public final static String ROUTING_KEY1="rk1";   
	public final static String ROUTING_KEY2="rk2";
	
	public void consumer() throws IOException, TimeoutException {
		ConnectionFactory factory=new ConnectionFactory();//创建一个连接工厂，用于生成与RabbitMQ进行连接
		factory.setHost(HOST);//根据这个连接工厂设置RabbitMQ所在的主机，账号密码和端口号等(默认情况下就不需要账号密码和端口了)
		factory.setUsername(USERNAME);
		factory.setPassword(PASSWORD);
		factory.setPort(PORT);
		Connection connection = factory.newConnection();
		
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		//绑定队列名。交换器和路由键
		channel.queueBind(QUEUENAME, EXCHANGE_NAME, ROUTING_KEY1);
		channel.queueBind(QUEUENAME, EXCHANGE_NAME, ROUTING_KEY2);
		
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            	String message = new String(body, "UTF-8");
                System.out.println("路由键" + envelope.getRoutingKey() + "收到消息：    " + message + "'");
            }
        };
        channel.basicConsume(QUEUENAME, true, consumer);
	}
}
