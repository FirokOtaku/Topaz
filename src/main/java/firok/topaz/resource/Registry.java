package firok.topaz.resource;

import firok.topaz.TopazExceptions;
import firok.topaz.function.TriConsumer;
import firok.topaz.function.TriFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 一个简易的可锁定的注册表
 * @see RegistryItem
 * @apiNote 这个类的实例线程不安全, 需要调用者确保线程安全
 * @since 6.12.0
 * @version 7.14.0
 * @author Firok
 * */
@SuppressWarnings({"SpellCheckingInspection"})
public class Registry<TypeKey, TypeValue extends RegistryItem<TypeKey>>
{
	private final boolean isReplacable;
	private boolean isLocked;
	private Map<TypeKey, TypeValue> map;
	/**
	 * 注册监听器
	 * */
	private TriFunction<@NotNull TypeKey, @NotNull TypeValue, @Nullable TypeValue, @NotNull Boolean> listener;

	/**
	 * 创建一个注册表
	 * */
	public Registry()
	{
		this(false);
	}
	/**
	 * 创建一个注册表
	 * @param isReplacable 是否允许替换已注册的键
	 * */
	public Registry(boolean isReplacable)
	{
		this.map = new HashMap<>();
		this.isReplacable = isReplacable;
		this.isLocked = false;
	}

	/**
	 * @since 6.17.0
	 * */
	public Map<TypeKey, TypeValue> items()
	{
		return new HashMap<>(this.map);
	}

	public void register(TypeValue item)
	{
		TopazExceptions.RegistryAlreadyLocked.maybe(isLocked);
		var key = item.getRegistryKey();
		TopazExceptions.RegistryKeyDuplicate.maybe(!isReplacable && map.containsKey(key));

		if(listener != null)
		{
			var old = map.get(key);
			var result = listener.apply(key, item, old);
			if(result)
			{
				map.put(key, item);
			}
		}
		else
		{
			map.put(key, item);
		}
	}

	/**
	 * @since 6.17.0
	 * */
	public void registerAll(Map<TypeKey, TypeValue> map)
	{
		for(var value : map.values())
		{
			register(value);
		}
	}

	public TypeValue get(TypeKey key)
	{
		return map.get(key);
	}

	public void lock()
	{
		map = Collections.unmodifiableMap(map);
		isLocked = true;
	}

	public boolean isLocked()
	{
		return isLocked;
	}

	/**
	 * 注册一个监听器, 当有新的项目注册时会调用
	 * @param listener (键, 将要注册的新值, (Nullable) 旧值) -> true - 成功注册, false - 阻止注册
	 * @since 7.14.0
	 * */
	public void onRegister(
			TriFunction<@NotNull TypeKey, @NotNull TypeValue, @Nullable TypeValue, @NotNull Boolean> listener
	)
	{
		this.listener = listener;
	}
}
