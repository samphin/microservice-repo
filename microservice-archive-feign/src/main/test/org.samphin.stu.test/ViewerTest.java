package org.samphin.stu.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ViewerTest {

	@Test
	public void hashCodeTest() {
		//equals相同的值，hashcode一定相同，反之不会
		String x = "string";
		String y = "string";
		System.out.println(x.equals(y));
		System.out.println(x.hashCode() +","+ y.hashCode());
		
		String u = "通话";
		String v = "重地";
		System.out.println(u.hashCode()+","+v.hashCode());
		System.out.println(u.equals(v));
	}
	
	//多线程
	@Test
	public void threadTest(){
		Thread a = new ThreadA();
		Thread b = new ThreadB();
		//System.out.println("默认优先级：ThreadA="+a.getPriority()+",ThreadB="+b.getPriority());
		//设置线程优先级  
		//数字越大的优先级越高。但是，优先级高只是意味着该线程获取的 CPU时间片相对多一些。
		//并不是说是先让你走完。
		a.setPriority(1);
		b.setPriority(6);
		a.start();
		b.start();
	}
	
	//字符串反转
	@Test
	public void stringTest() {
		String str = "string";
		StringBuilder sb = new StringBuilder(str);
		sb = sb.reverse();
		System.out.println(sb.toString());
	}
	
	//如何将ArrayList序列化
	@Test
	public void ListTest() {
		try {
			List<Person> list = new ArrayList<Person>();
			list.add(new Person("1001","samphin"));
			list.add(new Person("1002","james"));
			FileOutputStream fs=new FileOutputStream("F:\\foo.txt");
			ObjectOutputStream os=new ObjectOutputStream(fs);
			os.writeObject(list);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
