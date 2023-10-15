package firok.topaz.general;

import java.util.*;
import java.util.function.Predicate;

/**
 * 特别简单的事件总线实现
 *
 * @author Firok
 * @since 3.22.0
 * */
public class SimpleEventBus<TypeEvent>
{
	private final List<Predicate<TypeEvent>> listeners = new ArrayList<>();

	/**
	 * @param listener 返回 true, 将会使得事件总线停止继续执行
	 * */
	public void addListener(Predicate<TypeEvent> listener)
	{
		Objects.requireNonNull(listener);
		synchronized (this)
		{
			listeners.add(listener);
		}
	}
	/**
	 * @see #addListener(Predicate)
	 * */
	public void addListener(int index, Predicate<TypeEvent> listener)
	{
		Objects.requireNonNull(listener);
		synchronized (this)
		{
			listeners.add(index, listener);
		}
	}
	/**
	 * 移除一个监听器
	 * */
	public void removeListener(Predicate<TypeEvent> listener)
	{
		Objects.requireNonNull(listener);
		synchronized (this)
		{
			listeners.remove(listener);
		}
	}
	/**
	 * 移除指定监听器
	 * */
	public void removeListener(int index)
	{
		synchronized (this)
		{
			listeners.remove(index);
		}
	}
	/**
	 * 清空所有监听器
	 * */
	public void clear()
	{
		synchronized (this)
		{
			this.listeners.clear();
		}
	}
	/**
	 * 获取监听器列表
	 * @apiNote 这会返回一个拷贝列表
	 * */
	public List<Predicate<TypeEvent>> getListeners()
	{
		synchronized (this)
		{
			return new ArrayList<>(this.listeners);
		}
	}

	/**
	 * 触发一次事件
	 * @return 是否被停止执行
	 * */
	public boolean trigger(TypeEvent event)
	{
		for(var listener : listeners)
		{
			var result = listener.test(event);
			if(result)
				return true;
		}
		return false;
	}
}
