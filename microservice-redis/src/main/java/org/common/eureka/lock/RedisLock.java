package org.common.eureka.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 通过Redis实现分布式锁 思路： 通过竞争获取锁 竞争锁--排队 任务在对资源操作中 占有锁--占坑 其他任务等待 任务阻塞 直到该任务完成 释放锁
 *
 * @author samphin
 */
public class RedisLock implements Lock {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String LOCK_KEY = "lockKey";
    // 添加锁值，为1代表加锁成功
    private static final String LOCK_FLAG = "1";

    private static final long expireTime = 20000L;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DefaultRedisScript<String> redisScript;

    @Autowired
    private static RedisSerializer<String> argsSerializer;

    @Autowired
    private static RedisSerializer resultSerializer;

    private static final Long EXEC_RESULT = 1L;

    // 声明一个本地线程来装载redisKey
    private ThreadLocal<String> threadLocal = new ThreadLocal<>();

    // 阻塞式加锁
    @Override
    public void lock() { // 锁---是为了排队
        // 1、尝试加锁
        if (tryLock()) {
            return;
        }
        // 2、加锁失败，当前任务休眠一段时间
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 递归调用，再次重新加锁
        lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
    }

    // 非阻塞式加锁，往 Redis中写入lock=1的数据，如果数据插入成功代表加锁成功
    @Override
    public boolean tryLock() {
        // 随机产生一个锁值
        String lockValue = UUID.randomUUID().toString();
        // 如果该Key不存在，说明可以加锁，则直接返回true
        boolean lockFlag = stringRedisTemplate.opsForValue().setIfAbsent(LOCK_KEY, lockValue);
        // 设置上锁的过期时间为50s
        stringRedisTemplate.expire(LOCK_KEY, 50, TimeUnit.SECONDS);
        if (lockFlag) {
            // 设置成功，并将锁值存放到本地线程中
            threadLocal.set(lockValue);
            return true;
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    // 使用事务来操作解锁
    @Override
    public void unlock() {
        /**
         * 不用SessionCallback的话会报错，ERR EXEC WITHOUT MULTI，原因如下：
         * 我们查询multi、delete等源代码，发现会执行RedisTemplate类中execute()方法 进行跟踪发现
         * RedisCallback中doInRedis获取的RedisConnection每次都是新的，所以才导致该问题。
         */
        // 通过事务操作
        /*
         * SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
         *
         * @Override public Object execute(RedisOperations operations) throws
         * DataAccessException { // 开始手动提交事务 logger.info("========开始解锁");
         * operations.multi(); String lockKey =
         * String.valueOf(operations.opsForValue().get(LOCK_KEY)); if (null != lockKey
         * && lockKey.equals(threadLocal.get())) { //
         * 如果从redis中获得锁不为空且redis锁和我当前线程中临时存放的锁是一致的，是删除 operations.delete(LOCK_KEY); } //
         * 通过key从redis中获取随机值的锁，然后再删除 Object val = operations.exec();
         * logger.info("==================解锁成功：" + JSONObject.toJSONString(val)); return
         * val; } }; stringRedisTemplate.execute(sessionCallback);
         */
        // 第二种方式 ：也可以用lua脚本语言来实现。
        /*
         * redisScript.setScriptSource(new ResourceScriptSource(new
         * ClassPathResource("unlock.lua"))); stringRedisTemplate.execute(redisScript,
         * Arrays.asList(LOCK_KEY), Arrays.asList(threadLocal.get()));
         */

        // 第二种方式另一种写法
        String luaContent = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then return redis.call(\"del\",KEYS[1])\r\n"
                + "else return 0\r\n" + "end";
        redisScript.setScriptText(luaContent);
        stringRedisTemplate.execute(redisScript, Arrays.asList(LOCK_KEY), Arrays.asList(threadLocal.get()));
    }

    @Override
    public Condition newCondition() {
        // TODO Auto-generated method stub
        return null;
    }

}
