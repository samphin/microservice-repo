package org.common.eureka.controller;

import com.alibaba.fastjson.JSONObject;
import org.common.eureka.dto.UserDto;
import org.common.eureka.interfaces.UserFeignClient;
import org.common.eureka.service.ArchiveService;
import org.common.eureka.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/archives")
public class ArchivesFeignController{

    private final Logger logger = LoggerFactory.getLogger(ArchivesFeignController.class);

    @Autowired
    private ArchiveService archiveService;

    @Autowired
    private UserFeignClient userFeignClient;

    /**
     * 获取用户对象信息
     *
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    public UserVo queryOne(@PathVariable(value = "id") String id){
        return userFeignClient.queryOne(id);
    }

    //获取用户信息
    @GetMapping("/user_info")
    public JSONObject queryUserInfo(@ModelAttribute UserDto userDto) {
        //默认的方式请求
		/*UserVo userVo = restTemplate.getForObject("http://127.0.0.1:8788/"+id, UserVo.class);
		System.out.println(new Gson().toJson(userVo));*/
        //用Feign声明式方式请求
        return this.archiveService.queryUserInfo(userDto);
    }
}
