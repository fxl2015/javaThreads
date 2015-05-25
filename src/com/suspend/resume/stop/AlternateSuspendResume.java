package com.suspend.resume.stop;

/*
 * 由结果可以看出，输出的所有结果均为true。首先，针对情况1（线程挂起的位置不确定），这里确定了线程挂起的位置，
 * 不会出现线程在stepOne操作和stepTwo操作之间挂起的情况；针对情况2（main线程中执行asr.areValuesEqual（）进行比较时，
 * 恰逢stepOne操作执行完，而stepTwo操作还没执行），在发出挂起请求后，还没有执行asr.areValuesEqual（）操作前，
 * 让main线程休眠450ms（>300ms），如果挂起请求发出时，新线程正执行到或即将执行到stepOne操作（如果在其前面的话，就会响应挂起请求，从而挂起线程），
 * 那么在stepTwo操作执行前，main线程的休眠还没结束，从而main线程休眠结束后执行asr.areValuesEqual（）操作进行比较时，
 * stepTwo操作已经执行完，因此也不会出现输出结果为false的情况。
 * 可以将ars.suspendRequest（）代码后的sleep代码去掉，
 * 或将休眠时间改为200（明显小于300即可）后，查看执行结果，会发现结果中依然会有出现false的情况。
 * 
 * 
 * 总结：线程的挂起和恢复实现的正确方法是：通过设置标志位，让线程在安全的位置挂起
 *
 *
 * 终止线程
 * 当调用Thread的start（）方法，执行完run（）方法后，或在run（）方法中return，
 * 线程便会自然消亡。另外Thread API中包含了一个stop（）方法，可以突然终止线程。
 * 但它在JDK1.2后便被淘汰了，因为它可能导致数据对象的崩溃。一个问题是，当线程终止时，
 * 很少有机会执行清理工作；另一个问题是，当在某个线程上调用stop（）方法时，线程释放它当前持有的所有锁，
 * 持有这些锁必定有某种合适的理由——也许是阻止其他线程访问尚未处于一致性状态的数据，
 * 突然释放锁可能使某些对象中的数据处于不一致状态，而且不会出现数据可能崩溃的任何警告。
 *
 * 终止线程的替代方法：同样是使用标志位，通过控制标志位来终止线程。
 * 
 */

public class AlternateSuspendResume extends Object implements Runnable {

	private volatile int firstVal;
	private volatile int secondVal;

	// 增加标志位，用来实现线程的挂起和恢复
	private volatile boolean suspended;

	public boolean areValueEquals() {
		return firstVal == secondVal;
	}

	@Override
	public void run() {
		suspended = false;
		firstVal = 0;
		secondVal = 0;
		try {
			workMethod();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void workMethod() throws InterruptedException {
		int val = 1;

		while (true) {
			// 仅当线程挂起时，才运行这行代码
			waitWhileSuspended();

			stepOne(val);
			stepTwo(val);
			val++;

			// 仅当线程挂起时，才运行这行代码
			waitWhileSuspended();
			Thread.sleep(200);
		}
	}

	private void stepOne(int val) throws InterruptedException {
		firstVal = val;
		Thread.sleep(300);
	}

	private void stepTwo(int val) {
		secondVal = val;
	}

	public void suspendRequest() {
		suspended = true;
	}

	public void resumeRequest() {
		suspended = false;
	}

	private void waitWhileSuspended() throws InterruptedException {

		// 这是一个“繁忙等待”技术的示例。
		// 它是非等待条件改变的最佳途径，因为它会不断请求处理器周期地执行检查，
		// 更佳的技术是：使用Java的内置“通知-等待”机制
		while (suspended) {
			Thread.sleep(200);
		}
	}

	public static void main(String[] args) {

		AlternateSuspendResume asr = new AlternateSuspendResume();
		Thread t = new Thread(asr);
		t.start();

		// 休眠1秒，让其他线程有机会获得执行
		try {
			Thread.sleep(1000);
		} catch (InterruptedException x) {
		}

		for (int i = 0; i < 10; i++) {
			asr.suspendRequest();

			// 让线程有机会注意到挂起请求
			// 注意：这里休眠时间一定要大于
			// stepOne操作对firstVal赋值后的休眠时间，即300ms，
			// 目的是为了防止在执行asr.areValuesEqual（）进行比较时,
			// 恰逢stepOne操作执行完，而stepTwo操作还没执行
			try {
				Thread.sleep(350);
			} catch (InterruptedException x) {
			}

			System.out.println("dsr.areValueEquals()=" + asr.areValueEquals());

			asr.resumeRequest();

			try {
				// 线程随机休眠0~2秒
				Thread.sleep((long) (Math.random() * 2000.0));
			} catch (InterruptedException x) {
				x.printStackTrace();
			}
		}

		System.exit(0); // 退出应用程序

	}

}
