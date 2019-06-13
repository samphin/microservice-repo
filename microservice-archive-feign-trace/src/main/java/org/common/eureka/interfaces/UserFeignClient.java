package org.common.eureka.interfaces;

import org.common.eureka.interfaces.impl.FeignClientFallbackFactory;
import org.common.eureka.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//URL指明请求服务提供者的地址
//如果配置configuration=FeignDisableHystrixConfig.class，表示当前UserFeignClient接口禁用断路器HystrixConfig
@FeignClient(name="microservice-user",url="http://127.0.0.1:8001/",fallbackFactory= FeignClientFallbackFactory.class)
@Component
public interface UserFeignClient {

	@GetMapping("/user/json/{id}")
	public String queryJsonById(@PathVariable(value = "id") String id);

	@GetMapping("/user/{id}")
	public UserVo queryOne(@PathVariable(value = "id") String id);

	@GetMapping("/user/all")
	public String queryAll();

	@GetMapping("/user/name/{userName}")
	public String queryJsonByName(@PathVariable(value = "userName") String userName);
}
