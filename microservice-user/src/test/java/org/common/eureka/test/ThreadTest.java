package org.common.eureka.test;
import java.util.concurrent.CountDownLatch;

import org.common.eureka.UserSystemApplication;
import org.common.eureka.entity.SecUser;
import org.common.eureka.service.ISecUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 多线程测试用例
 * @author samphin
 */
public class ThreadTest {
	//最大并发数
	private final int MAX_NUM = 40;
	
	//实例化一个并发测试对象
	private CountDownLatch cdl = new CountDownLatch(MAX_NUM); 

	//通过线程池来创建线程
	//ExecutorService es = Executors.newFixedThreadPool(MAX_NUM);
	
	//并发压力测试
	@Test
	public void findUserInfo() {
		for(int i = 0; i < MAX_NUM; i++){
			Thread newThread = new Thread(new Runnable() {
					@Override
					public void run() {
						System.out.println("大家好，我是新来的线程！");
					}
			});
			newThread.start();
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						newThread.join();
						cdl.await();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			cdl.countDown();
		}
		try {
			Thread.currentThread();
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 线程优先级测试
	 */
	@Test
	public void threadPriority() {
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("大家好，我是线程1！");
			}
		});
		t1.setPriority(10);
		
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("大家好，我是线程2！");
			}
		});
		t2.setPriority(8);
		
		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("大家好，我是线程3！");
			}
		});
		t3.setPriority(3);
		
		Thread t4 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("大家好，我是线程4！");
			}
		});
		t4.setPriority(6);
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		try {
			//让主线程休眠3秒，查看子线程优先级调用情况
			Thread.currentThread();
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
