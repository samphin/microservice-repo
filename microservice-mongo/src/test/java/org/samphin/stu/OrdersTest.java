package org.samphin.stu;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.samphin.stu.po.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 通地聚合管道来对订单里面的信息进行统计操作。
 */
@SpringBootTest(classes = MongoApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class OrdersTest {

	Logger logger = LoggerFactory.getLogger(OrdersTest.class);

	@Autowired
	private MongoTemplate mongoTemplate;
	
	/**
	 * 重命名谋个字段名称
	 */
	@Test
	public void renameFieldName() {
		Query query = new Query();
		Update update = new Update().rename("auditors1", "auditors");
		this.mongoTemplate.updateMulti(query, update, Order.class);
	}

	/**
	 * 查询2015年4月3日之前，每个用户每个月消费的总金额，并按用户名进行排序。
	 */
	@Test
	public void statistics1() {
		try {
			Criteria criteria = Criteria.where("orderTime").lt(DateUtils.parseDate("2015-04-03", "yyyy-MM-dd"));
			Aggregation aggregation = Aggregation.newAggregation(
					Aggregation.match(criteria),
					Aggregation.group("useCode","price","orderTime","auditors").sum("price").as("年度customerPrice"),
					Aggregation.sort(Direction.ASC, "useCode"),
					Aggregation.skip(0L),//从第0条开始
					Aggregation.limit(20L),//取20条
					Aggregation.project("useCode","price","orderTime","auditors")//投影出指定输出的字段
					);
			/*
			 * 第一种返回格式：用BasicDBObject
			 * AggregationResults<BasicDBObject> results = mongoTemplate.aggregate(aggregation,"orders",BasicDBObject.class);
			for(Iterator<BasicDBObject> outResult = results.iterator();outResult.hasNext();) {
				DBObject obj = outResult.next();
			    System.out.println("聚合结果："+JSON.toJSONString(obj));
			}*/
			/**
			 * 第二种方式指定POJO类，POJO类字段必须与MongoDB数据库中已存储的文档字段保持一致，不然会报错。
			 */
			AggregationResults<Order> results = mongoTemplate.aggregate(aggregation,"orders",Order.class);
			for(Iterator<Order> outResult = results.iterator();outResult.hasNext();) {
				Order obj = outResult.next();
			    System.out.println("聚合结果："+JSON.toJSONString(obj));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询2015年4月3号之前，每个审核员分别审批的订单总金额，按审核员名称进行排序
	 * 查询思路：
	 * 1、通过时间进行过滤，查询出2015-4-3日前的数据
	 * 2、再通过unwind对数组里面的审核员进行分拆据
	 * 3、将分拆好的数据按照auditors进行分组
	 * 4、对分组中管理员涉及到的金额字段进行求和
	 * 5、最后根据审批员名称进行排序
	 */
	@Test
	public void statistics2() {
		/*Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -4);
		c.add(Calendar.DATE, -3);*/
		//DateUtils.parseDate("2015-04-03 00:00:00", "yyyy-MM-dd HH:mm:ss")
		Criteria criteria = Criteria.where("orderTime").lt(dateToISODate("2015-04-03 00:00:00"));
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(criteria),
				Aggregation.unwind("auditors"),
				Aggregation.group("auditors").sum("price").as("price"),
				Aggregation.project("price").and("auditors").previousOperation(),
				Aggregation.sort(Direction.ASC, "auditors")
				);//投影出指定输出的字段));
		/**
		 * 第二种方式指定POJO类，POJO类字段必须与MongoDB数据库中已存储的文档字段保持一致，不然会报错。
		 */
		AggregationResults<Order> results = mongoTemplate.aggregate(aggregation,"orders",Order.class);
		for(Iterator<Order> outResult = results.iterator();outResult.hasNext();) {
			Order obj = outResult.next();
		    System.out.println("聚合结果："+JSON.toJSONString(obj));
		}
	}
	
	
	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -4);
		c.add(Calendar.DATE, -3);
		System.out.println(DateFormatUtils.format(c, "yyyy-MM-dd"));
	}
	
	
	/**日期转化
	 * mongo 日期查询isodate
	 * @param dateStr
	 * @return
	 */
	public Date dateToISODate(String dateStr){
	    try {
	    	//T代表后面跟着时间，Z代表UTC统一时间
		    Date date = DateUtils.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		    format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
		    String isoDate =   format.format(date);
	        return format.parse(isoDate);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}
