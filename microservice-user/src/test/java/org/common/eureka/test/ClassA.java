package org.common.eureka.test;

public class ClassA {

	public ClassA() {
		System.out.println("我是构造方法：ClassA");
	}
	{System.out.println("我是代码块：ClassA");}
	static {
		System.out.println("我是静态代码块：ClassA");
	}
}
