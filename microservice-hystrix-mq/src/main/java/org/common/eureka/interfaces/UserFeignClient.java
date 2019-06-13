package org.common.eureka.interfaces;

import org.common.eureka.interfaces.impl.FeignClientFallbakFactory;
import org.common.eureka.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//URL指明请求服务提供者的地址
//如果配置configuration=FeignDisableHystrixConfig.class，表示当前UserFeignClient接口禁用断路器HystrixConfig
@FeignClient(name="UserSystem",url="http://127.0.0.1:8788/",fallbackFactory=FeignClientFallbakFactory.class)
@Component
public interface UserFeignClient {

	@RequestMapping(value="/findById/{id}",method=RequestMethod.GET)
	public UserVo findById(@PathVariable("id")String id);
	
	
	@RequestMapping(value="/findByName/{userName}",method=RequestMethod.GET)
	public UserVo findByName(@PathVariable("userName") String userName);
}
