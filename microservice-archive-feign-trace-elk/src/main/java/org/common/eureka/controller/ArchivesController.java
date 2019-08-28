package org.common.eureka.controller;

import org.common.eureka.interfaces.UserFeignClient;
import org.common.eureka.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArchivesController {

    private final Logger logger = LoggerFactory.getLogger(ArchivesController.class);

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @GetMapping("/user/json/{id}")
    public String queryJsonById(@PathVariable(value = "id") String id) {

        //用Feign声明式方式请求
        String userJson = this.userFeignClient.queryJsonById(id);

        return userJson;
    }

    @GetMapping("/user/{id}")
    public UserVo queryOne(@PathVariable(value = "id") String id) {
        UserVo vo = this.userFeignClient.queryOne(id);
        return vo;
    }

    @GetMapping("/user/all")
    public String queryAll() {
        return this.userFeignClient.queryAll();
    }

    @GetMapping("/user/name/{userName}")
    public String queryJsonByName(@PathVariable(value = "userName") String userName) {
        return this.userFeignClient.queryJsonByName(userName);
    }

    /**
     * 打印负载均衡日志
     */
    @GetMapping("/log-instance")
    public void logArchiverInstance() {
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("microservice-user");
        //打印当前选择的是哪个节点
        logger.info("{}:{}:{}", serviceInstance.getServiceId(), serviceInstance.getHost(), serviceInstance.getPort());
    }
}
