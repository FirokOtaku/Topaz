package firok.topaz.math;

import firok.topaz.general.Collections;

/**
 * 多边形处理相关
 *
 * @since 3.15.0
 * @author Firok
 * */
public class Shapes
{
	/**
	 * 计算给定多边形端点是否为顺时针排列
	 *
	 * https://www.cnblogs.com/kyokuhuang/p/4250526.html
	 *
	 * @param xs x 坐标列表
	 * @param ys y 坐标列表
	 * */
	public static boolean isClockwise(double[] xs, double[] ys)
	{
		if(xs == null || ys == null || xs.length != ys.length)
			throw new IllegalArgumentException();

		double temp = 0;
		for(int step = 0; step < xs.length - 1; step++)
		{
			temp += -0.5 * (ys[step + 1] + ys[step]) * (xs[step + 1] - xs[step]);
		}
		return temp <= 0;
	}

	/**
	 * 使得给定多边形端点按照某方向排列
	 * @param value 是否为顺时针
	 * @param xs x 坐标列表
	 * @param ys y 坐标列表
	 * @implNote 就地处理数据
	 * */
	public static void makeClockwise(boolean value, double[] xs, double[] ys)
	{
		var isClockwiseOrigin = isClockwise(xs, ys);
		if(value == isClockwiseOrigin) return;
		Collections.makeReverse(xs);
		Collections.makeReverse(ys);
	}
}
