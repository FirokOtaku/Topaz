package firok.topaz.resource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;

/**
 * 用于管理自动超时关闭资源的缓存池
 * */
public class AutoClosablePool<TypeKey extends Record, TypeItem extends java.lang.AutoCloseable> implements java.lang.AutoCloseable
{
	private boolean isClosed;
	private final long timeout;
	private final Function<TypeKey, TypeItem> generator;
	private final Closer<TypeItem> closer;
	private Map<TypeKey, TypeItem> poolItem;
	private Map<TypeKey, Long> poolPing;
	private Thread threadPool;

	public interface Closer<TypeItem extends java.lang.AutoCloseable> { void closeItem(TypeItem item) throws Exception; }
	/**
	 * @param timeout 超时时间，单位毫秒. 如果为 {@code Long.MAX_VALUE} 则不会自动关闭资源
	 * @param generator 映射函数. 不要返回 null
	 * @param closer 关闭函数. 如果为 null, 则使用 {@link java.lang.AutoCloseable#close()}
	 * */
	public AutoClosablePool(long timeout, Function<TypeKey, TypeItem> generator, Closer<TypeItem> closer)
	{
		if(timeout <= 0)
			throw new IllegalArgumentException("timeout must be greater than 0");
		if(generator == null)
			throw new IllegalArgumentException("generator must not be null");

		this.isClosed = false;
		this.timeout = timeout;
		this.generator = generator;
		this.closer = closer;
		this.poolItem = new HashMap<>();
		this.poolPing = new HashMap<>();

		if(timeout != Long.MAX_VALUE)
		{
			this.threadPool = new ThreadPool();
			this.threadPool.start();
		}
	}
	/**
	 * @see AutoClosablePool#AutoClosablePool(long, Function, Closer)
	 * */
	public AutoClosablePool(long timeout, Function<TypeKey, TypeItem> generator)
	{
		this(timeout, generator, null);
	}

	public synchronized TypeItem get(TypeKey key) throws Exception
	{
		this.checkNoClosed();

		TypeItem item = this.poolItem.get(key);
		try
		{
			if(item == null)
			{
				item = this.generator.apply(key);
				this.poolItem.put(key, item);
			}
			this.poolPing.put(key, System.currentTimeMillis());
			return item;
		}
		catch (Exception e)
		{
			this.poolItem.remove(key);
			this.poolPing.remove(key);
			throw new Exception("error when getting item");
		}
	}
	public synchronized void closeAndRemove(TypeKey key) throws Exception
	{
		this.checkNoClosed();
		
		TypeItem item = this.poolItem.get(key);
		if(item == null)
			throw new IllegalArgumentException("item not found");
		
		if(closer == null)
			item.close();
		else
			closer.closeItem(item);

		this.poolItem.remove(key);
		this.poolPing.remove(key);
	}

	private void checkNoClosed()
	{
		if(this.isClosed)
			throw new IllegalStateException("pool is closed");
	}

	public synchronized boolean isClosed()
	{
		return this.isClosed;
	}

	@Override
	public synchronized void close() throws Exception
	{
		checkNoClosed();
		var setKey = new HashSet<TypeKey>();
		try
		{
			for(var entry : this.poolItem.entrySet())
			{
				var key = entry.getKey();
				var item = entry.getValue();
				if(closer == null)
					item.close();
				else
					closer.closeItem(item);
				setKey.add(key);
			}
		}
		catch (Exception e)
		{
			throw new Exception("error when closing item", e);
		}
		finally
		{
			for(var key : setKey)
			{
				this.poolItem.remove(key);
			}
		}
//		this.poolItem.clear();
		this.poolItem = null;
//		this.poolPing.clear();
		this.poolPing = null;

		this.threadPool.interrupt();
		this.threadPool = null;

		this.isClosed = true;
	}

	private class ThreadPool extends Thread
	{
		@Override
		public void run()
		{
			while(true)
			{
				try
				{
					try
					{
						checkNoClosed();
					}
					catch (Exception e)
					{
						throw new InterruptedException("pool was closed");
					}

					final var now = System.currentTimeMillis();

					var setKey = new HashSet<TypeKey>();
					synchronized (AutoClosablePool.this)
					{
						for(var entry : AutoClosablePool.this.poolPing.entrySet())
						{
							var key = entry.getKey();
							var lastPing = entry.getValue();
							if(lastPing <= 0 || now - lastPing >= AutoClosablePool.this.timeout)
							{
								setKey.add(key);
							}
						}

						for(var key : setKey)
						{
							try
							{
								var item = AutoClosablePool.this.poolItem.get(key);
								if(closer == null)
									item.close();
								else
									closer.closeItem(item);
								AutoClosablePool.this.poolItem.remove(key);
								AutoClosablePool.this.poolPing.remove(key);
							}
							catch (Exception e)
							{
								AutoClosablePool.this.poolPing.put(key, -1L);
							}
						}
					}

					Thread.sleep(5000);
				}
				catch (InterruptedException e)
				{
//					System.out.println("pool was closed");
					break;
				}
			}
		}
	}
}
