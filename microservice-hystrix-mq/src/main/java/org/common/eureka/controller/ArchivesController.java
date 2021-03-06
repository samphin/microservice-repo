package org.common.eureka.controller;

import org.common.eureka.interfaces.UserFeignClient;
import org.common.eureka.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
public class ArchivesController {
	private final Logger logger = LoggerFactory.getLogger(ArchivesController.class);
	
	//用RESTFUL请求接口
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	@Autowired
	private UserFeignClient userFeignClient;
	
	@GetMapping("/user/findById/{id}")
	public UserVo findById(@PathVariable String id){
		//默认的方式请求
		/*UserVo userVo = restTemplate.getForObject("http://127.0.0.1:8788/"+id, UserVo.class);
		System.out.println(new Gson().toJson(userVo));*/
		//用Feign声明式方式请求
		UserVo user = this.userFeignClient.findById(id);
		
		return user;
	}
	
	@GetMapping("/user/findByName/{userName}")
	public UserVo findByName(@PathVariable String userName){
		//用Feign声明式方式请求
		return this.userFeignClient.findByName("'%"+userName+"%'");
	}
	
	/**
	 * 打印负载均衡日志
	 */
	@GetMapping("/log-instance")
	public void logArchiverInstance(){
		ServiceInstance serviceInstance = this.loadBalancerClient.choose("EurekaUserSystem");
		//打印当前选择的是哪个节点
		logger.info("{}:{}:{}",serviceInstance.getServiceId(),serviceInstance.getHost(),serviceInstance.getPort());
	}
}
