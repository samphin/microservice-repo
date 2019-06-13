package org.common.eureka.controller;

import java.util.List;

import javax.validation.Valid;

import org.common.eureka.entity.User1000w;
import org.common.eureka.service.IUser1000wService;
import org.common.eureka.service.impl.RedisService;
import org.common.eureka.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 所有请求都是通过restful风格定义的
 * @author samphin
 */
@RestController
@RequestMapping("/userController")
public class UserController {
	
	@Autowired
	private IUser1000wService user1000wService;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private ResponseResult responseResult;
	
	/**
	 * 从redis中取出姓陈用户信息，并返回到界在，用vue渲染出死表数据。
	 */
	@GetMapping("/queryUserInfoForChen/{start}/{end}") 
	public List<Object> queryUserInfoForChen(@PathVariable int start,@PathVariable int end) {
		long t1 = System.currentTimeMillis();
		System.out.println("从Redis中开始查询："+t1);
		List<Object> users = redisService.lRange("user1000w_firstname_chen", 0, 9);
		long t2 = System.currentTimeMillis();
		System.out.println("查询总耗时："+(t2-t1)+"，姓陈的用户共有"+users.size()+"条！");
		return users;
	}
	
	/**
	 * 新增用户
	 * @param id
	 * @return
	 */
	@PostMapping(value="/addUser/{user}", produces = "application/json", 
            consumes = "application/json") 
	public ResponseResult addUser(@RequestBody @Valid User1000w user){
		try {
			user1000wService.insert(user);
			
			//并将数据，写入到Redis
			return responseResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return responseResult.failure();
		}
	}
	
	/**
	 *  删除用户
	 * @param id
	 * @return
	 */
	@DeleteMapping("/deleteUser/{id}")
	public ResponseResult delete(@PathVariable int id){
		try {
			user1000wService.deleteByPrimaryKey(id);
			return responseResult.success();
		} catch (Exception e) {
			return responseResult.failure();
		}
	}
	
	/**
	 * 修改
	 * @param user
	 * @return
	 */
	@PutMapping(value="/updateUser/{user}", produces = "application/json", 
            consumes = "application/json")
	public ResponseResult update(@RequestBody @Valid User1000w user){
		try {
			user1000wService.updateByPrimaryKeySelective(user);
			//从redis中查询当前用户信息，如果存在也更新缓存信息。
			return responseResult.success();
		} catch (Exception e) {
			return responseResult.failure();
		}
	}
}
