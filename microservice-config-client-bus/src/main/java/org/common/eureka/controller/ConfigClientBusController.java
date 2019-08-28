package org.common.eureka.controller;

import org.common.eureka.config.InfoConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ConfigClientBusController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${eureka.client.service-url.defaultZone}")
    private String eurekaServers;

    @Resource
    private InfoConfig infoConfig;

    @GetMapping("/config")
    public String getConfig() {
        return "ApplicationName = " + this.applicationName +"、EurekaServers = " +
                this.eurekaServers + "、infos = " + infoConfig.toString();
    }
}
