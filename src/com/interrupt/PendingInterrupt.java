package com.interrupt;

/*
 *待决中断
 *在上面的例子中，sleep（）方法的实现检查到休眠线程被中断，
 *它会相当友好地终止线程，并抛出InterruptedException异常。
 *另外一种情况，如果线程在调用sleep（）方法前被中断，那么该中断称为待决中断，
 *它会在刚调用sleep（）方法时，立即抛出InterruptedException异常。
 * 
 * 
 *如果i小于10，那么线程不会被中断，
 *最终输出的时间差距应该在2000附近（具体时间由系统决定，不精确），
 *如果i大于10，则调用中断当前线程的代码，
 *但main线程仍然运行，最终输出的时间差距应该远小于2000，因为线程尚未休眠，便被中断，
 *因此，一旦调用sleep（）方法，会立即打印出catch块中的信息。
 * */
public class PendingInterrupt extends Object {

	public static void main(String[] args) {

		int i = 100;
		// 如果输入了参数，则在mian线程中中断当前线程（亦即main线程）
		if (i > 10) {
			Thread.currentThread().interrupt();
		}
		// 获取当前时间
		long startTime = System.currentTimeMillis();
		try {
			Thread.sleep(2000);
			System.out.println("was NOT interrupted");
		} catch (InterruptedException x) {
			System.out.println("was interrupted");
		}
		// 计算中间代码执行的时间
		System.out.println("elapsedTime="
				+ (System.currentTimeMillis() - startTime));
	}
}
