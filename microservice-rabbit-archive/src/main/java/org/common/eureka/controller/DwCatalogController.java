package org.common.eureka.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.common.eureka.entity.DwCatalog;
import org.common.eureka.service.IDwCatalogService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * 所有请求都是通过restful风格定义的
 * @author samphin
 */
@RestController
public class DwCatalogController {
	
	@Autowired
	private IDwCatalogService catalogService;
	
	@Autowired
	private RabbitTemplate amqpTemplate;
	
	@GetMapping(value="findCatalog")
	public String findCatalog(String userId){
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("createUserId", userId);
		List<DwCatalog> datas = this.catalogService.queryAll(params);
		if(datas!=null && datas.size()>0) {
			return JSONArray.toJSONString(datas);
		}else {
			datas = new ArrayList<DwCatalog>();
			return JSONArray.toJSONString(datas);
		}
	}
	
	/**
	 * http://localhost:8888/sendCatalogMessageToMQ/3ce7d3cb-b248-438a-82df-4f6e7e4828ea
	 * @param userId
	 */
	@RabbitListener(queues= {"topic.user.catalog"})
	public void sendCatalogMessageToMQ(String userId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("createUserId", userId);
		List<DwCatalog> catalogs = catalogService.queryAll(params);
		if(catalogs!=null && catalogs.size()>0) {
			//监听topic.user.catalog队列并将查询到的目录信息发送至目录队列中
			amqpTemplate.convertAndSend("topic.catalog.message", JSON.toJSONString(catalogs));
		}
	}
}
