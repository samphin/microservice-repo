package org.samphin.stu.test;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class JvmTest {

    public static void main(String[] args) {
        //栈的演示，FILO，先进后出规则
        Stack<String> stack = new Stack<String>();
        stack.push("samphin");
        stack.push("james");
        stack.push("wade");
        stack.push("kobe");
        System.out.println("栈大小：" + stack.size());
        for (int i = 1; i <= stack.size(); i++) {
            System.out.println(stack.pop());
        }
		
		/*堆  新生代、永久代、元空间
		
		新生代：永久代 = 1:2
		
		新生代=Eden+from+to   8:1:1
		
		比如创建一个对象时，把eden空间占满了，下次再来一个新对象需要内存空间时，就会触发一次GC
		为新对象分配内存空间

		新生代：指新创建的对象   mijorGC
		
		老年代：就是长期存活在内存中对象 majorGC
		
		永久代：方法区 fullGC*/
        System.out.println("-----------------------------集合删除操作~");
        List<Person> list = new ArrayList<Person>();
        list.add(new Person("1001", "samphin"));
        list.add(new Person("1002", "james"));
        list.add(new Person("1003", "wade"));
        list.add(new Person("1004", "kobe"));
        System.out.println("删除元素前:" + JSONArray.toJSONString(list));
        Iterator<Person> it = list.iterator();
        while (it.hasNext()) {
            Person item = it.next();
            if (item.getName().equals("wade")) {
                it.remove();
            }
        }
        System.out.println("删除元素后:" + JSONArray.toJSONString(list));
    }

}
