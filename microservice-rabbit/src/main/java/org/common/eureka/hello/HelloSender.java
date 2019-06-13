package org.common.eureka.hello;

import java.util.Date;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelloSender {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void send(String context) {
		this.rabbitTemplate.convertAndSend("hello", context+"\t"+new Date());
	}

}