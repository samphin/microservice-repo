package org.common.eureka.test;

/**
 * 类加载顺序测试
 */
public class ClassLoaderOrderTest{
	
	public static void main(String[] args) {
		ClassA classA = new ClassA();
		System.out.println("****************************");
		ClassA classB = new ClassB();
	}
}
