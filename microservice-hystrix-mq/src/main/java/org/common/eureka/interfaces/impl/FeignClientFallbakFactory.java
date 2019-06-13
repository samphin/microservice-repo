package org.common.eureka.interfaces.impl;

import org.common.eureka.interfaces.UserFeignClient;
import org.common.eureka.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import feign.hystrix.FallbackFactory;

/**
 * 回退类，专为Feign接口提供回退原因
 * 防止雪崩效应，接口提供者接口出错，快速失败返回用户信息
 * @author samphin
 */
@Component
public class FeignClientFallbakFactory implements FallbackFactory<UserFeignClient> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeignClientFallbakFactory.class);
	
	@Override
	public UserFeignClient create(Throwable cause) {
		return new UserFeignClient() {
			
			@Override
			public UserVo findByName(String userName) {
				UserVo userVo = new UserVo();
				userVo.setUserId("-1");
				userVo.setUserName("administrator");
				return userVo;
			}
			
			@Override
			public UserVo findById(String id) {
				//日志最好放在各个fallback方法中，而不要直接放在create方法中。
				//否则在引用启动时，就会打印该日志 
				FeignClientFallbakFactory.LOGGER.info("fallback；reason was:"+cause);
				UserVo userVo = new UserVo();
				if(cause instanceof IllegalArgumentException) {
					userVo.setUserId("-1");
				}else {
					userVo.setUserId("-2");
				}
				userVo.setUserName("administrator");
				return userVo;
			}
		};
	}

}
