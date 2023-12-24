package firok.topaz.thread;

import firok.topaz.function.MayConsumer;
import firok.topaz.function.MayRunnable;
import firok.topaz.general.Collections;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * 多线程工具方法
 * @since 3.14.0
 * @author Firok
 * */
@SuppressWarnings("UnusedReturnValue")
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
	 * 直接启动一个线程
	 * @since 7.6.0
	 * */
	public static Thread start(boolean isDaemon, MayConsumer<Thread> function)
	{
		var thread = new Thread() {
			@Override
			public void run()
			{
				function.anyway().accept(this);
			}
		};
		thread.setDaemon(isDaemon);
		thread.start();
		return thread;
	}

	/**
	 * 直接启动一个线程
	 * @since 5.11.0
	 * */
	public static Thread start(boolean isDaemon, String name, MayRunnable function)
	{
		var thread = new Thread(function.anyway(true), name);
		thread.setDaemon(isDaemon);
		thread.start();
		return thread;
	}

	/**
	 * 直接启动一个线程
	 * @since 7.6.0
	 * */
	public static Thread start(boolean isDaemon, String name, MayConsumer<Thread> function)
	{
		var thread = new Thread() {
			@Override
			public void run()
			{
				function.anyway().accept(this);
			}
		};
		thread.setDaemon(isDaemon);
		thread.setName(name);
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

		var batchName = "topaz.batch-" + UUID.randomUUID().toString().substring(0, 8) + '-';

		var latch = new CountDownLatch(size);
		for(var step = 0; step < size; step++)
		{
			var function = functions[step];
			start(false, batchName + step, () -> {
				try
				{
					function.anyway().run();
				}
				catch (Exception any)
				{
					any.printStackTrace(System.err);
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

	/**
	 * 等待一组任务执行完毕
	 * @return 是否成功执行完毕
	 * @since 6.4.0
	 * */
	public static boolean waitFor(Collection<MayRunnable> functions)
	{
		var arrFunctions = functions.toArray(new MayRunnable[0]);
		return waitFor(arrFunctions);
	}
}
