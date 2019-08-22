package org.common.eureka.interfaces.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import feign.hystrix.FallbackFactory;
import org.common.eureka.interfaces.UserFeignClient;
import org.common.eureka.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 回退类，专为Feign接口提供回退原因,快速熔断
 * 防止雪崩效应，服务提供者因网络故障或找不到，快速熔断
 *
 * @author samphin
 */
@Component
public class FeignClientFallbackFactory implements FallbackFactory<UserFeignClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeignClientFallbackFactory.class);

    @Override
    public UserFeignClient create(Throwable cause) {
        System.out.println("熔断原因 = " + cause);
        return new UserFeignClient() {
            @Override
            public String queryJsonById(String id) {
                UserVo userVo = new UserVo();
                if (cause instanceof IllegalArgumentException) {
                    userVo.setUserId("-1");
                } else {
                    userVo.setUserId("-2");
                }
                userVo.setUserName("administrator");
                return JSONObject.toJSONString(userVo);
            }

            @Override
            public UserVo queryOne(String id) {
                //日志最好放在各个fallback方法中，而不要直接放在create方法中。
                //否则在引用启动时，就会打印该日志
                FeignClientFallbackFactory.LOGGER.info("fallback；reason was:" + cause);
                UserVo userVo = new UserVo();
                userVo.setUserId("-1");
                userVo.setUserName("administrator");
                return userVo;
            }

            @Override
            public String queryAll() {
                //日志最好放在各个fallback方法中，而不要直接放在create方法中。
                //否则在引用启动时，就会打印该日志
                FeignClientFallbackFactory.LOGGER.info("fallback；reason was:" + cause);
                UserVo userVo = new UserVo();
                userVo.setUserId("-1");
                userVo.setUserName("administrator");
                return JSONArray.toJSONString(userVo);
            }

            @Override
            public String queryJsonByName(String userName) {
                //日志最好放在各个fallback方法中，而不要直接放在create方法中。
                //否则在引用启动时，就会打印该日志
                FeignClientFallbackFactory.LOGGER.info("fallback；reason was:" + cause);
                UserVo userVo = new UserVo();
                if (cause instanceof IllegalArgumentException) {
                    userVo.setUserId("-1");
                } else {
                    userVo.setUserId("-2");
                }
                userVo.setUserName("administrator");
                return JSONObject.toJSONString(userVo);
            }
        };
    }

}
