package org.samphin.stu.config;

import com.mongodb.MongoClient;
import org.samphin.stu.convert.BigDecimalToDecimal128Converter;
import org.samphin.stu.convert.Decimal128ToBigDecimalConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB自定义配置
 *
 * @author samphin
 */
@Configuration
public class MongoDBConfig extends AbstractMongoConfiguration {

    @Bean
    public MongoProperties getMongoProperties() {
        MongoProperties mongoProperties = new MongoProperties();
        mongoProperties.setHost(host);
        mongoProperties.setPort(port);
        mongoProperties.setDatabase(getDatabaseName());
        mongoProperties.setUsername(username);
        mongoProperties.setPassword(password.toCharArray());
        return mongoProperties;
    }

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Override
    public MongoClient mongoClient() {
        MongoClient mongoClient = new MongoClient(host, port);
        return mongoClient;
    }

    @Bean
    public MongoDbFactory mongoDbFactory() {
        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new MongoClient(host, port),
                getDatabaseName());
        return mongoDbFactory;
    }

    /**
     * 自定义转换类
     * @return
     */
    @Bean
    @Override
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<Converter<?, ?>>();
        converterList.add(new BigDecimalToDecimal128Converter());
        converterList.add(new Decimal128ToBigDecimalConverter());
        return new MongoCustomConversions(converterList);
    }
}
