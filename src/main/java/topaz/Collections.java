package topaz;

import java.util.*;
import java.util.function.Function;

/**
 * @author Firok
 */
public class Collections
{
	/**
	 * 提取若干实体数据中的一对一映射关系
	 * @param items 实体集
	 * @param keyFunction 如何获取键
	 * @param <TypeKey> 键类型
	 * @param <TypeEntity> 实体类型
	 * @return 映射关系, 不可变表
	 */
	public static <TypeKey,TypeEntity> Map<TypeKey, TypeEntity> mapping(
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
	 * @param targetFunction 如何获取值
	 * @param <TypeKey> 键类型
	 * @param <TypeEntity> 实体类型
	 * @param <TypeTarget> 值类型
	 * @return 映射关系, 不可变表
	 */
	public static <TypeKey,TypeEntity,TypeTarget> Map<TypeKey,TypeTarget> mapping(
			Iterable<TypeEntity> items,
			Function<TypeEntity,TypeKey> keyFunction,
			Function<TypeEntity,TypeTarget> targetFunction
	)
	{
		Objects.requireNonNull(items);
		Objects.requireNonNull(keyFunction);
		Objects.requireNonNull(targetFunction);

		var map = new HashMap<TypeKey,TypeTarget>();

		for(TypeEntity item : items) map.put(keyFunction.apply(item), targetFunction.apply(item));

		return java.util.Collections.unmodifiableMap(map);
	}

	/**
	 * 提取若干实体数据中的一对多映射关系
	 * @param items 实体集
	 * @param keyFunction 如何获取键
	 * @param targetFunction 如何获取值
	 * @param <TypeKey> 键类型
	 * @param <TypeEntity> 实体类型
	 * @param <TypeTarget> 值类型
	 * @return 映射关系, 不可变表
	 */
	public static <TypeKey, TypeEntity, TypeTarget> Map<TypeKey, List<TypeTarget>> mappingMultiList(
			Iterable<TypeEntity> items,
			Function<TypeEntity, TypeKey> keyFunction,
			Function<TypeEntity, TypeTarget> targetFunction
	)
	{
		Objects.requireNonNull(items);
		Objects.requireNonNull(keyFunction);
		Objects.requireNonNull(targetFunction);

		var map = new HashMap<TypeKey, List<TypeTarget>>();

		for(TypeEntity item : items)
		{
			TypeKey key = keyFunction.apply(item);
			TypeTarget target = targetFunction.apply(item);

			List<TypeTarget> list = map.computeIfAbsent(key, k -> new ArrayList<>());
			list.add(target);
		}

		return java.util.Collections.unmodifiableMap(map);
	}


	/**
	 * 提取若干实体数据中的一对多映射关系
	 * @param items 实体集
	 * @param keyFunction 如何获取键
	 * @param targetFunction 如何获取值
	 * @param <TypeKey> 键类型
	 * @param <TypeEntity> 实体类型
	 * @param <TypeTarget> 值类型
	 * @return 映射关系, 不可变表
	 */
	public static <TypeKey, TypeEntity, TypeTarget> Map<TypeKey, Set<TypeTarget>> mappingMultiSet(
			Iterable<TypeEntity> items,
			Function<TypeEntity, TypeKey> keyFunction,
			Function<TypeEntity, TypeTarget> targetFunction
	)
	{
		Objects.requireNonNull(items);
		Objects.requireNonNull(keyFunction);
		Objects.requireNonNull(targetFunction);

		var map = new HashMap<TypeKey, Set<TypeTarget>>();

		for(TypeEntity item : items)
		{
			TypeKey key = keyFunction.apply(item);
			TypeTarget target = targetFunction.apply(item);

			Set<TypeTarget> list = map.computeIfAbsent(key, k -> new HashSet<>());
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
	public static <TypeKey, TypeEntity> Map<TypeKey, List<TypeEntity>> mappingMultiList(
			Iterable<TypeEntity> items,
			Function<TypeEntity, TypeKey> keyFunction
	)
	{
		return mappingMultiList(items, keyFunction, i->i);
	}


	/**
	 * 提取若干实体数据中的一对多映射关系
	 * @param items 实体集
	 * @param keyFunction 如何获取键
	 * @param <TypeKey> 键类型
	 * @param <TypeEntity> 实体类型
	 * @return 映射关系, 不可变表
	 */
	public static <TypeKey, TypeEntity> Map<TypeKey, Set<TypeEntity>> mappingMultiSet(
			Iterable<TypeEntity> items,
			Function<TypeEntity, TypeKey> keyFunction
	)
	{
		return mappingMultiSet(items, keyFunction, i->i);
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
	 * 裁剪数组至指定最大长度
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
}
