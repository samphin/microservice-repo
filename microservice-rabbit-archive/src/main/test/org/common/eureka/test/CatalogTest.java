package org.common.eureka.test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.common.eureka.RabbitArchiveApplication;
import org.common.eureka.entity.DwCatalog;
import org.common.eureka.service.IDwCatalogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 目录测试用例
 * @author samphin
 */
@SpringBootTest(classes=RabbitArchiveApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CatalogTest {
	@Autowired
	private IDwCatalogService catalogService;
	
	//最大并发数
	private final int MAX_NUM = 10;
	
	//实例化一个并发测试对象
	private CountDownLatch cdl = new CountDownLatch(MAX_NUM); 

	//通过线程池来创建线程
	ExecutorService es = Executors.newCachedThreadPool();
	
	private Logger logger = Logger.getLogger(CatalogTest.class);
	
	//并发压力测试
	@Test
	public void queryCatalogInfo() {
		for(int i = 0; i < MAX_NUM; i++){
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						cdl.await();
						String userId = "3ce7d3cb-b248-438a-82df-4f6e7e4828ea";
						Map<String,Object> params = new HashMap<String,Object>();
						params.put("createUserId", userId);
						List<DwCatalog> catalogs = catalogService.queryAll(params);
						for (DwCatalog dwCatalog : catalogs) {
							System.out.println("目录名称："+dwCatalog.getName()+"----目录ID："+dwCatalog.getId());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			//通过线程池来创建线程，并提交线程到排队状态
			es.submit(thread);
			cdl.countDown();
		}
		try {
			Thread.currentThread();
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
