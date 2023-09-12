package firok.topaz.general;

import firok.topaz.annotation.Level;
import firok.topaz.annotation.PerformanceIssue;
import firok.topaz.annotation.Resource;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static firok.topaz.reflection.Reflections.constructorOf;
import static firok.topaz.reflection.Reflections.newInstanceOf;

/**
 * @author Firok
 * @since 1.0.0
 */
@SuppressWarnings({"DuplicatedCode", "unused"})
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
	 * 将一个集合按照指定大小切分
	 * @param collection 原集合. <b>目前支持 List 或 Set 类型</b>
	 * @param sizeGroup 切分大小
	 * @param <TypeCollection> 集合类型
	 * @param <TypeItem> 元素类型
	 * @return 切分后的分组列表
	 */
	@SuppressWarnings("unchecked")
	public static <TypeCollection extends Iterable<TypeItem>, TypeItem>
	List<TypeCollection> trimGroup(TypeCollection collection, int sizeGroup)
	{
		if(sizeGroup <= 1) throw new IllegalArgumentException("分组大小必须大于1");

		var ret = new ArrayList<TypeCollection>();

		Function<Integer, Collection<TypeItem>> cons;
		if(collection instanceof Set<?>) cons = HashSet::new;
		else if(collection instanceof List<?>) cons = ArrayList::new;
		else throw new IllegalArgumentException("不支持的集合类型");

		var stepGroup = 0;
		var subGroup = cons.apply(sizeGroup);
		for (TypeItem typeItem : collection)
		{
			subGroup.add(typeItem);
			stepGroup++;
			if(stepGroup == sizeGroup)
			{
				ret.add((TypeCollection) subGroup);
				subGroup = cons.apply(sizeGroup);
				stepGroup = 0;
			}
		}
		if(!subGroup.isEmpty())
			ret.add((TypeCollection) subGroup);

		return ret;
	}

	/**
	 * 就地翻转
	 * */
	public static void makeReverse(byte[] arr)
	{
		int size = arr.length;
		for (int i=0, mid=size>>1, j=size-1; i<mid; i++, j--)
		{
			var t = arr[i];
			arr[i] = arr[j];
			arr[j] = t;
		}
	}
	/**
	 * 就地翻转
	 * */
	public static void makeReverse(short[] arr)
	{
		int size = arr.length;
		for (int i=0, mid=size>>1, j=size-1; i<mid; i++, j--)
		{
			var t = arr[i];
			arr[i] = arr[j];
			arr[j] = t;
		}
	}
	/**
	 * 就地翻转
	 * */
	public static void makeReverse(char[] arr)
	{
		int size = arr.length;
		for (int i=0, mid=size>>1, j=size-1; i<mid; i++, j--)
		{
			var t = arr[i];
			arr[i] = arr[j];
			arr[j] = t;
		}
	}
	/**
	 * 就地翻转
	 * */
	public static void makeReverse(int[] arr)
	{
		int size = arr.length;
		for (int i=0, mid=size>>1, j=size-1; i<mid; i++, j--)
		{
			var t = arr[i];
			arr[i] = arr[j];
			arr[j] = t;
		}
	}
	/**
	 * 就地翻转
	 * */
	public static void makeReverse(float[] arr)
	{
		int size = arr.length;
		for (int i=0, mid=size>>1, j=size-1; i<mid; i++, j--)
		{
			var t = arr[i];
			arr[i] = arr[j];
			arr[j] = t;
		}
	}
	/**
	 * 就地翻转
	 * */
	public static void makeReverse(long[] arr)
	{
		int size = arr.length;
		for (int i=0, mid=size>>1, j=size-1; i<mid; i++, j--)
		{
			var t = arr[i];
			arr[i] = arr[j];
			arr[j] = t;
		}
	}
	/**
	 * 就地翻转
	 * */
	public static void makeReverse(double[] arr)
	{
		int size = arr.length;
		for (int i=0, mid=size>>1, j=size-1; i<mid; i++, j--)
		{
			var t = arr[i];
			arr[i] = arr[j];
			arr[j] = t;
		}
	}
	/**
	 * 就地翻转
	 * */
	public static <T> void makeReverse(T[] arr)
	{
		int size = arr.length;
		for (int i=0, mid=size>>1, j=size-1; i<mid; i++, j--)
		{
			var t = arr[i];
			arr[i] = arr[j];
			arr[j] = t;
		}
	}

	/**
	 * 使指定数组从给定位置开始排列
	 * */
	@PerformanceIssue(level = Level.Minor, cost = Resource.Mem)
	public static void makeStartIndex(int index, byte[] arr)
	{
		if(index == 0) return;
		@PerformanceIssue var tmp = new byte[arr.length];
		for(int step = 0; step < arr.length; step++)
		{
			tmp[step] = arr[(index + step) % arr.length];
		}
		System.arraycopy(tmp, 0, arr, 0, arr.length);
	}
	/**
	 * 使指定数组从给定位置开始排列
	 * */
	@PerformanceIssue(level = Level.Minor, cost = Resource.Mem)
	public static void makeStartIndex(int index, short[] arr)
	{
		if(index == 0) return;
		@PerformanceIssue var tmp = new short[arr.length];
		for(int step = 0; step < arr.length; step++)
		{
			tmp[step] = arr[(index + step) % arr.length];
		}
		System.arraycopy(tmp, 0, arr, 0, arr.length);
	}
	/**
	 * 使指定数组从给定位置开始排列
	 * */
	@PerformanceIssue(level = Level.Minor, cost = Resource.Mem)
	public static void makeStartIndex(int index, int[] arr)
	{
		if(index == 0) return;
		@PerformanceIssue var tmp = new int[arr.length];
		for(int step = 0; step < arr.length; step++)
		{
			tmp[step] = arr[(index + step) % arr.length];
		}
		System.arraycopy(tmp, 0, arr, 0, arr.length);
	}
	/**
	 * 使指定数组从给定位置开始排列
	 * */
	@PerformanceIssue(level = Level.Minor, cost = Resource.Mem)
	public static void makeStartIndex(int index, float[] arr)
	{
		if(index == 0) return;
		@PerformanceIssue var tmp = new float[arr.length];
		for(int step = 0; step < arr.length; step++)
		{
			tmp[step] = arr[(index + step) % arr.length];
		}
		System.arraycopy(tmp, 0, arr, 0, arr.length);
	}
	/**
	 * 使指定数组从给定位置开始排列
	 * */
	@PerformanceIssue(level = Level.Minor, cost = Resource.Mem)
	public static void makeStartIndex(int index, double[] arr)
	{
		if(index == 0) return;
		@PerformanceIssue var tmp = new double[arr.length];
		for(int step = 0; step < arr.length; step++)
		{
			tmp[step] = arr[(index + step) % arr.length];
		}
		System.arraycopy(tmp, 0, arr, 0, arr.length);
	}
	/**
	 * 使指定数组从给定位置开始排列
	 * */
	@PerformanceIssue(level = Level.Minor, cost = Resource.Mem)
	public static void makeStartIndex(int index, long[] arr)
	{
		if(index == 0) return;
		@PerformanceIssue var tmp = new long[arr.length];
		for(int step = 0; step < arr.length; step++)
		{
			tmp[step] = arr[(index + step) % arr.length];
		}
		System.arraycopy(tmp, 0, arr, 0, arr.length);
	}
	/**
	 * 使指定数组从给定位置开始排列
	 * */
	@PerformanceIssue(level = Level.Minor, cost = Resource.Mem)
	public static void makeStartIndex(int index, BigDecimal[] arr)
	{
		if(index == 0) return;
		@PerformanceIssue var tmp = new BigDecimal[arr.length];
		for(int step = 0; step < arr.length; step++)
		{
			tmp[step] = arr[(index + step) % arr.length];
		}
		System.arraycopy(tmp, 0, arr, 0, arr.length);
	}
	/**
	 * 使指定数组从给定位置开始排列
	 * */
	@PerformanceIssue(level = Level.Minor, cost = Resource.Mem)
	public static void makeStartIndex(int index, BigInteger[] arr)
	{
		if(index == 0) return;
		@PerformanceIssue var tmp = new BigInteger[arr.length];
		for(int step = 0; step < arr.length; step++)
		{
			tmp[step] = arr[(index + step) % arr.length];
		}
		System.arraycopy(tmp, 0, arr, 0, arr.length);
	}

	/**
	 * @author Firok
	 * @since 3.23.0
	 * */
	public static <TypeEntity> int sizeOf(Collection<TypeEntity> collection)
	{
		return collection == null ? 0 : collection.size();
	}

	/**
	 * @since 5.5.0
	 * @author Firok
	 * */
	public static <TypeEntity> int sizeOf(TypeEntity[] array)
	{
		return array == null ? 0 : array.length;
	}

	/**
	 * @since 5.13.0
	 * */
	public static int sizeOf(Map<?, ?> map)
	{
		return map == null ? 0 : map.size();
	}

	/**
	 * @author Firok
	 * @since 3.23.0
	 * */
	public static <TypeEntity> boolean isEmpty(Collection<TypeEntity> collection)
	{
		return collection == null || collection.isEmpty();
	}

	/**
	 * @since 5.5.0
	 * @author Firok
	 * */
	public static <TypeEntity> boolean isEmpty(TypeEntity[] array)
	{
		return array == null || array.length == 0;
	}

	/**
	 * @since 5.13.0
	 * */
	public static boolean isEmpty(Map<?, ?> map)
	{
		return map == null || map.isEmpty();
	}

	/**
	 * @since 3.24.0
	 * @author Firok
	 * */
	public static <TypeEntity> boolean isNotEmpty(Collection<TypeEntity> collection)
	{
		return collection != null && !collection.isEmpty();
	}

	/**
	 * @since 5.5.0
	 * @author Firok
	 * */
	public static <TypeEntity> boolean isNotEmpty(TypeEntity[] array)
	{
		return array != null && array.length > 0;
	}

	/**
	 * @since 5.13.0
	 * */
	public static boolean isNotEmpty(Map<?, ?> map)
	{
		return map != null && !map.isEmpty();
	}

	/**
	 * 对集合中所有所有字符串去头去尾
	 * @since 3.24.0
	 * @author Firok
	 * */
	public static List<String> trimAllOf(Collection<String> strings)
	{
		var ret = new ArrayList<String>();
		for(var string : strings)
		{
			ret.add(string.trim());
		}
		return ret;
	}

	/**
	 * 填充数组
	 * @since 5.5.0
	 * @author Firok
	 * @implNote 当前是同步填充实现方式, 后续可能有并行填充
	 * */
	public static <T> T[] fill(T[] array, Supplier<T> supplier)
	{
		Objects.requireNonNull(array, "数组不可为空");
		Objects.requireNonNull(supplier, "值构造器不可为空");
		if(array.length == 0) return array;
		for(var step = 0; step < array.length; step++)
		{
			array[step] = supplier.get();
		}
		return array;
	}

	/**
	 * 去除集合中的空元素
	 * @since 5.19.0
	 * */
	public static <T> List<T> squeeze(Collection<T> collection)
	{
		var ret = new ArrayList<T>();
		if(collection != null) for(var obj : collection)
		{
			if(obj == null) continue;
			ret.add(obj);
		}
		return ret;
	}

	/**
	 * 去除数组中的空元素
	 * @param array 不可为空
	 * @since 5.19.0
	 * */
	@SuppressWarnings("unchecked")
	public static <T> T[] squeeze(T[] array)
	{
		Objects.requireNonNull(array);
		var countNoneNull = 0;
		for(var obj : array) if(obj != null) countNoneNull++;
		var ret = (T[]) Array.newInstance(array.getClass().getComponentType(), countNoneNull);
		for(var obj : array) if(obj != null)
		{
			ret[ret.length - countNoneNull] = obj;
			countNoneNull--;
		}
		return ret;
	}
}
