package org.common.eureka.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.common.eureka.intercepter.MyInterceptor;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
//import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan(basePackages = "org.common.eureka")
public class WebMvcConfig extends WebMvcConfigurationSupport {
	@Value("${server.port}")
	private String port;
	@Value("${server.context-path}")
	private String contextPath;
	private Set<ErrorPage> pageHandlers;

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	/**
	 * 视图设置
	 */
	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	/**
	 * 扩展已至此上传进度监控
	 */
	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxInMemorySize(4096);
		// 最大上传文件限制为5G
		resolver.setMaxUploadSize(5 * 1024 * 1024);
		resolver.setDefaultEncoding("UTF-8");
		return resolver;
	}

	/**
	 * 解决http请求跨域问题
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		//允许哪些域访问
		String[] allowedOrigins = {"http://localhost:9001","http://localhost:9002","http://localhost:9003"};
		registry.addMapping("/*").
		allowedOrigins(allowedOrigins).
		allowCredentials(true).
		allowedHeaders("Content-Type,X-Token").
		allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").maxAge(3600);//访问超时时间：一小时
	}
	
	private CorsConfiguration addcorsConfig() {
	    CorsConfiguration corsConfiguration = new CorsConfiguration();
	    List<String> list = new ArrayList<String>();
	    list.add("*");
	    corsConfiguration.setAllowedOrigins(list);
	    /*
	    // 请求常用的三种配置，*代表允许所有，当时你也可以自定义属性（比如header只能带什么，只能是post方式等等）
	    */
	    corsConfiguration.addAllowedOrigin("*"); 
	    corsConfiguration.addAllowedHeader("*"); 
	    corsConfiguration.addAllowedMethod("*"); 
	    return corsConfiguration;
	  }

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", addcorsConfig());
		return new CorsFilter(source);
	}

	/**
	 * mvc传送验证并转换数据类型
	 */
	@Bean
	public RequestMappingHandlerAdapter handlerAdapter() {
		RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
		// 自定义转换类型（数据）
		adapter.setWebBindingInitializer(new DataBingding());
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		list.add(new ByteArrayHttpMessageConverter());
		StringHttpMessageConverter shm = new StringHttpMessageConverter();
		shm.setWriteAcceptCharset(false);
		list.add(shm);
		list.add(new AllEncompassingFormHttpMessageConverter());
		// IE9,10,11浏览器响应浏览器时返回的Content-Type为application/json,不支持,每次返回下载json数据包
		MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		Map<String, String> mapSet = new HashMap<String, String>();
		mapSet.put("charset", "UTF-8");
		MediaType mediaType = new MediaType("text", "html", mapSet);
		supportedMediaTypes.add(mediaType);
		jackson2HttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
		list.add(jackson2HttpMessageConverter);
		// 自定义消息转换类
		adapter.setMessageConverters(list);
		return adapter;
	}

	@PostConstruct
	private void init() {
		pageHandlers = new HashSet<ErrorPage>();
		pageHandlers.add(new ErrorPage(HttpStatus.NOT_FOUND, "/errorPage.html"));
		pageHandlers.add(new ErrorPage(HttpStatus.FORBIDDEN, "/forbidden.html"));
	}

	/*@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
		factory.setPort(Integer.valueOf(port));
		factory.setContextPath(contextPath);
		factory.setErrorPages(pageHandlers);
		return factory;
	}*/

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new MyInterceptor()).addPathPatterns("/asd/**");
	}

}
