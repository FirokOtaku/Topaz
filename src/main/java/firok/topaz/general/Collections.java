package firok.topaz.general;

import firok.topaz.annotation.Level;
import firok.topaz.annotation.Overload;
import firok.topaz.annotation.PerformanceIssue;
import firok.topaz.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
	public static <TypeKey, TypeEntity> Map<TypeKey, TypeEntity> mappingKeyEntity(
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
	public static <TypeKey, TypeEntity, TypeValue> Map<TypeKey, TypeValue> mappingKeyValue(
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
	 * 将某个映射变换为另一个映射
	 * @return 映射关系, 不可变表
	 * @since 6.3.0
	 * */
	public static <TypeKey, TypeOld, TypeNew> Map<TypeKey, TypeNew> mappingMap(
			Map<TypeKey, TypeOld> map, Function<TypeOld, TypeNew> mappingFunction
	)
	{
		var ret = new HashMap<TypeKey, TypeNew>();
		for(var entry : map.entrySet())
		{
			var key = entry.getKey();
			var valueOld = entry.getValue();
			var valueNew = mappingFunction.apply(valueOld);
			ret.put(key, valueNew);
		}
		return java.util.Collections.unmodifiableMap(ret);
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
	 * 遍历并收集一个可遍历对象中所有的数据
	 * @since 7.7.0
	 * */
	public static <T> List<T> collect(Iterable<T> iter)
	{
		var ret = new ArrayList<T>();
		for(var element : iter) ret.add(element);
		return ret;
	}
	/**
	 * 遍历并收集一个可遍历对象中所有的数据
	 * @since 7.7.0
	 * */
	public static <T> List<T> collect(Iterator<T> iter)
	{
		var ret = new ArrayList<T>();
		while(iter.hasNext()) ret.add(iter.next());
		return ret;
	}
	/**
	 * 遍历并收集一个可遍历对象中所有的数据
	 * @since 7.7.0
	 * */
	public static <T> List<T> collect(Enumeration<T> iter)
	{
		var ret = new ArrayList<T>();
		while(iter.hasMoreElements()) ret.add(iter.nextElement());
		return ret;
	}

	/**
	 * 收集某个集合里实体的特定字段值
	 * @since 7.36.0
	 * */
	public static <TypeBean, TypeField> List<TypeField> collectFieldToList(Iterable<TypeBean> iter, Function<TypeBean, TypeField> function)
	{
		var ret = new ArrayList<TypeField>();
		for(var element : iter) ret.add(function.apply(element));
		return ret;
	}
	/**
	 * 收集某个集合里实体的特定字段值
	 * @since 7.36.0
	 * */
	public static <TypeBean, TypeField> List<TypeField> collectFieldToList(Iterator<TypeBean> iter, Function<TypeBean, TypeField> function)
	{
		var ret = new ArrayList<TypeField>();
		while(iter.hasNext()) ret.add(function.apply(iter.next()));
		return ret;
	}
	/**
	 * 收集某个集合里实体的特定字段值
	 * @since 7.36.0
	 * */
	public static <TypeBean, TypeField> List<TypeField> collectFieldToList(Enumeration<TypeBean> iter, Function<TypeBean, TypeField> function)
	{
		var ret = new ArrayList<TypeField>();
		while(iter.hasMoreElements()) ret.add(function.apply(iter.nextElement()));
		return ret;
	}

	/**
	 * 收集某个集合里实体的特定字段值
	 * @since 7.36.0
	 * */
	public static <TypeBean, TypeField> Set<TypeField> collectFieldToSet(Iterable<TypeBean> iter, Function<TypeBean, TypeField> function)
	{
		var ret = new HashSet<TypeField>();
		for(var element : iter) ret.add(function.apply(element));
		return ret;
	}
	/**
	 * 收集某个集合里实体的特定字段值
	 * @since 7.36.0
	 * */
	public static <TypeBean, TypeField> Set<TypeField> collectFieldToSet(Iterator<TypeBean> iter, Function<TypeBean, TypeField> function)
	{
		var ret = new HashSet<TypeField>();
		while(iter.hasNext()) ret.add(function.apply(iter.next()));
		return ret;
	}
	/**
	 * 收集某个集合里实体的特定字段值
	 * @since 7.36.0
	 * */
	public static <TypeBean, TypeField> Set<TypeField> collectFieldToSet(Enumeration<TypeBean> iter, Function<TypeBean, TypeField> function)
	{
		var ret = new HashSet<TypeField>();
		while(iter.hasMoreElements()) ret.add(function.apply(iter.nextElement()));
		return ret;
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
	 * 将一个 Map 的键值反转, 返回一个新的 Map
	 * @since 7.25.0
	 * */
	public static <TypeKey, TypeValue> Map<TypeValue, TypeKey> reverseOf(Map<TypeKey, TypeValue> map)
	{
		var ret = new HashMap<TypeValue, TypeKey>();
		for(var entry : map.entrySet())
		{
			ret.put(entry.getValue(), entry.getKey());
		}
		return ret;
	}
	/**
	 * 将一个 List 内容顺序反转, 返回一个新的 List
	 * @since 7.25.0
	 * */
	public static <Type> List<Type> reverseOf(List<Type> list)
	{
		var count = sizeOf(list);
		var ret = new ArrayList<Type>(count);
		for(int step = count - 1; step >= 0; step--)
		{
			ret.add(list.get(step));
		}
		return ret;
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
	 * @since 6.16.0
	 * @apiNote 这个接口以后可能对更多类型做出支持, 但是相关改动将不会影响主版本号
	 * */
	public static int sizeOf(Object any)
	{
		if(any == null) return 0;
		else if(any instanceof Collection<?> collection) return collection.size();
		else if(any instanceof Map<?, ?> map) return map.size();
		else if(any.getClass().isArray()) return Array.getLength(any);
		else throw new IllegalArgumentException("given value is not a collection or array");
	}

	/**
	 * @since 6.16.0
	 * @apiNote 这个接口以后可能对更多类型做出支持, 但是相关改动将不会影响主版本号
	 * */
	public static boolean isEmpty(Object any)
	{
		if(any == null) return true;
		else if(any instanceof Collection<?> collection) return collection.isEmpty();
		else if(any instanceof Map<?, ?> map) return map.isEmpty();
		else if(any.getClass().isArray()) return Array.getLength(any) <= 0;
		else throw new IllegalArgumentException("given value is not a collection or array");
	}

	/**
	 * @since 6.16.0
	 * @apiNote 这个接口以后可能对更多类型做出支持, 但是相关改动将不会影响主版本号
	 * */
	public static boolean isNotEmpty(Object any)
	{
		if(any == null) return false;
		else if(any instanceof Collection<?> collection) return !collection.isEmpty();
		else if(any instanceof Map<?, ?> map) return !map.isEmpty();
		else if(any.getClass().isArray()) return Array.getLength(any) > 0;
		else throw new IllegalArgumentException("given value is not a collection or array");
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

	/**
	 * 提取出数据中符合条件的区间:
	 * [1, 2, 3, 4, 3, 2, 1] + (i)->i>=3 = [2, 4]
	 * @since 6.5.0
	 * */
	public static <T> int[][] rangesOf(T[] array, Predicate<T> predicate)
	{
		Objects.requireNonNull(array);
		Objects.requireNonNull(predicate);
		if(isEmpty(array)) return new int[0][];
		var status = predicate.test(array[0]);
		var startIndex = status ? 0 : -1;
		var ret = new ArrayList<int[]>();
		for(var step = 1; step < array.length; step++)
		{
			var currentStatus = predicate.test(array[step]);
			if(currentStatus != status)
			{
				if(status)
				{
					ret.add(new int[]{startIndex, step - 1});
				}
				else
				{
					startIndex = step;
				}
				status = currentStatus;
			}
		}
		if(status)
		{
			ret.add(new int[]{startIndex, array.length - 1});
		}

		return ret.toArray(new int[0][]);
	}

	/**
	 * 提取出数据中符合条件的区间:
	 * [1, 2, 3, 4, 3, 2, 1] + (i)->i>=3 = [2, 4]
	 * @since 6.5.0
	 * */
	public static <T> int[][] rangesOf(Iterable<T> data, Predicate<T> predicate)
	{
		Objects.requireNonNull(data);
		Objects.requireNonNull(predicate);
		var iter = data.iterator();
		if(!iter.hasNext()) return new int[0][];

		var first = iter.next();
		var status = predicate.test(first);
		var startIndex = status ? 0 : -1;
		var ret = new ArrayList<int[]>();
		var step = 1;
		while(iter.hasNext())
		{
			var currentStatus = predicate.test(iter.next());
			if(currentStatus != status)
			{
				if(status)
				{
					ret.add(new int[]{startIndex, step - 1});
				}
				else
				{
					startIndex = step;
				}
				status = currentStatus;
			}
			step++;
		}
		if(status)
		{
			ret.add(new int[]{startIndex, step - 1});
		}

		return ret.toArray(new int[0][]);
	}

	/**
	 * 将一个对象引用转换为数组引用
	 * @throws IllegalArgumentException 如果传入的引用为空或原本的类型不是数组类型, 将会抛出
	 * @since 6.16.0
	 * */
	public static Object[] toObjectArray(Object rawRef)
	{
		return (Object[]) toTypeArray(rawRef, Object.class);
	}

	/**
	 * 将一个对象引用转换为指定类型的数组引用
	 * @throws IllegalArgumentException 如果传入的引用为空或原本的类型不是数组类型, 将会抛出
	 * @since 6.16.0
	 * */
	public static <Type> Object toTypeArray(Object rawRef, Class<Type> classType)
	{
		if(rawRef == null || !rawRef.getClass().isArray()) throw new IllegalArgumentException("given param is not an array type ref");
		if(void.class == classType || Void.class == classType) throw new IllegalArgumentException("cannot convert to void type");
		var len = Array.getLength(rawRef);
		var ret = Array.newInstance(classType, len);
		for(var step = 0; step < len; step++)
		{
			var value = Array.get(rawRef, step);
			Array.set(ret, step, value);
		}
		return ret;
	}

	/**
	 * 判断集合中是否包含任何一个指定的元素
	 * @return 如果未指定任何元素, 直接返回 true
	 * @since 7.40.0
	 * */
	@SafeVarargs
	public static <Type> boolean containsAny(@NotNull Collection<Type> collection, @Nullable Type... items)
	{
		if(isEmpty(items)) return true;
		for(var item : items)
		{
			if(collection.contains(item)) return true;
		}
		return false;
	}
	/**
	 * 判断集合中是否包含任何一个指定的元素
	 * @return 如果未指定任何元素, 直接返回 true
	 * @since 7.40.0
	 * */
	@Overload
	public static <Type> boolean containsAny(@NotNull Collection<Type> collection, @Nullable Iterable<Type> items)
	{
		if(isEmpty(items)) return true;
		for(var item : items)
		{
			if(collection.contains(item)) return true;
		}
		return false;
	}

	/**
	 * 判断集合中是否包含所有指定元素
	 * @return 如果未指定任何元素, 直接返回 true
	 * @since 7.40.0
	 * */
	@SafeVarargs
    public static <Type> boolean containsAll(@NotNull Collection<Type> collection, @Nullable Type... items)
	{
		if(isEmpty(items)) return true;
		for(var item : items)
		{
			if(!collection.contains(item)) return false;
		}
		return true;
	}
	/**
	 * 判断集合中是否包含所有指定元素
	 * @return 如果未指定任何元素, 直接返回 true
	 * @since 7.40.0
	 * */
	@Overload
	public static <Type> boolean containsAll(@NotNull Collection<Type> collection, @Nullable Iterable<Type> items)
	{
		if(isEmpty(items)) return true;
		for(var item : items)
		{
			if(!collection.contains(item)) return false;
		}
		return true;
	}

	/**
	 * 判断集合中是否包含指定元素中的指定个数
	 * @since 7.41.0
	 * */
	@SafeVarargs
	public static <Type> boolean containsCount(@NotNull Collection<Type> collection, final int count, @Nullable Type... items)
	{
		int temp = 0;
		if(isNotEmpty(items))
		{
			for(var item : items)
			{
				if(collection.contains(item))
				{
					temp++;
				}
			}
		}
		return temp == count;
	}
	/**
	 * @since 7.41.0
	 * */
	@Overload
	public static <Type> boolean containsCount(@NotNull Collection<Type> collection, final int count, @Nullable Iterable<Type> items)
	{
		int temp = 0;
		if(isNotEmpty(items))
		{
			for(var item : items)
			{
				if(collection.contains(item))
				{
					temp++;
				}
			}
		}
		return temp == count;
	}

	/**
	 * 判断集合中是否包含指定元素中的某一个.
	 * 如果包含多余或少于一个都会报错
	 * @since 7.41.0
	 * */
	@Overload
	@SafeVarargs
	public static <Type> boolean containsOne(@NotNull Collection<Type> collection, @Nullable Type... items)
	{
		return containsCount(collection, 1, items);
	}
	/**
	 * @since 7.41.0
	 * */
	@Overload
	public static <Type> boolean containsOne(@NotNull Collection<Type> collection, @Nullable Iterable<Type> items)
	{
		return containsCount(collection, 1, items);
	}

}
