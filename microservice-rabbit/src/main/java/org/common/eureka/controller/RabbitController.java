package org.common.eureka.controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.common.eureka.direct.DirectConsumer;
import org.common.eureka.direct.DirectSender;
import org.common.eureka.fanout.FanoutSender;
import org.common.eureka.object.ObjectSender;
import org.common.eureka.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitController {
	@Autowired
	private FanoutSender fanoutSender;
	
	@Autowired
	private ObjectSender objectSender;
	
	@Autowired
	private DirectSender directSender;
	
	@Autowired
	private DirectConsumer directConsumer;
	
	/**********************Fanout类型******************/
	@GetMapping("/fanout/send/{context}")
	public void fanoutSend(@PathVariable String context){
		fanoutSender.send(context);
	}
	
	/**********************Direct类型******************/
	@GetMapping("/direct/send")
	public void sendMessage(){
		try {
			directSender.send();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/direct/receive")
	public void receiveMessage(){
		try {
			directConsumer.consumer();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/senderObj/userVo")
	public void sendMessage(@PathVariable UserVo userVo){
		objectSender.send(userVo);
	}
}
