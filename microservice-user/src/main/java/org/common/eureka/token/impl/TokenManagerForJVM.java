package org.common.eureka.token.impl;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.common.eureka.token.ITokenManager;
import org.springframework.stereotype.Component;

/**
 * 将token设置到Redis中，并设置其缓存过期时间为10分钟
 * @author samphin
 * @date 2019-3-26 14:02:50
 */
@Component
public class TokenManagerForJVM implements ITokenManager {

	/** 将token存储到JVM内存(ConcurrentHashMap)中   (@author: rico) */      
    private static Map<String, String> tokenMap = new ConcurrentHashMap<String, String>();

    /** 
     * @description 利用UUID创建Token(用户登录时，创建Token)
     * @author rico       
     * @created 2017年7月4日 下午4:46:46      
     * @param username
     * @return     
     */  
    public String createToken(String username) {
    	String token = UUID.randomUUID().toString().replace("-","");
        tokenMap.put(token, username);
        return token;
    }


    /** 
     * @description Token验证(用户登录验证)
     * @author rico       
     * @created 2017年7月4日 下午4:46:50      
     * @param token
     * @return     
     */  
    public boolean checkToken(String token) {
        return !StringUtils.isEmpty(token) && tokenMap.containsKey(token);
    }


    /** 
     * @description Token删除(用户登出时，删除Token)
     * @author rico       
     * @created 2017年7月4日 下午4:46:54      
     * @param token     
     */  
    @Override
    public void deleteToken(String token) {
        // TODO Auto-generated method stub
        tokenMap.remove(token);
    }
}
