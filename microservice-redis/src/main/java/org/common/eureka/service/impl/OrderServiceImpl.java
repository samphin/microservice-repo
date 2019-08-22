package org.common.eureka.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.common.eureka.mapper.IOrderDao;
import org.common.eureka.dto.OrderDto;
import org.common.eureka.entity.Order;
import org.common.eureka.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    // 声时一个布隆过滤器
    private BloomFilter<CharSequence> bf = null;

    // 在bean初始化完成后，实例化bloomFilter，并加载数据
    @PostConstruct
    public void init() {
        List<Order> orders = orderDao.findAll();
        bf = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), orders.size());
        for (Order order : orders) {
            bf.put(order.getId());
        }
    }

    @CachePut(value = "order", keyGenerator = "keyGenerator")
    @Override
    public boolean save(OrderDto orderDto) {
        Order order = new Order().buildOrder(orderDto);
        order = this.orderDao.save(order);
        if (order == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean update(OrderDto orderDto) {
        Order order = new Order().buildOrder(orderDto);
        order = this.orderDao.saveAndFlush(order);
        if (order == null) {
            return false;
        }
        return true;
    }

    @CacheEvict(value = "order", key = "#orderId")
    @Override
    public boolean delete(String orderId) {
        Order order = this.orderDao.getOne(orderId);
        this.orderDao.delete(order);
        return true;
    }

    @Cacheable(value = "order", keyGenerator = "keyGenerator")
    @Override
    public Order queryOne(String orderId) {
        // 先判断布隆过滤器中是否存在该值，值存在才允许访问缓存和数据库
        if (!bf.mightContain(orderId)) {
            System.out.println("非法访问-----------" + System.currentTimeMillis());
            return null;
        }

        System.out.println("数据库中得到数据-----------" + System.currentTimeMillis());
        Order order = this.orderDao.getOne(orderId);

        //将数据库查到的订单信息缓存到redis
        redisTemplate.opsForValue().set("order:"+orderId, JSONObject.toJSONString(order));
        return order;
    }

    @Override
    public List<Order> queryList(OrderDto orderDto) {
        Order order = new Order().buildOrder(orderDto);
        ExampleMatcher matcher = ExampleMatcher.matching();
        matcher.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.regex());
        Example<Order> example = Example.of(order, matcher);
        return this.orderDao.findAll(example);
    }
}
