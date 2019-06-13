package org.common.eureka.service.impl;

import java.util.List;
import java.util.Map;

import org.common.eureka.dao.SecUserMapper;
import org.common.eureka.entity.SecUser;
import org.common.eureka.service.ISecUserService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;

@Service
public class SecUserServiceImpl implements ISecUserService {
	
	@Autowired
	private SecUserMapper userMapper;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private AmqpTemplate amqpTemplate;

	@Override
	public int deleteByPrimaryKey(String userId) {
		return userMapper.deleteByPrimaryKey(userId);
	}

	@Override
	public int insert(SecUser record) {
		return userMapper.insert(record);
	}

	@Override
	public int insertSelective(SecUser record) {
		return userMapper.insertSelective(record);
	}

	@Override
	public SecUser queryByPrimaryKey(String userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	@Override
	public int updateByPrimaryKeySelective(SecUser record) {
		return userMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(SecUser record) {
		return userMapper.updateByPrimaryKey(record);
	}
	
	@Override
	public List<SecUser> queryAll(Map<String, Object> params) {
		return userMapper.selectAll(params);
	}
	
	@Override
	public void sendUserCatalogInfos(String userId) {
		//将要查询的信息发送到队列中
		amqpTemplate.convertAndSend("exchange", "topic.user.catalog", userId);
		
	}
	
	@Override
	public String queryUserCatalogInfos(String userId) {
		//将得到的目录信息，缓存到Redis中，方便业务处理
		Object catalogInfo = redisService.get(userId);
		System.out.println("Redis的目录信息："+String.valueOf(catalogInfo));
		return String.valueOf(catalogInfo);
	}
	
	/**
	 * 监听器监听指定中间件中的队列名称topic.catalog.message，
	 * @param catalogInfo
	 */
	//@RabbitListener(queues = "topic.catalog.message")
	public void processCatalog(String catalogInfo) {
		System.out.println("目录信息："+catalogInfo);
		JSONArray catalogInfos = JSONArray.parseArray(catalogInfo);
		if(catalogInfos!=null && catalogInfos.size()>0) {
			String userId = catalogInfos.getJSONObject(0).getString("createUserId");
			//将得到的目录信息，缓存到Redis中，方便业务处理
			redisService.lPush(userId, catalogInfos.toJavaList(Map.class));
		}
	}
}
