package firok.topaz.math;

import firok.topaz.annotation.Level;
import firok.topaz.annotation.PerformanceIssue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

import static firok.topaz.general.Collections.toTypeArray;

/**
 * 一些数学运算工具方法
 *
 * @since 3.16.0
 * @author Firok
 * */
@SuppressWarnings({"unused", "RedundantLengthCheck", "DuplicatedCode"})
public final class Maths
{
	private Maths() { }

	public static <Type extends Comparable<Type>> Type range(Type value, Type min, Type max)
	{
		if(value.compareTo(max) > 0) return max;
		else if(value.compareTo(min) < 0) return min;
		return value;
	}
	public static <Type extends Comparable<Type>> boolean isInRange(Type value, Type min, Type max)
	{
		return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
	}

	/**
	 * 计算最大值
	 * @since 5.3.0
	 * @author Firok
	 * */
	@SafeVarargs
	public static <T extends Comparable<T>> T max(T... values)
	{
		if(values == null || values.length == 0)
			throw new IllegalArgumentException("values cannot be null");
		if(values.length == 1) return values[0];
		T max = null;
		for(var num : values)
		{
			if(max == null || num.compareTo(max) > 0)
			{
				max = num;
			}
		}
		return max;
	}
	/**
	 * 计算最小值
	 * @since 5.3.0
	 * @author Firok
	 * */
	@SafeVarargs
	public static <T extends Comparable<T>> T min(T... values)
	{
		if(values == null || values.length == 0)
			throw new IllegalArgumentException("values cannot be null");
		if(values.length == 1) return values[0];
		T min = null;
		for(var num : values)
		{
			if(min == null || num.compareTo(min) < 0)
			{
				min = num;
			}
		}
		return min;
	}

	private static final String MathMethodIssue = "为了方便, 现在的实现方式会在底层用反射创建一个副本然后复制数据, 这在数组容量比较大的时候可能有性能问题";
	/**
	 * 计算最大值
	 * @since 6.16.0
	 * */
	@PerformanceIssue(value = MathMethodIssue, level = Level.Minor)
	public static byte max(byte[] nums) { return max((Byte[]) toTypeArray(nums, Byte.class)); }
	/**
	 * 计算最大值
	 * @since 6.16.0
	 * */
	@PerformanceIssue(value = MathMethodIssue, level = Level.Minor)
	public static short max(short[] nums) { return max((Short[]) toTypeArray(nums, Short.class)); }
	/**
	 * 计算最大值
	 * @since 6.16.0
	 * */
	@PerformanceIssue(value = MathMethodIssue, level = Level.Minor)
	public static int max(int[] nums) { return max((Integer[]) toTypeArray(nums, Integer.class)); }
	/**
	 * 计算最大值
	 * @since 6.16.0
	 * */
	@PerformanceIssue(value = MathMethodIssue, level = Level.Minor)
	public static long max(long[] nums) { return max((Long[]) toTypeArray(nums, Long.class)); }
	/**
	 * 计算最大值
	 * @since 6.16.0
	 * */
	@PerformanceIssue(value = MathMethodIssue, level = Level.Minor)
	public static float max(float[] nums) { return max((Float[]) toTypeArray(nums, Float.class)); }
	/**
	 * 计算最大值
	 * @since 6.16.0
	 * */
	@PerformanceIssue(value = MathMethodIssue, level = Level.Minor)
	public static double max(double[] nums) { return max((Double[]) toTypeArray(nums, Double.class)); }
	/**
	 * 计算最大值
	 * @since 6.16.0
	 * */
	@PerformanceIssue(value = MathMethodIssue, level = Level.Minor)
	public static char max(char[] nums) { return max((Character[]) toTypeArray(nums, Character.class)); }

	/**
	 * 计算最小值
	 * @since 6.16.0
	 * */
	@PerformanceIssue(value = MathMethodIssue, level = Level.Minor)
	public static byte min(byte[] nums) { return min((Byte[]) toTypeArray(nums, Byte.class)); }
	/**
	 * 计算最小值
	 * @since 6.16.0
	 * */
	@PerformanceIssue(value = MathMethodIssue, level = Level.Minor)
	public static short min(short[] nums) { return min((Short[]) toTypeArray(nums, Short.class)); }
	/**
	 * 计算最小值
	 * @since 6.16.0
	 * */
	@PerformanceIssue(value = MathMethodIssue, level = Level.Minor)
	public static int min(int[] nums) { return min((Integer[]) toTypeArray(nums, Integer.class)); }
	/**
	 * 计算最小值
	 * @since 6.16.0
	 * */
	@PerformanceIssue(value = MathMethodIssue, level = Level.Minor)
	public static long min(long[] nums) { return min((Long[]) toTypeArray(nums, Long.class)); }
	/**
	 * 计算最小值
	 * @since 6.16.0
	 * */
	@PerformanceIssue(value = MathMethodIssue, level = Level.Minor)
	public static float min(float[] nums) { return min((Float[]) toTypeArray(nums, Float.class)); }
	/**
	 * 计算最小值
	 * @since 6.16.0
	 * */
	@PerformanceIssue(value = MathMethodIssue, level = Level.Minor)
	public static double min(double[] nums) { return min((Double[]) toTypeArray(nums, Double.class)); }
	/**
	 * 计算最小值
	 * @since 6.16.0
	 * */
	@PerformanceIssue(value = MathMethodIssue, level = Level.Minor)
	public static char min(char[] nums) { return min((Character[]) toTypeArray(nums, Character.class)); }

	/**
	 * 计算中间值
	 * @since 5.3.0
	 * @author Firok
	 * */
	public static <T extends Comparable<T>> T mid(T n1, T n2, T n3)
	{
		if(n1 == null || n2 == null || n3 == null)
			throw new IllegalArgumentException("numbers cannot be null");
		var list = new ArrayList<T>(3);
		list.add(n1);
		list.add(n2);
		list.add(n3);
		Collections.sort(list); // 这一切值得吗 那当然是能用就行了
		return list.get(1);
	}

	/**
	 * 不抛出异常的转换方法
	 * @return 如果转换失败就返回 null
	 * @since 5.18.0
	 * */
	public static Boolean parseBoolean(String raw)
	{
		try { return Boolean.parseBoolean(raw); }
		catch (Exception ignored) { return null; }
	}

	/**
	 * 转换方法, 转换失败就返回默认值
	 * @since 5.18.0
	 * */
	public static Boolean parseBoolean(String raw, boolean defaultValue)
	{
		try { return Boolean.parseBoolean(raw); }
		catch (Exception ignored) { return defaultValue; }
	}

	/**
	 * 不抛出异常的转换方法
	 * @return 如果转换失败就返回 null
	 * @since 5.18.0
	 * */
	public static Byte parseByte(String raw)
	{
		try { return Byte.parseByte(raw); }
		catch (Exception ignored) { return null; }
	}

	/**
	 * 转换方法, 转换失败就返回默认值
	 * @since 5.18.0
	 * */
	public static Byte parseByte(String raw, byte defaultValue)
	{
		try { return Byte.parseByte(raw); }
		catch (Exception ignored) { return defaultValue; }
	}

	/**
	 * 不抛出异常的转换方法
	 * @return 如果转换失败就返回 null
	 * @since 5.18.0
	 * */
	public static Short parseShort(String raw)
	{
		try { return Short.parseShort(raw); }
		catch (Exception ignored) { return null; }
	}

	/**
	 * 转换方法, 转换失败就返回默认值
	 * @since 5.18.0
	 * */
	public static Short parseShort(String raw, short defaultValue)
	{
		try { return Short.parseShort(raw); }
		catch (Exception ignored) { return defaultValue; }
	}

	/**
	 * 不抛出异常的转换方法
	 * @return 如果转换失败就返回 null
	 * @since 5.18.0
	 * */
	public static Integer parseInt(String raw)
	{
		try { return Integer.parseInt(raw); }
		catch (Exception ignored) { return null; }
	}

	/**
	 * 转换方法, 转换失败就返回默认值
	 * @since 5.18.0
	 * */
	public static Integer parseInt(String raw, int defaultValue)
	{
		try { return Integer.parseInt(raw); }
		catch (Exception ignored) { return defaultValue; }
	}

	/**
	 * 不抛出异常的转换方法
	 * @return 如果转换失败就返回 null
	 * @since 5.18.0
	 * */
	public static Long parseLong(String raw)
	{
		try { return Long.parseLong(raw); }
		catch (Exception ignored) { return null; }
	}

	/**
	 * 转换方法, 转换失败就返回默认值
	 * @since 5.18.0
	 * */
	public static Long parseLong(String raw, long defaultValue)
	{
		try { return Long.parseLong(raw); }
		catch (Exception ignored) { return defaultValue; }
	}

	/**
	 * 不抛出异常的转换方法
	 * @return 如果转换失败就返回 null
	 * @since 5.18.0
	 * */
	public static Float parseFloat(String raw)
	{
		try { return Float.parseFloat(raw); }
		catch (Exception ignored) { return null; }
	}

	/**
	 * 转换方法, 转换失败就返回默认值
	 * @since 5.18.0
	 * */
	public static Float parseFloat(String raw, float defaultValue)
	{
		try { return Float.parseFloat(raw); }
		catch (Exception ignored) { return defaultValue; }
	}

	/**
	 * 不抛出异常的转换方法
	 * @return 如果转换失败就返回 null
	 * @since 5.18.0
	 * */
	public static Double parseDouble(String raw)
	{
		try { return Double.parseDouble(raw); }
		catch (Exception ignored) { return null; }
	}

	/**
	 * 转换方法, 转换失败就返回默认值
	 * @since 5.18.0
	 * */
	public static Double parseDouble(String raw, double defaultValue)
	{
		try { return Double.parseDouble(raw); }
		catch (Exception ignored) { return defaultValue; }
	}

	/**
	 * 不抛出异常的转换方法
	 * @return 如果转换失败就返回 null
	 * @since 5.18.0
	 * */
	public static BigInteger parseBigInteger(String raw)
	{
		try { return new BigInteger(raw); }
		catch (Exception ignored) { return null; }
	}

	/**
	 * 转换方法, 转换失败就返回默认值
	 * @since 5.18.0
	 * */
	public static BigInteger parseBigInteger(String raw, BigInteger defaultValue)
	{
		try { return new BigInteger(raw); }
		catch (Exception ignored) { return defaultValue; }
	}

	/**
	 * 不抛出异常的转换方法
	 * @return 如果转换失败就返回 null
	 * @since 5.18.0
	 * */
	public static BigDecimal parseBigDecimal(String raw)
	{
		try { return new BigDecimal(raw); }
		catch (Exception ignored) { return null; }
	}

	/**
	 * 转换方法, 转换失败就返回默认值
	 * @since 5.18.0
	 * */
	public static BigDecimal parseBigDecimal(String raw, BigDecimal defaultValue)
	{
		try { return new BigDecimal(raw); }
		catch (Exception ignored) { return defaultValue; }
	}

	/**
	 * 均值拟合
	 * @param values 需要拟合的数据, 长度需要大于 0
	 * @param range 拟合范围, 需要大于参数 range
	 * @return 拟合之后的数据, 数组长度与参数 values 相同
	 * @since 6.15.0
	 * */
	public static double[] meanFit(double[] values, int range)
	{
		if(range <= 1) throw new IllegalArgumentException("range must be greater than 1");
		if(values == null || values.length <= range) throw new IllegalArgumentException("values length must be greater than range");

		double[] ret = new double[values.length];

		for(var step = 0; step < values.length; step++)
		{
			double sum = 0;
			for(var index = step - range; index <= step + range; index++)
			{
				if(index < 0)
					sum += 2 * values[0] - values[-index];
				else if(index > values.length - 1)
					sum += 2 * values[values.length - 1] - values[2 * (values.length - 1) - index];
				else
					sum += values[index];
			}
			ret[step] = sum / (range * 2 + 1);
		}

		return ret;
	}
}
