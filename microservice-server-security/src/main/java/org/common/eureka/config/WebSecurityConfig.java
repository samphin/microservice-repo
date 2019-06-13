package org.common.eureka.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 通过配置类实现服务验证
 * eureka开启验证后无法连接注册中心?
 * spring Cloud 2.0 以上）的security默认启用了csrf检验，要在eurekaServer端配置security的csrf检验为false
 * @author samphin
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/*
	* springboot2.0以上整合security
	* 报There is no PasswordEncoder mapped for the id "null" || Encoded password d*/
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		//允许那个用户用什么权限的人登录  使用bcrypt加密方式
		auth.inMemoryAuthentication().
		//添加加密方式
		passwordEncoder(new BCryptPasswordEncoder()).withUser("samphin")
		//对密码进行加密
		.password(new BCryptPasswordEncoder().encode("samphin123")).roles("ADMIN").
		and().withUser("admin").password("admin123").roles("ADMIN", "USER");
	}
	
	//等价于
	/*@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.inMemoryAuthentication().withUser("samphin").password("samphin123").roles("USER").and().withUser("admin").password("admin123").roles("ADMIN","USER");
	}*/

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//springboot2.0默认启用csrf校验
		http.csrf().disable();
		super.configure(http);
//		http.formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/welcome").permitAll().and()
//				.authorizeRequests().anyRequest().authenticated()
//				.antMatchers("/login.html", "/**/**.css", "/images/**", "**/**.js", "/index").permitAll()// 解决静态资源被拦截的问题
//				.and().logout().permitAll();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/login.html", "/**/**.css", "/**/images/**", "**/**.js", "/welcome");
	}
}
