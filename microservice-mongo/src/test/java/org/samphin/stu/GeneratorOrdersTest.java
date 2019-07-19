package org.samphin.stu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.samphin.stu.po.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 随机生成10万条订单信息
 */
@SpringBootTest(classes = MongoApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class GeneratorOrdersTest {

	Logger logger = LoggerFactory.getLogger(GeneratorOrdersTest.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	// 随机生成orderTest数据
	@Test
	public void batchInsertOrder() {
		mongoTemplate.dropCollection("orders");
		String[] userCodes = new String[] { "james", "kobe", "allen", "wade", "peter", "mark", "king", "zero", "lance",
				"deer", "lison" };
		String[] auditors = new String[] { "auditor1", "auditor2", "auditor3", "auditor4", "auditor5" };
		List<Order> list = new ArrayList<Order>();
		Random rand = new Random();
		for (int i = 0; i < 100000; i++) {
			Order order = new Order();
			int num = rand.nextInt(11);
			order.setUseCode(userCodes[num]);
			order.setOrderCode(UUID.randomUUID().toString());
			order.setOrderTime(RondomDateTest.randomDate("2015-01-01", "2017-10-31"));
			order.setPrice(RondomDateTest.randomBigDecimal(10000, 1));
			int length = rand.nextInt(5) + 1;
			String[] temp = new String[length];
			for (int j = 0; j < temp.length; j++) {
				temp[j] = getFromArrays(temp, auditors, rand);
			}
			order.setAuditors(temp);
			list.add(order);
		}
		mongoTemplate.insertAll(list);
	}

	private String getFromArrays(String[] temp, String[] auditors, Random rand) {
		String ret = null;
		boolean test = true;
		while (test) {
			ret = auditors[rand.nextInt(5)];
			int i = 0;
			for (String _temp : temp) {
				i++;
				if (ret.equals(_temp)) {
					break;
				}
			}
			if (i == temp.length) {
				test = false;
			}

		}
		return ret;

	}
}
