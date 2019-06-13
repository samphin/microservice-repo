package org.common.eureka.controller;

import com.alibaba.fastjson.JSONObject;
import org.common.eureka.dto.UserDto;
import org.common.eureka.service.ArchiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController("/archives")
public class ArchivesFeignController {

    private final Logger logger = LoggerFactory.getLogger(ArchivesFeignController.class);

    //用RESTFUL请求接口
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private ArchiveService archiveService;

    //获取用户信息
    @GetMapping("/user_info")
    public JSONObject queryUserInfo(@ModelAttribute UserDto userDto) {
        //默认的方式请求
		/*UserVo userVo = restTemplate.getForObject("http://127.0.0.1:8788/"+id, UserVo.class);
		System.out.println(new Gson().toJson(userVo));*/
        //用Feign声明式方式请求
        return this.archiveService.queryUserInfo(userDto);
    }

    /**
     * 打印负载均衡日志
     */
    @GetMapping("/log-instance")
    public void logArchiveInstance() {
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("microservice-user");
        //打印当前选择的是哪个节点
        logger.info("{}:{}:{}", serviceInstance.getServiceId(), serviceInstance.getHost(), serviceInstance.getPort());
    }
}
