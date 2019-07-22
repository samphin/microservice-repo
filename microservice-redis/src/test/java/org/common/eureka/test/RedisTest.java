package org.common.eureka.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.common.eureka.RedisApplication;
import org.common.eureka.entity.Article;
import org.common.eureka.service.IOrderService;
import org.common.eureka.util.RedisService;
import org.common.eureka.util.SnowflakeIdWorker;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;

/**
 * Redis测试用例
 * 
 * @author samphin
 */
@SpringBootTest(classes = RedisApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisTest {

	Logger logger = LoggerFactory.getLogger(RedisTest.class);

	@Resource
	private RedisService redisService;

	@Resource
	private RedisTemplate redisTemplate;

	@Resource
	private IOrderService orderService;

	/**
	 * 发布文章
	 */
	@Test
	public void publishArticle() {
		Assert.assertNotNull(redisTemplate);
		// 雪花算法生成Key
		try {
			long id = SnowflakeIdWorker.generateId();
			String articleId = "article:" + id;
			Article article = new Article();
			article.setArticleId(articleId);
			article.setTitle("CentOS7_NAT模式网络配置");
			article.setContent("CentOS7_NAT模式网络配置Content......");
			article.setPublishUserId("10002");
			article.setPublishDate(new Date());
			Map<String, Object> m = objectToMap(article);
			redisTemplate.opsForHash().putAll(articleId, m);
			System.out.println("文章发布成功....");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取已发布文章
	 */
	@Test
	public void queryArticle() {
		Assert.assertNotNull(redisTemplate);
		String articleId = "article:313105798007398400";
		//获取指定文章值信息
		List<Object> articleList = redisTemplate.opsForHash().values(articleId);

		System.out.println("articleList = " + JSONArray.toJSONString(articleList));

		//获取指定文章key-value信息
		Map<String,Object> articleMap = redisTemplate.opsForHash().entries(articleId);

		System.out.println("articleMap = " + JSONObject.toJSONString(articleMap));
	}

	@Test
	public void stringTest() {
		Assert.assertNotNull(redisTemplate);
		String article = "article:001";
		System.out.println("============" + redisTemplate.opsForValue().get(article));
		logger.info("============" + redisTemplate.opsForValue().get(article));
	}

	/**
	 * 保存订单信息
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void orderSaveTest() {
		/**
		 * 不用SessionCallback的话会报错，ERR EXEC WITHOUT MULTI，原因如下：
		 * 我们查询multi、delete等源代码，发现会执行RedisTemplate类中execute()方法 进行跟踪发现
		 * RedisCallback中doInRedis获取的RedisConnection每次都是新的，所以才导致该问题。
		 */
		SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
			@Override
			public Object execute(RedisOperations operations) throws DataAccessException {
				// 开始手动提交事务
				logger.info("========开始事务");
				operations.multi();
				// 设置redisTemplate为事务
				// redisTemplate.setEnableTransactionSupport(true);
				// 用hash保存订单信息
				Map<String, Object> order1 = new HashMap<String, Object>();
				order1.put("orderId", 1);
				order1.put("money", 88.8);
				order1.put("time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				redisTemplate.opsForHash().putAll("order:1", order1);
				Map<String, Object> order2 = new HashMap<String, Object>();
				order2.put("orderId", 2);
				order2.put("money", 55.5);
				order2.put("time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				redisTemplate.opsForHash().putAll("order:2", order2);
				Map<String, Object> order3 = new HashMap<String, Object>();
				order3.put("orderId", 3);
				order3.put("money", 66.6);
				order3.put("time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				redisTemplate.opsForHash().putAll("order:3", order3);
				// 生成用户信息
				Map<String, Object> user1 = new HashMap<String, Object>();
				user1.put("userId", 10001);
				user1.put("name", "samphin");
				user1.put("sex", "boy");
				user1.put("age", 27);
				redisTemplate.opsForHash().putAll("user:10001", user1);
				// 将订单放入队列中
				Object[] orders = { "order:1", "order:2", "order:3" };
				redisTemplate.opsForList().leftPushAll("user:10001:order", orders);
				logger.info("==================订单信息保存成功！");
				Object val = operations.exec();
				logger.info("==================事务提交信息：" + JSONObject.toJSONString(val));
				return val;
			}
		};
		// 执行当前会话中队列中命令，一条一条
		redisTemplate.execute(sessionCallback);
	}

	/**
	 * 查询订单信息
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void orderQueryTest() {
		// 查询订单信息
		List<String> pros = new ArrayList<String>();
		pros.add("orderId");
		pros.add("money");
		List orders = redisTemplate.opsForHash().multiGet("order:1", pros);
		Iterator it = orders.iterator();
		while (it.hasNext()) {
			logger.info(it.next().toString());
		}
		// 根据用户查询订单信息
		// 弹出队列中的订单信息
		logger.info("==================订单信息查询成功！");
	}

	/**
	 * 将Object对象里面的属性和值转化成Map对象
	 * 
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 */
	public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		Class<?> clazz = obj.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			//打开私有访问
			field.setAccessible(true);
			String fieldName = field.getName();
			Object value = field.get(obj);
			map.put(fieldName, value);
		}
		return map;
	}
}
