package org.samphin.stu.test;

public class ThreadB extends Thread {

	private int count;
	
	public ThreadB(){
		
	}
	
	public ThreadB(int count){
		this.count = count;
	}	

	@Override
	public void run() {
		System.out.println("ThreadB执行的顺序号"+count+"......");
	}
}
