package org.common.eureka.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
public class ArchivesRibbonController {

    private final Logger logger = LoggerFactory.getLogger(ArchivesRibbonController.class);

    //声明指定服务名称》》》记得要与application.yml文件中指定负载均衡策略的服务名称保持一致
    private final static String USER_TOPIC = "microservice-user";

    //用RESTFUL请求接口
    @Autowired
    private RestTemplate restTemplate;
    //可通过负载均衡策略对象，获取服务提供方信息
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/users")
    public String queryUserList() {
        //根据传入的服务名microservice-id，从负载均衡器中挑选一个对应服务的实例
        ServiceInstance serviceInstance = this.loadBalancerClient.choose(USER_TOPIC);
        //打印当前选择的是哪个节点
        logger.info("{}:{}:{}", serviceInstance.getServiceId(), serviceInstance.getHost(), serviceInstance.getPort());

        URI uri = URI.create(String.format("http://%s:%s/findAll", serviceInstance.getHost(), serviceInstance.getPort()));
        //常用的访问方式
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(uri, String.class);
        String users = responseEntity.getBody();
        System.out.println(users);
        return users;
    }

    /**
     * 打印负载均衡日志
     */
    @GetMapping("/log-instance")
    public void logUserInstance() {
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("microservice-user");
        //打印当前选择的是哪个节点
        logger.info("{}:{}:{}", serviceInstance.getServiceId(), serviceInstance.getHost(), serviceInstance.getPort());
    }
}
