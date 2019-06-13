package org.common.eureka.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages={"org.common.eureka.dao"}, sqlSessionFactoryRef = "sqlSessionFactoryForPrimary")
public class MyBatisConfig {
	
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	public JdbcTemplate getJdbcTemplate() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(primaryDataSource());
		return jdbcTemplate;
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactoryForPrimary() throws Exception {
		SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
		ssfb.setDataSource(primaryDataSource());
		//加入扫描Mapper.xml文件
		 PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		 ssfb.setMapperLocations(resolver.getResources("classpath*:MybatisMapperXml/*Mapper.xml"));
		return ssfb.getObject();
	}	
	
	@Bean
    public DataSourceTransactionManager transactionManagerForPrimary() {  
        return new DataSourceTransactionManager(primaryDataSource());  
    } 
}
