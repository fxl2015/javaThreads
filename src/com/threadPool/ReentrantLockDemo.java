package com.threadPool;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

	public static void main(String[] args) {
		
		Info info=new Info();
		Producer producer1=new Producer(info);
		Producer producer2=new Producer(info);
		Consumer consumer=new Consumer(info);
		
		new Thread(producer1).start();
		new Thread(producer2).start();
		
		new Thread(consumer).start();
		
	}
}

class Info{
	int i=0;
	ReentrantLock lock=new ReentrantLock();
	Condition condition=lock.newCondition();
	
	public void getInfo(){
		try {
		lock.lock();
		if (i<=0) {
			try {
				condition.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Info.getInfo()**********"+i);
        i--;
		condition.signal();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			lock.unlock();
		}
		
	}
	
	public void putInfo(String info){
		try {	
		lock.lock();
		if (i>4) {
			try {
				condition.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Info.putInfo()***"+i);
		i++;
		condition.signal();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			lock.unlock();
		}
		
	}
}

class Producer implements Runnable{

	Info info;
	int i;
	public Producer(Info info){
		this.info=info;
	}
	@Override
	public void run() {
		while (true) {
			info.putInfo("info***"+i);
			i++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
}



class Consumer implements Runnable{
	
	Info info;
	public Consumer(Info info){
		this.info=info;
	}

	@Override
	public void run() {
		while (true) {

			info.getInfo();
			try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}