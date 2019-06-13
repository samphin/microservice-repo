package org.common.test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.common.redis.RedisApplication;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReceiveAndReplyMessageCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.MessageProperties;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RabbitTest {
	
	@Autowired
    private AmqpTemplate template;
	
	protected final String queueName = "hello.world.queue";

	/**
	 * 发送消息
	 */
    @Test
    public void sendMessage() {
    	template.convertAndSend(queueName,"hello,rabbit~");
    }
    
    /**
     * 同步接收消息
     */
    @After
    public void realTimeReceive() {
    	Object obj = template.receiveAndConvert(queueName);
    	System.out.println("同步接收消息："+obj.toString());
    }
    
    /**
     * 异步接收消息
     */
    @Test
    public void ajaxReceiveMessage() {
    	Message message = template.receive("q_findUserInfo");
    	byte[] content = message.getBody();
    	String c = new String(content);
    	System.out.println("异步接收消息："+c);
    }
    
    public final static String QUEUE_NAME="rabbitMQ.test";
    /**
     * 通过rabbit原生态代码发送消息
     */
    @Test
    public void sendMsg(){
    	try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("127.0.0.1");
			factory.setUsername("guest");
			factory.setPassword("guest");
			factory.setPort(5672);
			Connection conn = factory.newConnection();
			Channel channel = conn.createChannel();
			Map<String,Object> reg = new HashMap<String, Object>();
			//定义对列
			channel.queueDeclare(QUEUE_NAME, true, false, false, reg);
			//分发信息
	        for (int i=0;i<10;i++){
	            String message="Hello RabbitMQ"+i;
	            channel.basicPublish("",QUEUE_NAME,
	                    MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
	            System.out.println("NewTask send '"+message+"'");
	        }
	        channel.close();
	        conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 通过rabbit原生态代码发送消息
     */
    @Test
    public void receiveMsg(){
    	try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("127.0.0.1");
			Connection conn = factory.newConnection();
			Channel channel = conn.createChannel();
			Map<String,Object> reg = new HashMap<String, Object>();
			//定义对列
			channel.queueDeclare(QUEUE_NAME, true, false, false, reg);
			System.out.println("Customer Waiting Received messages");
	        //DefaultConsumer类实现了Consumer接口，通过传入一个频道，
	        // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
	        //自动回复队列应答 -- RabbitMQ中的消息确认机制
		    Consumer consumer = new DefaultConsumer(channel) {
	            @Override
	            public void handleDelivery(String consumerTag, Envelope envelope,
	                                       AMQP.BasicProperties properties, byte[] body)
	                    throws IOException {
	                String message = new String(body, "UTF-8");
	                System.out.println("Customer Received '" + message + "'");
	            }
	        };
	        channel.basicConsume(QUEUE_NAME, true, consumer);
	        //只接收队列消息
	        // GetResponse gr = channel.basicGet(QUEUE_NAME, true);
			//System.out.println(new String(gr.getBody()));
	        channel.close();
	        conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
    }
}
