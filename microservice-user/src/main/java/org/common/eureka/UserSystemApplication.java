package org.common.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 描述：将用户系统作为微服务注册到服务中心
 * 包名：org.common.eureka
 * 作者：chenyongfeng
 * 时间：2018年11月27日
 */
@SpringBootApplication
public class UserSystemApplication{

	public static void main(String[] args) {
		SpringApplication.run(UserSystemApplication.class, args);
	}
}
