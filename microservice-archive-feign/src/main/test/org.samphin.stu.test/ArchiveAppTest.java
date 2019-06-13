package org.samphin.stu.test;

import com.alibaba.fastjson.JSONObject;
import org.common.eureka.ArchivesSystemFeignClientApplication;
import org.common.eureka.controller.ArchivesFeignController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

/**
 * 前后端分离后，spring提供模拟客户端请求控制器的工具类
 * @author samphin
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= ArchivesSystemFeignClientApplication.class)
public class ArchiveAppTest {

	private MockMvc mvc;
	
	@Before
	public void setUp() throws Exception{
		mvc = MockMvcBuilders.standaloneSetup(ArchivesFeignController.class).build();
	}
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Test
	public void findUserInfo() throws Exception{
		//MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user/findUserInfo/"+id).accept(MediaType.APPLICATION_JSON)).andReturn();
		String id = "3ce7d3cb-b248-438a-82df-4f6e7e4828ea";
		/*UserVo userVo = restTemplate.getForObject("http://127.0.0.1:8788/"+id, UserVo.class);
		System.out.println(new Gson().toJson(userVo));*/
		JSONObject result = restTemplate.getForObject("http://127.0.0.1:8789/user/findUserInfo/"+id, JSONObject.class);
		System.out.println(result.toString());
	}
	
}
