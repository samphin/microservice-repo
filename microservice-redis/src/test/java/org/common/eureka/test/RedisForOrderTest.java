package org.common.eureka.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.common.eureka.RedisApplication;
import org.common.eureka.dto.OrderDto;
import org.common.eureka.entity.Order;
import org.common.eureka.service.IOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Redis 缓存雪崩
 *
 * @author samphin
 */
@SpringBootTest(classes = RedisApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisForOrderTest {

    Logger logger = LoggerFactory.getLogger(RedisForOrderTest.class);

    private ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<String, Lock>();

    private final static int MAX_NUM = 100;
    // 定义5000并发
    private CountDownLatch cdl = new CountDownLatch(MAX_NUM);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private IOrderService orderService;

    @Resource
    private CacheManager cacheManager;

    /**
     * 新增测试数据
     *
     * @author samphin
     * @date 2019年4月9日 下午11:14:58 @GitConfig：
     */
    @Test
    public void addOrder() {
        OrderDto order = new OrderDto();
        order.setId("10003");
        order.setUseCode("kobe");
        order.setOrderCode("AS8936673433");
        order.setOrderTime(new Date());
        order.setPrice(new BigDecimal("52.1"));
        this.orderService.save(order);
        logger.info("=====订单保存成功");
    }

    /**
     * 保存订单信息
     */
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
                try {
                    Order order1 = new Order("10001", "OR133456739", "samphin", new Date(), new BigDecimal(88.8));
                    redisTemplate.opsForHash().putAll("order:10001", objectToMap(order1));
                    Order order2 = new Order("10002", "OR1334DFA9", "james", new Date(), new BigDecimal(66.6));
                    redisTemplate.opsForHash().putAll("order:10002", objectToMap(order2));
                    Order order3 = new Order("10003", "OR143789739", "kobe", new Date(), new BigDecimal(10.66));
                    redisTemplate.opsForHash().putAll("order:10003", objectToMap(order3));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                // 生成用户信息
                Map<String, Object> user1 = new HashMap<String, Object>();
                user1.put("userId", 10001);
                user1.put("name", "samphin");
                user1.put("sex", "boy");
                user1.put("age", 27);
                redisTemplate.opsForHash().putAll("user:10001", user1);
                // 将订单放入队列中
                Object[] orders = {"order:1", "order:2", "order:3"};
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
     * 查询订单信息，缓存雪崩解决方案。 当查询的key不存在或者过期。此时，高并发访问进来怎么办？ 1、对访问的key进行加锁处理
     * 2、高并发时，第一个线程获得锁后，从数据库中查询订单信息，并将订单重新缓存到Redis中。那么剩下的线程就可能从 缓存中获取到订单信息了。
     */
    @Test
    public void queryOrderTest() {
        String orderId = "10003";
        try {
            for (int i = 0; i < MAX_NUM; i++) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        queryOrder(orderId);
                        try {
                            cdl.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                cdl.countDown();
            }

            try {
                Thread.currentThread();
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Order queryOrder(String orderId) {
        // 从缓存中取数据
        Cache.ValueWrapper valueWrapper = cacheManager.getCache("order").get(orderId);
        if (null != valueWrapper) {
            System.out.println("从Redis缓存中取到订单的信息：" + JSON.toJSONString(valueWrapper.get()));
            return (Order) (valueWrapper.get());
        }

        // 加锁排队，通过阻塞线程来实现加锁
        doLock(orderId);

        try {
            // 一次只有一个线程会得到锁，然后查询数据库
            valueWrapper = cacheManager.getCache("order").get(orderId);
            if (null != valueWrapper) {
                System.out.println("从Redis缓存中取到订单的信息：" + JSON.toJSONString(valueWrapper.get()));
                return (Order) (valueWrapper.get());
            }
            Order order = orderService.queryOne(orderId);
            if (null != order) {
                cacheManager.getCache("order").put(orderId, order);
                System.out.println("从数据库中取到订单的信息：" + JSON.toJSONString(order));
            }
            return order;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // 解锁
            releaseLock(orderId);
        }
    }

    /**
     * 解锁
     *
     * @param lockcode
     * @author samphin
     * @date 2019年4月9日 下午6:47:20 @GitConfig：
     */
    private void releaseLock(String lockcode) {
        ReentrantLock oldLock = (ReentrantLock) locks.get(lockcode);
        if (oldLock != null && oldLock.isHeldByCurrentThread()) {
            oldLock.unlock();
        }
    }

    /**
     * 加锁
     *
     * @param lockcode
     * @author samphin
     * @date 2019年4月9日 下午6:47:37 @GitConfig：
     */
    private void doLock(String lockcode) {
        // 创建一个锁
        ReentrantLock newLock = new ReentrantLock();
        Lock oldLock = locks.putIfAbsent(lockcode, newLock);
        if (null == oldLock) {
            newLock.lock();
        } else {
            oldLock.lock();
        }
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
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }
}
