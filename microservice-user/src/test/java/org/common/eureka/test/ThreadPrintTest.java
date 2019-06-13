package org.common.eureka.test;

/**
 * 多线程测试用例
 * @author samphin
 */
public class ThreadPrintTest implements Runnable{
	
	@Override
	public void run() {
		try {
			long time1 = System.currentTimeMillis();
			Thread.currentThread().sleep(100);
			long time2 = System.currentTimeMillis();
			System.out.println("又可以打印了"+(time2-time1));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 线程安全测试
	 */
	public static void main(String[] args) {
		while(true) {
			ThreadPrintTest t = new ThreadPrintTest();
			Thread t1 = new Thread(t);
			t1.start();
		}
	}
}
