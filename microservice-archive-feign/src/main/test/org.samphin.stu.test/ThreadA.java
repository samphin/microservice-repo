package org.samphin.stu.test;

public class ThreadA extends Thread {
	private int count;
	
	public ThreadA(){
		
	}
	
	public ThreadA(int count){
		this.count = count;
	}	

	@Override
	public void run() {
		System.out.println("ThreadA执行的顺序号"+count+"......");
	}
}
