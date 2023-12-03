package firok.topaz.resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 一个简易的可锁定的注册表
 * @see RegistryItem
 * @apiNote 这个类的实例线程不安全, 需要调用者确保线程安全
 * @since 6.12.0
 * @author Firok
 * */
@SuppressWarnings({"SpellCheckingInspection"})
public class Registry<TypeKey, TypeValue extends RegistryItem<TypeKey>>
{
	private final boolean isReplacable;
	private boolean isLocked;
	private Map<TypeKey, TypeValue> map;

	public Registry()
	{
		this(false);
	}
	/**
	 * @param isReplacable 是否允许替换已注册的键
	 * */
	public Registry(boolean isReplacable)
	{
		this.map = new HashMap<>();
		this.isReplacable = isReplacable;
		this.isLocked = false;
	}

	public void register(TypeValue item)
	{
		var key = item.getRegistryKey();
		if(!isReplacable && map.containsKey(key))
			throw new IllegalArgumentException("重复注册键: " + key);
		map.put(key, item);
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
}
