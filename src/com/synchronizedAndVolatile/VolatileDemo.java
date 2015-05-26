package com.synchronizedAndVolatile;


/*
 * volatile是第二种Java多线程同步的机制，根据JLS(Java LanguageSpecifications)的说法，一个变量可以被volatile修饰，
 * 在这种情况下内存模型(主内存和线程工作内存)确保所有线程可以看到一致的变量值
 * 
 * one方法和two方法还会并发的去执行，但是加上volatile可以将共享变量i和j的改变直接响应到主内存中，
 * 这样保证了主内存中i和j的值一致性，然而在执行two方法时，在two方法获取到i的值和获取到j的值中间的这段时间，
 * one方法也许被执行了好多次，导致j的值会大于i的值。
 * 所以volatile可以保证内存可见性，不能保证并发有序性。
 * */

public class VolatileDemo {

	public static void main(String[] args) {
		Test1.one();
		Test1.two();

	}

}


class Test1{
	  static int i = 0, j = 0;  
	    static void one() {  
	        i++;  
	        j++;  
	    }  
	    static void two() {  
	        System.out.println("i=" + i + " j=" + j);  
	    }  
}

class Test2{
	 static int i = 0, j = 0;  
	    static synchronized void one() {  
	        i++;  
	        j++;  
	    }  
	    static synchronized void two() {  
	        System.out.println("i=" + i + " j=" + j);  
	    }  
}

class Test3{
	 static volatile int i = 0, j = 0;  
	    static void one() {  
	        i++;  
	        j++;  
	    }  
	    static void two() {  
	        System.out.println("i=" + i + " j=" + j);  
	    }  
}
