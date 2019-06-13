package org.common.eureka.token;

/**        
 * Title: REST 鉴权   
 * Description: 登录用户的身份鉴权
 * @author samphine       
 * @created 2019-3-26 14:00:59   
 */  
public interface ITokenManager {

	String createToken(String username);  

    boolean checkToken(String token); 

    void deleteToken(String token);
}
