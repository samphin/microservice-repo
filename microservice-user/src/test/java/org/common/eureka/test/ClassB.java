package org.common.eureka.test;

public class ClassB extends ClassA{

	public ClassB() {
		System.out.println("我是构造方法：ClassB");
	}
	{System.out.println("我是代码块：ClassB");}
	static {
		System.out.println("我是静态代码块：ClassB");
	}
}
