package org.common.eureka.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages={"org.common.eureka.mapper"}, sqlSessionFactoryRef = "sqlSessionFactory")
public class MyBatisConfig {

	//这个注解读取配置文件前缀为prefix的配置，将外部的配置文件与这里绑定
	@ConfigurationProperties(prefix = "spring.druid")
	//容器的开启与关闭
	@Bean(initMethod = "init", destroyMethod = "close")
	public DruidDataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setProxyFilters(Lists.newArrayList(statFilter()));
		return dataSource;
	}

	//bean注解，成为spring的bean，利用filter将慢sql的日志打印出来
	@Bean
	public Filter statFilter() {
		StatFilter statFilter = new StatFilter();
		//多长时间定义为慢sql，这里定义为5s
		statFilter.setSlowSqlMillis(5000);
		//是否打印出慢日志
		statFilter.setLogSlowSql(true);
		//是否将日志合并起来
		statFilter.setMergeSql(true);
		return statFilter;
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource());
		//加入扫描Mapper.xml文件
		sessionFactoryBean.setTypeAliasesPackage("org.common.eureka.entity");
		// 配置mapper的扫描，找到所有的mapper.xml映射文件
		Resource[] resources = new PathMatchingResourcePatternResolver()
				.getResources("classpath:mappers/*.xml");
		sessionFactoryBean.setMapperLocations(resources);
		// 加载全局的配置文件
		sessionFactoryBean.setConfigLocation(
				new DefaultResourceLoader().
						getResource("classpath:mybatis-config.xml"));
		return sessionFactoryBean.getObject();
	}	
	
	@Bean
    public DataSourceTransactionManager transactionManagerForPrimary() {  
        return new DataSourceTransactionManager(dataSource());
    } 
}
