package firok.topaz.thread;

import firok.topaz.function.MayRunnable;
import firok.topaz.general.Collections;

import java.util.concurrent.CountDownLatch;

/**
 * 多线程工具方法
 * @since 3.14.0
 * @author Firok
 * */
public class Threads
{
	/**
	 * 我大抵是想睡觉, 不需要你提醒我这样很危险
	 * @param ms 睡多久
	 * @return 睡醒了返 true, 睡过去了返 false
	 * */
	@SuppressWarnings("all")
	public static boolean sleep(long ms)
	{
		try
		{
			Thread.sleep(ms);
			return true;
		}
		catch (InterruptedException any)
		{
			return false;
		}
	}

	/**
	 * 直接启动一个线程
	 * */
	public static Thread start(boolean isDaemon, MayRunnable function)
	{
		var thread = new Thread(function.anyway(true));
		thread.setDaemon(isDaemon);
		thread.start();
		return thread;
	}

	/**
	 * 等待一组任务执行完毕
	 * @return 是否成功执行完毕
	 * @since 5.9.0
	 * */
	public static boolean waitFor(MayRunnable... functions)
	{
		final int size = Collections.sizeOf(functions);
		if(size == 0) return true;

		var latch = new CountDownLatch(size);
		for(var step = 0; step < size; step++)
		{
			var function = functions[step];
			start(false, () -> {
				try
				{
					function.anyway().run();
				}
				finally
				{
					latch.countDown();
				}
			});
		}

		try
		{
			synchronized (latch)
			{
				latch.await();
			}
			return true;
		}
		catch (InterruptedException shutdown)
		{
			return false;
		}
	}
}
