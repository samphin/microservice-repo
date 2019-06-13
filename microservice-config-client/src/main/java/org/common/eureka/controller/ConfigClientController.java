package org.common.eureka.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigClientController {

	//获取Git仓库配置文件中的profile属性，而不是获取当前bootstrap中的profile配置spring.cloud.config.profile的值
	@Value("${profile}")
	private String profile;
	
	@GetMapping("/profile")
	public String hello() {
		return this.profile;
	}
}
