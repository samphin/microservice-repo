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

    @GetMapping(value = "/user/id/{id}")
    public UserVo queryOne(@PathVariable String id) {
        //默认的方式请求
		/*UserVo userVo = restTemplate.getForObject("http://127.0.0.1:8788/"+id, UserVo.class);
		System.out.println(new Gson().toJson(userVo));*/
        //用Feign声明式方式请求
        UserVo user = this.userFeignClient.queryOne(id);

        return user;
    }

    @GetMapping(value = "/user/name/{userName}")
    public String queryJsonByName(@PathVariable String userName) {
        //用Feign声明式方式请求
        return this.userFeignClient.queryJsonByName("'%" + userName + "%'");
    }

    @GetMapping(value = "/user/all")
    public String queryAll() {
        return this.userFeignClient.queryAll();
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
