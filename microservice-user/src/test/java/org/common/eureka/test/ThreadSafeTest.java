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
public class ThreadSafeTest implements Runnable{
	
	//模拟火车票出票情况，假设火车票只剩10张
	private int num = 10;
	@Override
	public void run() {
		//线程不安全的做法，火车票出票时，可能出现负数，这是不正常的。
		/*while(true) {
			if(num>0) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("火车票还剩"+num--+"张~");
			}
		}*/
		//下面使用同步机制来实现线程安全
		while(true) {
			/*将同步机制设置在操作对象上
			 * synchronized("") {
				if(num>0) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("火车票还剩"+num--+"张~");
				}
			}*/
			
			//将同步机制设置在方法级别
			doit();
		}
	}
	
	//使用同步机制的另外一种办法，也就是扩大线程安全粒度，将sychronized设置在方法上面
	private synchronized void doit() {
		if(num>0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("火车票还剩"+num--+"张~");
		}
	}
	
	/**
	 * 线程安全测试
	 */
	public static void main(String[] args) {
		ThreadSafeTest t = new ThreadSafeTest();
		Thread t1 = new Thread(t);
		Thread t2 = new Thread(t);
		Thread t3 = new Thread(t);
		Thread t4 = new Thread(t);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
	}
}
