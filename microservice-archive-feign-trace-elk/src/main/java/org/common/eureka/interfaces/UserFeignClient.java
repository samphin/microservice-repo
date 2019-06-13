package org.common.eureka.interfaces;

import org.common.eureka.config.FeignDisableHystrixConfig;
import org.common.eureka.interfaces.impl.FeignClientFallbackFactory;
import org.common.eureka.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//URL指明请求服务提供者的地址
//如果配置configuration=FeignDisableHystrixConfig.class，表示当前UserFeignClient接口禁用断路器HystrixConfig
@FeignClient(name = "MICROSERVICE-USER", fallbackFactory = FeignClientFallbackFactory.class, configuration = FeignDisableHystrixConfig.class)
@Component
public interface UserFeignClient {

	@GetMapping("/user/json/{id}")
	public String queryJsonById(@PathVariable(value = "id") String id);

	/**
	 * 获取用户对象信息
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/user/{id}")
	public UserVo queryOne(@PathVariable(value = "id") String id);

	@GetMapping("/user/all")
	public String queryAll();

	@GetMapping("/user/name/{userName}")
	public String queryJsonByName(@PathVariable(value = "userName") String userName);
}
