package org.common.test;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.common.redis.RedisApplication;
import org.common.redis.utils.MongodbGridFSUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MongoDBTest {
	
	@Autowired
    private MongodbGridFSUtil mongodbGridFSUtil;
	
	@Before
	public void optInsertUser(){
		
	}
	
	//存储普通JSONString
	@Test
	public void optString(){
		Map<String,Object> company = new HashMap<String, Object>();
		company.put("name", "GeoStar");
		company.put("cjsj", 1998);
		company.put("telephone", "18802096261");
		JSONObject.fromObject(company);
	}
	
	//存储二进制
	@Test
	public void optHash(){
		
	}
}
