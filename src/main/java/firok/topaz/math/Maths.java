package firok.topaz.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 一些数学运算工具方法
 *
 * @since 3.16.0
 * @author Firok
 * */
public final class Maths
{
	private Maths() { }

	public static byte range(byte value, byte min, byte max)
	{
		if(value > max) return max;
		else if(value < min) return min;
		return value;
	}
	public static short range(short value, short min, short max)
	{
		if(value > max) return max;
		else if(value < min) return min;
		return value;
	}
	public static char range(char value, char min, char max)
	{
		if(value > max) return max;
		else if(value < min) return min;
		return value;
	}
	public static int range(int value, int min, int max)
	{
		if(value > max) return max;
		else if(value < min) return min;
		return value;
	}
	public static float range(float value, float min, float max)
	{
		if(value > max) return max;
		else if(value < min) return min;
		return value;
	}
	public static long range(long value, long min, long max)
	{
		if(value > max) return max;
		else if(value < min) return min;
		return value;
	}
	public static double range(double value, double min, double max)
	{
		if(value > max) return max;
		else if(value < min) return min;
		return value;
	}
	public static BigDecimal range(BigDecimal value, BigDecimal min, BigDecimal max)
	{
		if(value.compareTo(max) > 0) return max;
		else if(value.compareTo(min) < 0) return min;
		return value;
	}
	public static BigInteger range(BigInteger value, BigInteger min, BigInteger max)
	{
		if(value.compareTo(max) > 0) return max;
		else if(value.compareTo(min) < 0) return min;
		return value;
	}
	public static java.util.Date range(java.util.Date value, java.util.Date min, java.util.Date max)
	{
		if(value.compareTo(max) > 0) return max;
		else if(value.compareTo(min) < 0) return min;
		return value;
	}

	/**
	 * @since 3.20.0
	 * @author Firok
	 * */
	public static BigDecimal min(BigDecimal init, BigDecimal... numbers)
	{
		if(init == null)
			throw new IllegalArgumentException("init number cannot be null");

		var ret = init;

		if(numbers != null && numbers.length > 0)
		{
			for(var number : numbers)
			{
				if(number == null) continue;

				if(ret.compareTo(number) > 0)
					ret = number;
			}
		}

		return ret;
	}

	/**
	 * @since 3.20.0
	 * @author Firok
	 * */
	public static BigDecimal max(BigDecimal init, BigDecimal... numbers)
	{
		if(init == null)
			throw new IllegalArgumentException("init number cannot be null");

		var ret = init;

		if(numbers != null && numbers.length > 0)
		{
			for(var number : numbers)
			{
				if(number == null) continue;

				if(ret.compareTo(number) < 0)
					ret = number;
			}
		}

		return ret;
	}

	/**
	 * @since 3.20.0
	 * @author Firok
	 * */
	public static BigInteger min(BigInteger init, BigInteger... numbers)
	{
		if(init == null)
			throw new IllegalArgumentException("init number cannot be null");

		var ret = init;

		if(numbers != null && numbers.length > 0)
		{
			for(var number : numbers)
			{
				if(number == null) continue;

				if(ret.compareTo(number) > 0)
					ret = number;
			}
		}

		return ret;
	}

	/**
	 * @since 3.20.0
	 * @author Firok
	 * */
	public static BigInteger max(BigInteger init, BigInteger... numbers)
	{
		if(init == null)
			throw new IllegalArgumentException("init number cannot be null");

		var ret = init;

		if(numbers != null && numbers.length > 0)
		{
			for(var number : numbers)
			{
				if(number == null) continue;

				if(ret.compareTo(number) < 0)
					ret = number;
			}
		}

		return ret;
	}

	/**
	 * 计算最大值
	 * @since 5.3.0
	 * @author Firok
	 * */
	public static <T extends Number & Comparable<T>> T max(T... nums)
	{
		if(nums == null || nums.length == 0)
			throw new IllegalArgumentException("nums cannot be null");
		if(nums.length == 1) return nums[0];
		T max = null;
		for(var num : nums)
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
	public static <T extends Number & Comparable<T>> T min(T... nums)
	{
		if(nums == null || nums.length == 0)
			throw new IllegalArgumentException("nums cannot be null");
		if(nums.length == 1) return nums[0];
		T min = null;
		for(var num : nums)
		{
			if(min == null || num.compareTo(min) < 0)
			{
				min = num;
			}
		}
		return min;
	}

	/**
	 * 计算中间值
	 * @since 5.3.0
	 * @author Firok
	 * */
	public static <T extends Number & Comparable<T>> T mid(T n1, T n2, T n3)
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
}
