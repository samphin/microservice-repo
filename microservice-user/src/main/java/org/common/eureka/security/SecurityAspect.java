package org.common.eureka.security;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.common.eureka.annotation.IgnoreSecurity;
import org.common.eureka.exception.TokenException;
import org.common.eureka.token.ITokenManager;
import org.common.eureka.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class SecurityAspect {

	/** Log4j日志处理 */
	private static final Logger log = Logger.getLogger(SecurityAspect.class);

	@Autowired
	private ITokenManager tokenManager;

	/**
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public Object execute(ProceedingJoinPoint pjp) throws Throwable {
		// 从切点上获取目标方法
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		log.debug("methodSignature : " + methodSignature);
		Method method = methodSignature.getMethod();
		log.debug("Method : " + method.getName() + " : " + method.isAnnotationPresent(IgnoreSecurity.class));
		// 若目标方法忽略了安全性检查,则直接调用目标方法
		if (method.isAnnotationPresent(IgnoreSecurity.class)) {
			return pjp.proceed();
		}
		// 从 request header 中获取当前 token
		String token = getRequest().getHeader(Constants.DEFAULT_TOKEN_NAME);
		// 检查 token 有效性
		if (!tokenManager.checkToken(token)) {
			String message = String.format("token [%s] is invalid", token);
			log.debug("message : " + message);
			throw new TokenException(message);
		}
		// 调用目标方法
		return pjp.proceed();
	}

	/**
	 * @description 获取HTTP请求
	 * @author samphin
	 * @created 2019-3-26 14:30:55
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return request;
	}
}
