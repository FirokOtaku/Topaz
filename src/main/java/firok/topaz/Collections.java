package firok.topaz;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

/**
 * @author Firok
 * @since 1.0.0
 */
public final class Collections
{
	private Collections() { }

	/**
	 * 提取若干实体数据中的一对一映射关系
	 * @param items 实体集
	 * @param keyFunction 如何获取键
	 * @param <TypeKey> 键类型
	 * @param <TypeEntity> 实体类型
	 * @return 映射关系, 不可变表
	 */
	public static <TypeKey,TypeEntity> Map<TypeKey, TypeEntity> mappingKeyEntity(
			Iterable<TypeEntity> items,
			Function<TypeEntity,TypeKey> keyFunction
	)
	{
		Objects.requireNonNull(items);
		Objects.requireNonNull(keyFunction);

		var map = new HashMap<TypeKey, TypeEntity>();

		for(TypeEntity item : items) map.put(keyFunction.apply(item),item);

		return java.util.Collections.unmodifiableMap(map);
	}



	/**
	 * 提取若干实体数据中的一对一映射关系
	 * @param items 实体集
	 * @param keyFunction 如何获取键
	 * @param valueFunction 如何获取值
	 * @param <TypeKey> 键类型
	 * @param <TypeEntity> 实体类型
	 * @param <TypeValue> 值类型
	 * @return 映射关系, 不可变表
	 */
	public static <TypeKey,TypeEntity, TypeValue> Map<TypeKey, TypeValue> mappingKeyValue(
			Iterable<TypeEntity> items,
			Function<TypeEntity,TypeKey> keyFunction,
			Function<TypeEntity, TypeValue> valueFunction
	)
	{
		Objects.requireNonNull(items);
		Objects.requireNonNull(keyFunction);
		Objects.requireNonNull(valueFunction);

		var map = new HashMap<TypeKey, TypeValue>();

		for(TypeEntity item : items) map.put(keyFunction.apply(item), valueFunction.apply(item));

		return java.util.Collections.unmodifiableMap(map);
	}

	/**
	 * 提取若干实体数据中的一对多映射关系
	 * @param items 实体集
	 * @param keyFunction 如何获取键
	 * @param valueFunction 如何获取值
	 * @param <TypeKey> 键类型
	 * @param <TypeEntity> 实体类型
	 * @param <TypeValue> 值类型
	 * @return 映射关系, 不可变表
	 */
	public static <TypeKey, TypeEntity, TypeValue> Map<TypeKey, List<TypeValue>> mappingKeyMultiValueList(
			Iterable<TypeEntity> items,
			Function<TypeEntity, TypeKey> keyFunction,
			Function<TypeEntity, TypeValue> valueFunction
	)
	{
		Objects.requireNonNull(items);
		Objects.requireNonNull(keyFunction);
		Objects.requireNonNull(valueFunction);

		var map = new HashMap<TypeKey, List<TypeValue>>();

		for(TypeEntity item : items)
		{
			TypeKey key = keyFunction.apply(item);
			TypeValue target = valueFunction.apply(item);

			List<TypeValue> list = map.computeIfAbsent(key, k -> new ArrayList<>());
			list.add(target);
		}

		return java.util.Collections.unmodifiableMap(map);
	}


	/**
	 * 提取若干实体数据中的一对多映射关系
	 * @param items 实体集
	 * @param keyFunction 如何获取键
	 * @param valueFunction 如何获取值
	 * @param <TypeKey> 键类型
	 * @param <TypeEntity> 实体类型
	 * @param <TypeValue> 值类型
	 * @return 映射关系, 不可变表
	 */
	public static <TypeKey, TypeEntity, TypeValue> Map<TypeKey, Set<TypeValue>> mappingKeyMultiValueSet(
			Iterable<TypeEntity> items,
			Function<TypeEntity, TypeKey> keyFunction,
			Function<TypeEntity, TypeValue> valueFunction
	)
	{
		Objects.requireNonNull(items);
		Objects.requireNonNull(keyFunction);
		Objects.requireNonNull(valueFunction);

		var map = new HashMap<TypeKey, Set<TypeValue>>();

		for(TypeEntity item : items)
		{
			TypeKey key = keyFunction.apply(item);
			TypeValue target = valueFunction.apply(item);

			Set<TypeValue> list = map.computeIfAbsent(key, k -> new HashSet<>());
			list.add(target);
		}

		return java.util.Collections.unmodifiableMap(map);
	}

	/**
	 * 提取若干实体数据中的一对多映射关系
	 * @param items 实体集
	 * @param keyFunction 如何获取键
	 * @param <TypeKey> 键类型
	 * @param <TypeEntity> 实体类型
	 * @return 映射关系, 不可变表
	 */
	public static <TypeKey, TypeEntity> Map<TypeKey, List<TypeEntity>> mappingKeyMultiEntityList(
			Iterable<TypeEntity> items,
			Function<TypeEntity, TypeKey> keyFunction
	)
	{
		return mappingKeyMultiValueList(items, keyFunction, i->i);
	}


	/**
	 * 提取若干实体数据中的一对多映射关系
	 * @param items 实体集
	 * @param keyFunction 如何获取键
	 * @param <TypeKey> 键类型
	 * @param <TypeEntity> 实体类型
	 * @return 映射关系, 不可变表
	 */
	public static <TypeKey, TypeEntity> Map<TypeKey, Set<TypeEntity>> mappingKeyMultiEntitySet(
			Iterable<TypeEntity> items,
			Function<TypeEntity, TypeKey> keyFunction
	)
	{
		return mappingKeyMultiValueSet(items, keyFunction, i->i);
	}

	/**
	 * 为数组创建一个迭代器
	 * @param values 要迭代的数据
	 * @param <T> 数据类型
	 * @return 迭代器
	 * @implNote 闲得写的
	 */
	@SafeVarargs
	public static <T> Iterator<T> iterator(T...values)
	{
		return new Iterator<>()
		{
			/**
			 * 迭代标志位
			 */
			int flag = 0;

			@Override
			public boolean hasNext()
			{
				return values != null && flag < values.length;
			}

			@Override
			public T next()
			{
				return values != null ? values[ flag++ ] : null;
			}
		};
	}

	/**
	 * 把数组前若干个元素取出
	 * @param maxSize 最大长度
	 * @param items 数据集
	 * @param <T> 数据类型
	 * @return 裁剪后数组
	 */
	@SafeVarargs
	public static <T> T[] cut(int maxSize, T...items)
	{
		return items == null ? null :
				items.length > maxSize ? Arrays.copyOf(items,maxSize) :
						items;
	}

	/**
	 * 从指定迭代器取出前若干个元素
	 * */
	public static <T> Collection<T> cut(int maxSize, Iterable<T> items)
	{
		var ret = new ArrayList<T>();

		int index = 0;
		for(T item : items)
		{
			ret.add(item);
			index++;
			if(index >= maxSize)
				break;
		}

		return ret;
	}

	/**
	 * 将数组转换为Set
	 * @param items 数据集
	 * @param <T> 数据类型
	 * @return 转换后Set
	 */
	@SafeVarargs
	public static <T> Set<T> toSet(T... items)
	{
		var ret = new HashSet<T>();
		if(items != null && items.length > 0)
			java.util.Collections.addAll(ret, items);
		return ret;
	}

	/**
	 * 长整型求和
	 */
	public static long sumLong(Iterable<? extends Number> numbers)
	{
		long ret = 0;
		for(var num : numbers) ret += num.longValue();
		return ret;
	}

	/**
	 * 双浮点型求和
	 */
	public static double sumDouble(Iterable<? extends Number> numbers)
	{
		double ret = 0;
		for(var num : numbers) ret += num.doubleValue();
		return ret;
	}

	/**
	 * 精确求和
	 */
	public static java.math.BigDecimal sumDecimal(Iterable<? extends Number> numbers)
	{
		var ret = BigDecimal.ZERO;
		for(var num : numbers) ret = ret.add(new BigDecimal(num.toString()));
		return ret;
	}

	/**
	 * 移除某集合中的空元素
	 * @param collection 集合
	 * @param <TypeCollection> 集合类型
	 * @param <TypeItem> 集合元素类型
	 * @return 处理之后的集合. 如果传入的值为null, 则返回null
	 * @implNote 至于为什么封装一个这个接口, 不直接用<code>Collection::removeIf</code>, 因为那东西返回的是boolean类型, 不好直接用进函数式编程
	 */
	public static <TypeCollection extends Collection<TypeItem>,TypeItem> TypeCollection removeNullElements(TypeCollection collection)
	{
		if(collection == null) return null;
		collection.removeIf(Objects::isNull);
		return collection;
	}

	/**
	 * @param collection 原集合
	 * @param sizeGroup 切分大小
	 * @param <TypeCollection> 集合类型
	 * @param <TypeItem> 元素类型
	 * @return 切分后的分组列表
	 * @deprecated 未开发完成, 存在逻辑错误
	 */
	@Deprecated
	public static <TypeCollection extends Iterable<TypeItem>, TypeItem> List<TypeCollection> trimGroup(TypeCollection collection, int sizeGroup)
	{
		if(sizeGroup < 1) throw new IllegalArgumentException("分组大小必须大于1");

		var ret = new ArrayList<TypeCollection>();

		var stepGroup = 0;
		var sub = new ArrayList<TypeItem>(sizeGroup);
		for (TypeItem typeItem : collection)
		{
			sub.add(typeItem);
			sub.size();
			stepGroup++;
			if(stepGroup == sizeGroup)
			{
				ret.add((TypeCollection) sub);
				sub = new ArrayList<>(sizeGroup);
			}
		}

		return ret;
	}
}
