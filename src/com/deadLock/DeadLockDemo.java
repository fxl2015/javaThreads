package com.deadLock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeadLockDemo {

	public static void main(String[] args) {
		
	//	test();
		ExecutorService ee=Executors.newCachedThreadPool();
        ee.submit(new Runnable() {
			
			@Override
			public void run() {
				System.out
						.println("DeadLockDemo.main(...).new Runnable() {...}.run()");
			}
		});
        
	}

	private static void test() {
		final AB ab=new AB();
		
		Runnable a=new Runnable() {
			
			@Override
			public void run() {
				ab.getB();
			}
		};
		
		
		Runnable b=new Runnable() {
			
			@Override
			public void run() {
				ab.getA();
			}
		};
		
		
		Thread aThread=new Thread(a,"线程A");
		Thread bThread=new Thread(b,"线程B");
		aThread.start();
		bThread.start();
	}

}

class AB extends Object{
	final Object A=new Object();
	final Object B=new Object();
	
	public  void getB(){
		synchronized (A) {
			System.out.println(Thread.currentThread().getName()+"**********i want to get B");
			synchronized (B) {
				System.out.println("get B");
			}
		}
	}
	
	public  void getA(){
		synchronized (B) {
			System.out.println(Thread.currentThread().getName()+"***i want to get A");
			synchronized (A) {
				System.out.println("get A");
			}
		}
	}
}