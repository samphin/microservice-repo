package org.common.eureka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
/**
 * 自动刷新Git中修改的配置文件内容，配置服务不重启需要通过/actuator/bus-refresh/刷新
 * http://localhost:8166/actuator/bus-refresh
 */
@RefreshScope
public class InfoConfig {

    //获取Git仓库配置文件中的profile属性，而不是获取当前bootstrap中的profile配置spring.cloud.config.profile的值
    @Value("${info.app.name}")
    private String appName;

    @Value("${info.company.name}")
    private String companyName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "InfoConfig{" +
                "appName='" + appName + '\'' +
                ", companyName= '" + companyName + '\'' +
                "}";
    }
}
