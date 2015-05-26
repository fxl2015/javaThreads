package com.synchronizedAndVolatile;


/*
 * 1.使用synchronized将需要互斥的代码包含起来，并上一把锁
 * 2.将synchronized加在需要互斥的方法上
 * 使用synchronized修饰的方法或者代码块可以看成是一个原子操作
 * 一个线程执行互斥代码过程如下：
 *       1. 获得同步锁；
 *       2. 清空工作内存；
 *       3. 从主内存拷贝对象副本到工作内存；
 *       4. 执行代码(计算或者输出等)；
 *       5. 刷新主内存数据；
 *       6. 释放同步锁。
 * 所以，synchronized既保证了多线程的并发有序性，又保证了多线程的内存可见性。
 * 
 * */

public class TraditionalThreadSynchronized {
	public static void main(String[] args) {
		final Outputter output = new Outputter();
		new Thread() {
			public void run() {
				output.output("1234");
			};
		}.start();		
		new Thread() {
			public void run() {
				output.output("abcd");
			};
		}.start();
	}
}
class Outputter {
	public void output(String name) {
		// TODO 为了保证对name的输出不是一个原子操作，这里逐个输出name的每个字符
		
		synchronized (this) {
			
		for(int i = 0; i < name.length(); i++) {
			System.out.print(name.charAt(i));
			 try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}}
}