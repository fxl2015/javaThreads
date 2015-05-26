package com.TimerAndTimerTask;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;



/*Timer和TimerTask可以做为实现线程的第三种方式，前两中方式分别是继承自Thread类和实现Runnable接口。
  Timer是一种线程设施，用于安排以后在后台线程中执行的任务。可安排任务执行一次，或者定期重复执行，
      可以看成一个定时器，可以调度TimerTask。TimerTask是一个抽象类，实现了Runnable接口，所以具备了多线程的能力。
      一个Timer可以调度任意多个TimerTask，它会将TimerTask存储在一个队列中，顺序调度，
      如果想两个TimerTask并发执行，则需要创建两个Timer。
*/

public class TimerAndTimerTask {

	public static void main(String[] args) {
		
	//	timerTest1();
		timerTest2();

	}

	private static void timerTest1() {
		Timer timer = new Timer();  
        timer.schedule(new TimerTest1.MyTimerTask1(), 2000);// 两秒后启动任务  
	}

	private static void timerTest2() {
		
		Timer timer = new Timer();  
        timer.schedule(new TimerTask2.MyTimerTask2(), 2000);  
        while(true) {  
            System.out.println(new Date().getSeconds());  
            try {  
                Thread.sleep(1000);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
        }  
	}

}



class TimerTest1{
	static class MyTimerTask1 extends TimerTask {  
        public void run() {  
            System.out.println("爆炸！！！");  
        }  
    }     
}


//  看一个稍复杂的例子，假设有这样一种需求，实现一个连环炸弹，2秒后爆炸一次，3秒后爆炸一次，
//  如此循环下去，这就需要创建两个任务，互相调度
class TimerTask2{
	static class MyTimerTask1 extends TimerTask {  
        public void run() {  
            System.out.println("爆炸！！！");  
            new Timer().schedule(new MyTimerTask2(), 2000);  
        }  
    }  
    static class MyTimerTask2 extends TimerTask {  
        public void run() {  
            System.out.println("爆炸！！！");  
            new Timer().schedule(new MyTimerTask1(), 3000);  
        }  
    }  
}