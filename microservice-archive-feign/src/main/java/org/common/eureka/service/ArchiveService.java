package org.common.eureka.service;

import com.alibaba.fastjson.JSONObject;
import org.common.eureka.dto.UserDto;
import org.common.eureka.interfaces.UserFeignClient;
import org.common.eureka.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.*;

@Service
public class ArchiveService {

	@Autowired
	private UserFeignClient userFeignClient;
	
	//创建一个线程池
	ExecutorService task = Executors.newFixedThreadPool(10);

	/**
	 * 查询用户信息
	 * @param userDto
	 * @return
	 */
	public JSONObject queryUserInfo(UserDto userDto) {
		long t1 = System.currentTimeMillis();
		System.out.println("访问开始时间："+t1);
		
		//通过并发编程方法，引入多线程，用线程池创业务子线程。
		//通过用户ID查询用户信息
		Callable<JSONObject> queryJsonById = new Callable<JSONObject>() {
			@Override
			public JSONObject call() throws Exception {
				JSONObject user1 = JSONObject.parseObject(userFeignClient.queryJsonById(userDto.getId()));
				return user1;
			}
		};
		
		Callable<JSONObject> queryJsonByName = new Callable<JSONObject>() {
			@Override
			public JSONObject call() throws Exception {
				JSONObject user2 = JSONObject.parseObject(userFeignClient.queryJsonByName(userDto.getUsername()));
				return user2;
			}
		};
		
		FutureTask<JSONObject> queryJsonByIdTask = new FutureTask<JSONObject>(queryJsonById);
		FutureTask<JSONObject> queryJsonByNameTask = new FutureTask<>(queryJsonByName);
		//new Thread(findUserByIdTask).start();
		//new Thread(findUserByNameTask).start();
		
		task.submit(queryJsonByIdTask);
		task.submit(queryJsonByNameTask);
		
		JSONObject resultData = new JSONObject();
		
		try {
			//只有当子线程执行完成，取得了返回值后，主线程才能继续执行。
			resultData.putAll(queryJsonByIdTask.get());//阻塞方法
			resultData.putAll(queryJsonByNameTask.get());//阻塞方法
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		long t2 = System.currentTimeMillis();
		System.out.println("请求总耗时："+(t2-t1));
		
		return resultData;
	}
}
