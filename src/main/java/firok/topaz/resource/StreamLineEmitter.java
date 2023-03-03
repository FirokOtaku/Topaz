package firok.topaz.resource;

import firok.topaz.function.MayConsumer;
import firok.topaz.thread.Threads;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;

/**
 * 从输入流中读取数据, 按行分割并交付监听器
 * @since 3.2.0
 * @author Firok
 * */
public final class StreamLineEmitter implements java.io.Closeable
{
	private transient final InputStream is;
	private transient final Scanner in;
	private transient final List<MayConsumer<String>> listListener;
	private transient boolean isClosed = false;
	private transient final Thread thread;
	/**
	 * 从输入流创建发射器
	 * @param is 输入流
	 * @param isDaemon 是否为守护线程
	 * */
	public StreamLineEmitter(InputStream is, boolean isDaemon)
	{
		this.is = is;
		this.in = new Scanner(is);
		this.listListener = new ArrayList<>();
		this.thread = new LineReceiveThread();
		if(isDaemon) this.thread.setDaemon(true);
		this.thread.start();
	}
	public StreamLineEmitter(InputStream is, boolean isDaemon, MayConsumer<String> lineConsumer)
	{
		this.is = is;
		this.in = new Scanner(is);
		this.listListener = new ArrayList<>();
		this.listListener.add(lineConsumer);
		this.thread = new LineReceiveThread();
		if(isDaemon) this.thread.setDaemon(true);
		this.thread.start();
	}
	public StreamLineEmitter(InputStream is, boolean isDaemon, Collection<MayConsumer<String>> lineConsumers)
	{
		this.is = is;
		this.in = new Scanner(is);
		this.listListener = new ArrayList<>();
		this.listListener.addAll(lineConsumers);
		this.thread = new LineReceiveThread();
		if(isDaemon) this.thread.setDaemon(true);
		this.thread.start();
	}

	/**
	 * 从标准输入流创建发射器
	 * @param isDaemon 是否为守护线程
	 * */
	public static StreamLineEmitter fromSystemIn(boolean isDaemon)
	{
		return new StreamLineEmitter(System.in, isDaemon);
	}

	private void checkNoClosed()
	{
		if(this.isClosed)
		{
			throw new IllegalStateException("emitter has been closed");
		}
	}

	/**
	 * 增加监听器
	 * */
	public synchronized void addListener(MayConsumer<String> lineConsumer)
	{
		checkNoClosed();
		this.listListener.add(lineConsumer);
	}
	/**
	 * 移除监听器
	 * */
	public synchronized void removeListener(MayConsumer<String> lineConsumer)
	{
		checkNoClosed();
		this.listListener.remove(lineConsumer);
	}
	/**
	 * 清空监听器
	 * */
	public synchronized void clearListener()
	{
		checkNoClosed();
		this.listListener.clear();
	}

	/**
	 * 关闭发射器
	 * */
	@Override
	public void close() throws IOException
	{
		checkNoClosed();
		this.in.close();
		this.is.close();
		this.thread.interrupt();
		this.isClosed = true;
	}

	/**
	 * 检查发射器是否关闭
	 * */
	public boolean isClosed()
	{
		return this.isClosed;
	}

	private void emit(String line)
	{
		checkNoClosed();
		for(var listener : this.listListener)
		{
			try
			{
				listener.accept(line);
			}
			catch (Exception any)
			{
				any.printStackTrace(System.err);
			}
		}
	}

	private class LineReceiveThread extends Thread
	{
		LineReceiveThread()
		{
			super();
		}

		@Override
		public void run()
		{
			while(in.hasNextLine())
			{
				var line = in.nextLine();
				synchronized (StreamLineEmitter.this) // 同步
				{
					StreamLineEmitter.this.emit(line);
				}
			}
		}
	}
}
