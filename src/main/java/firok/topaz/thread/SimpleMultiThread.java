package firok.topaz.thread;

import firok.topaz.function.MayRunnable;
import lombok.AllArgsConstructor;

/**
 * 简单创建几个线程跑代码用的工具
 * @implNote "简易" 指功能和实现非常简单, 几乎到了简陋的地步
 * @since 3.14.0
 * @author Firok
 * */
@SuppressWarnings("unused")
public class SimpleMultiThread
{
	/**
	 * 子线程列表
	 * */
	private final Thread[] threads;
	/**
	 * 子线程异常信息
	 * */
	private final Exception[] exceptions;
	/**
	 * 子线程停止状态
	 * */
	private final boolean[] ends;
	private final boolean shutdownTogether;
	/**
	 * @param shutdownTogether 如果某个子线程执行过程抛出异常, 则所有子线程都会一并停止运行
	 * */
	public SimpleMultiThread(boolean shutdownTogether, MayRunnable... runnables)
	{
		if(runnables == null || runnables.length == 0)
			throw new IllegalArgumentException("线程为空");
		for(var runnable : runnables) if(runnable == null)
			throw new IllegalArgumentException("线程为空");

		this.shutdownTogether = shutdownTogether;
		this.exceptions = new Exception[runnables.length];
		this.ends = new boolean[runnables.length];
		this.threads = new Thread[runnables.length];
		for (int step = 0; step < runnables.length; step++)
		{
			var runnable = runnables[step];
			var thread = new Thread(new PackedRunnable(step, runnable));
			thread.setDaemon(true);
			this.threads[step] = thread;
		}
	}
	public SimpleMultiThread(MayRunnable... runnables)
	{
		this(true, runnables);
	}

	/**
	 * 开始运行
	 * */
	public void start()
	{
		for(var thread : threads)
			thread.start();
	}
	/**
	 * 停止运行
	 * */
	public void interrupt()
	{
		for(var thread : threads)
			thread.interrupt();
	}
	/**
	 * 停止运行
	 * */
	public void stop()
	{
		for(var thread : threads)
			thread.stop();
	}

	/**
	 * 获取某个子线程
	 * @deprecated 虽然不知道你想干啥就是了
	 * */
	@Deprecated
	public Thread threadOf(int index)
	{
		return threads[index];
	}
	/**
	 * 获取某个线程是否出现了异常
	 * */
	public Exception exceptionOf(int index)
	{
		return exceptions[index];
	}
	/**
	 * 获取某个线程是否已经停止
	 * */
	public boolean endOf(int index)
	{
		return ends[index];
	}

	/**
	 * 检查是否出现了任何异常
	 * */
	public Exception anyException()
	{
		for(var exception : exceptions)
		{
			if(exception != null)
				return exception;
		}
		return null;
	}
	/**
	 * 如果内部出现异常 则抛出
	 *
	 * @since 3.21.0
	 * @author Firok
	 * */
	public void throwAnyException() throws Exception
	{
		var exception = anyException();
		if(exception != null)
			throw exception;
	}
	/**
	 * 是否所有子线程都已经停止
	 * */
	public boolean hasAllEnd()
	{
		for(var end : ends)
		{
			if(!end) return false;
		}
		return true;
	}
	/**
	 * 是否某个子线程已经停止
	 * */
	public boolean hasAnyEnd()
	{
		for(var end : ends)
		{
			if(end) return true;
		}
		return false;
	}

	/**
	 * 等待执行完成
	 * @apiNote 如未执行 {@code #start() } 方法启动子线程, 调用此方法将会导致阻塞
	 * */
	@SuppressWarnings("BusyWait")
	public void waitEnd(int interval)
	{
		do
		{
			if(hasAllEnd()) return;

			try { Thread.sleep(interval); }
			catch (Exception ignored) { }
		}
		while(true);
	}
	public void waitEnd()
	{
		waitEnd(500);
	}

	/**
	 * 子线程数量
	 * */
	public int size()
	{
		return threads.length;
	}

	@AllArgsConstructor
	private class PackedRunnable implements Runnable
	{
		int index;
		MayRunnable raw;
		@Override
		public void run()
		{
			try
			{
				raw.run();
				SimpleMultiThread.this.ends[index] = true;
			}
			catch (Exception any)
			{
				SimpleMultiThread.this.exceptions[index] = any;
				SimpleMultiThread.this.ends[index] = true;
				if(SimpleMultiThread.this.shutdownTogether) // 停止其它子线程
				{
					for(var step = 0; step < SimpleMultiThread.this.threads.length; step++)
					{
						if(step == index) continue;

						SimpleMultiThread.this.threads[step].stop();
						SimpleMultiThread.this.ends[step] = true;
					}
				}
			}
		}
	}
}
