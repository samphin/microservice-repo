package org.common.test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.common.redis.RedisApplication;
import org.common.redis.utils.RedisUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedisTest {
	
	@Resource
	private RedisUtil redisUtil;
	
	@Before
	public void optInsertUser(){
		redisUtil.set("company", "GeoStar");
	}
	//操作字符串
	@Test
	public void optString(){
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
		Map<String,Object> company = new HashMap<String, Object>();
		company.put("name", "GeoStar");
		company.put("cjsj", 1998);
		company.put("telephone", "18802096261");
		companyList.add(company);
		redisUtil.set("companyJsonString", JSONArray.fromObject(companyList).toString());
		JSONArray jsonArray = JSONArray.fromObject(redisUtil.get("companyJsonString"));
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject json = jsonArray.getJSONObject(i);
			System.out.println("联系电话--"+json.getString("telephone"));
		}
	}
	
	//操作hash
	@Test
	public void optHash(){
		Map<String,Object> company = new HashMap<String, Object>();
		company.put("name", "GeoStar");
		company.put("cjsj", 1998);
		company.put("telephone", "18802096261");
		redisUtil.set("companyMap", company);
	}
	
	@Test
	public void optForList(){
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
		Map<String,Object> company = new HashMap<String, Object>();
		company.put("name", "GeoStar");
		company.put("cjsj", 1998);
		company.put("telephone", "18802096261");
		companyList.add(company);
		redisUtil.set("companyList", companyList);
		
		List<Map<String, Object>> result = (List)redisUtil.get("companyList");
		for (Map<String, Object> map : result) {
			System.out.println(JSONUtils.valueToString(map));
		}
	}
	
	@Test
	public void opsForSet(){
	}
	
	@Test
	public void opsForZSet(){
	}
}
