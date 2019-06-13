package org.sam.stu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude= {
		//DataSourceAutoConfiguration.class,
		//DataSourceTransactionManagerAutoConfiguration.class,
		//HibernateJpaAutoConfiguration.class,
		//MongoDataAutoConfiguration.class,
		//MongoAutoConfiguration.class
})
public class JtaApplication{

	public static void main(String[] args) {
		SpringApplication.run(JtaApplication.class, args);
	}
}
