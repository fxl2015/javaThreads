package com.threadOrRunnable;

public class ThreadDemo {

	public static void main(String[] args) {
		
		new Sale1().start();
		new Sale1().start();
		new Sale1().start();

	}

}


class Sale1 extends Thread{
	
	private int ticket=5;
	
	public void run(){
		 for (int i=0;i<10;i++)  
	        {  
	            if(ticket > 0){  
	                System.out.println(Thread.currentThread().getName()+"------>"+"ticket = " + ticket--);  
	            }  
	        }  
	}
}
