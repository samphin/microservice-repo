package org.common.eureka.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

  
/**        
 * Title:自定义注解     
 * Description: 标识是否忽略REST安全性检查
 * @author samphin       
 * @created 2019-3-26 14:23:54    
 */      
@Target(ElementType.METHOD) 
@Retention(RetentionPolicy.RUNTIME) 
@Documented
public @interface IgnoreSecurity {

}
