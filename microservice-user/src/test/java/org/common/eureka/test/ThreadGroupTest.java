package org.common.eureka.test;

import javax.swing.table.DefaultTableModel;

/**
 * 线程组测试用例
 * @author samphin
 */
public class ThreadGroupTest{
	
	/**
	 * 线程安全测试
	 */
	public static void main(String[] args) {
		//获取当前线程所有的线程组
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		//使用线程数据保存所有活动状态的线程
		Thread[] threads = new Thread[group.activeCount()];
		//获得所有线程
		group.enumerate(threads);
		for (Thread thread : threads) {
			System.out.println("线程ID："+thread.getId()+"---，线程名称："+thread.getName());
		}
	}
}
