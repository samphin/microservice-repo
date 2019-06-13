package org.common.eureka.token.impl;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.common.eureka.service.impl.RedisService;
import org.common.eureka.token.ITokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  默认将token设置到Redis中，并设置其缓存过期时间为10分钟，
 *  为以后系统用户量增大时，可能采用分布式部署作准备
 * @author samphin
 * @date 2019-3-26 14:02:50
 */
@Component
public class DefaultTokenManager implements ITokenManager {

	@Autowired
	private RedisService redisService;

    /** 
     * @description 利用UUID创建Token(用户登录时，创建Token)
     * @author samphin       
     * @created 2019-3-26 14:08:12      
     * @param username
     * @return     
     */  
    public String createToken(String username) {
        String token = UUID.randomUUID().toString().replace("-","");
        //设置token，默认缓存过期时间为10分钟
        redisService.set(token, username,1*60*10L);
        return token;
    }


    /** 
     * @description Token验证(用户登录验证)
     * @author samphin    
     * @created 2019-3-26 14:08:12      
     * @param token
     * @return     
     */  
    public boolean checkToken(String token) {
        return !StringUtils.isEmpty(token) && redisService.exists(token);
    }

    /** 
     * @description Token删除(用户登出时，删除Token)
     * @author samphin       
     * @created 2019-3-26 14:08:12      
     * @param token     
     */  
    @Override
    public void deleteToken(String token) {
    	redisService.remove(token);
    }
}
