package org.common.eureka.test;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.common.eureka.UserSystemApplication;
import org.common.eureka.entity.SecUser;
import org.common.eureka.service.ISecUserService;
import org.common.eureka.service.impl.RedisService;
import org.common.eureka.util.SerializeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

/**
 * 用户测试用例
 * @author samphin
 */
@SpringBootTest(classes=UserSystemApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SecUserTest {
	@Autowired
	private ISecUserService userService;
	
	//最大并发数
	private final int MAX_NUM = 1;
	
	//实例化一个并发测试对象
	private CountDownLatch cdl = new CountDownLatch(MAX_NUM); 

	//通过线程池来创建线程
	ExecutorService es = Executors.newFixedThreadPool(MAX_NUM);
	
	@Autowired
	private RedisService redisService;
	
	//并发压力测试
	@Test
	public void findUserInfo() {
		for(int i = 0; i < MAX_NUM; i++){
			Thread newThread = new Thread(new Runnable() {
					@Override
					public void run() {
						System.out.println("大家好，我是新来的线程！");
					}
			});
			
			Thread queryUserThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						newThread.join();
						cdl.await();
						String userId = "3ce7d3cb-b248-438a-82df-4f6e7e4828ea";
						//取出当前用户的目录信息
						userService.sendUserCatalogInfos(userId);
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//es.submit(newThread);
			es.submit(queryUserThread);
			cdl.countDown();
		}
		try {
			Thread.currentThread();
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将用户信息存放到redis中
	 * 这种操作方式，效率更高。
	 */
	@Test
	public void setUserInfoToRedis() {
		String userId = "3ce7d3cb-b248-438a-82df-4f6e7e4828ea";
		//取出当前用户的目录信息
		/*List list = redisService.lRange(userId, 0,10);
		for (int i = 0;i<list.size();i++) {
			System.out.println(list.get(i).toString());
		}*/
		List<SecUser> users = this.userService.queryAll(null);
		//redisService.set(userId, SerializeUtil.serializeForList(users));
		redisService.set(userId, users);
	}
	
	@Test
	public void getUserInfoToRedis() {
		String userId = "3ce7d3cb-b248-438a-82df-4f6e7e4828ea";
		List<SecUser> users = SerializeUtil.unserializeForList((byte[])redisService.get(userId));
		//取出当前用户的目录信息
		for (SecUser user : users) {
			System.out.println("用户ID："+user.getUserId()+"，用户名称："+user.getUserName());
		}
	}
	
	
}
