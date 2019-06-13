package org.common.eureka.controller;

import org.common.eureka.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class ArchivesRibbonController {
	private final Logger logger = LoggerFactory.getLogger(ArchivesRibbonController.class);
	
	//用RESTFUL请求接口
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	
	@HystrixCommand(fallbackMethod="findByIdForJsonFallBack")
	@GetMapping("/user/{id}")
	public String findByIdForJson(@PathVariable String id){
		//根据传入的服务名microservice-id，从负载均衡器中挑选一个对应服务的实例
		ServiceInstance serviceInstance = this.loadBalancerClient.choose("microservice-user");
		//打印当前选择的是哪个节点
		logger.info("{}:{}:{}",serviceInstance.getServiceId(),serviceInstance.getHost(),serviceInstance.getPort());
		//UserVo userVo = this.restTemplate.getForObject("http://"+serviceInstance.getServiceId()+"/findById/"+id, UserVo.class);
		//常用的访问方式
		ResponseEntity<String> responseEntity = this.restTemplate.getForEntity("http://"+serviceInstance.getServiceId()+"/findByIdForJson/"+id, String.class);
		String users = responseEntity.getBody();
		System.out.println(users);
		return users;
	}
	
	/**
	 * 配置断路器
	 * @return
	 */
	public String findByIdForJsonFallBack() {
		return "Error occurred!";
	}
	
	/**
	 * 获取用户对象
	 * @param id
	 * @return
	 */
	@GetMapping("/findById/{id}")
	public UserVo findById(@PathVariable String id){
		//根据传入的服务名microservice-id，从负载均衡器中挑选一个对应服务的实例
		ServiceInstance serviceInstance = this.loadBalancerClient.choose("microservice-user");
		//打印当前选择的是哪个节点
		logger.info("{}:{}:{}",serviceInstance.getServiceId(),serviceInstance.getHost(),serviceInstance.getPort());
		//常用的访问方式
		ResponseEntity<UserVo> responseEntity = this.restTemplate.getForEntity("http://"+serviceInstance.getServiceId()+"/findById/"+id, UserVo.class);
		UserVo users = responseEntity.getBody();
		System.out.println(users);
		return users;
	}
	
	@GetMapping("/userlist")
	public String userlist(){
		//根据传入的服务名microservice-id，从负载均衡器中挑选一个对应服务的实例
		ServiceInstance serviceInstance = this.loadBalancerClient.choose("microservice-user");
		//打印当前选择的是哪个节点
		logger.info("{}:{}:{}",serviceInstance.getServiceId(),serviceInstance.getHost(),serviceInstance.getPort());
		//常用的访问方式
		ResponseEntity<String> responseEntity = this.restTemplate.getForEntity("http://"+serviceInstance.getServiceId()+"/findAll", String.class);
		String users = responseEntity.getBody();
		System.out.println(users);
		return users;
	}
	
	/**
	 * 打印负载均衡日志
	 */
	@GetMapping("/log-instance")
	public void logUserInstance(){
		ServiceInstance serviceInstance = this.loadBalancerClient.choose("microservice-user");
		//打印当前选择的是哪个节点
		logger.info("{}:{}:{}",serviceInstance.getServiceId(),serviceInstance.getHost(),serviceInstance.getPort());
	}
}
