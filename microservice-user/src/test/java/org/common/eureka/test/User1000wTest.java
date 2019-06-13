package org.common.eureka.test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.common.eureka.UserSystemApplication;
import org.common.eureka.entity.SecUser;
import org.common.eureka.entity.User1000w;
import org.common.eureka.service.IUser1000wService;
import org.common.eureka.service.impl.RedisService;
import org.common.eureka.util.SerializeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 用户测试用例
 * @author samphin
 */
@SpringBootTest(classes=UserSystemApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class User1000wTest {
	@Autowired
	private IUser1000wService user1000wService;
	
	@Autowired
	private RedisService redisService;
	
	@Test
	public void findUserForChen() {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("firstName", "陈");
		long t1 = System.currentTimeMillis();
		System.out.println("开始查询："+t1);
		List<User1000w> users = user1000wService.queryAll(params);
		long t2 = System.currentTimeMillis();
		System.out.println("查询总耗时："+(t2-t1)+"，姓陈的用户共有"+users.size()+"条！");
	}
	
	/**
	 * 将用户信息存放到redis中
	 * 这种操作方式，效率更高。
	 */
	@Test
	public void setUserInfoToRedis() {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("firstName", "陈");
		List<User1000w> users = user1000wService.queryAll(params);
		for (User1000w user1000w : users) {
			redisService.lPush("user1000w_firstname_chen", user1000w);
		}
		System.out.println("姓名='陈'的用户缓存成功，共缓存了"+users.size()+"条用户信息!");
	}
	
	/**
	 * 从redis中取出姓陈用户信息
	 */
	@Test
	public void getUserInfoFromRedis() {
		long t1 = System.currentTimeMillis();
		System.out.println("从Redis中开始查询："+t1);
		List<Object> users = redisService.lRange("user1000w_firstname_chen", 0, 999999);
		/*for (Object object : users) {
			User1000w user = (User1000w)object;
			System.out.println("用户姓名："+user.getFirstName()+user.getLastName());
		}*/
		long t2 = System.currentTimeMillis();
		System.out.println("查询总耗时："+(t2-t1)+"，姓陈的用户共有"+users.size()+"条！");
	}
	
}
