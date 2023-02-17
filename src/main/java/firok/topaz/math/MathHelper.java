package firok.topaz.math;

/**
 * 一个用数组缓存加速数学运算的工具类
 *
 * @implNote 多谢 Minecraft
 * @since 2.3.0
 * @author Firok
 * */
public class MathHelper
{
	private final double[] tableCos, tableSin, tableTan;
	/**
	 * @param poolSizeTrigonometry 三角函数缓存池容量
	 * */
	public MathHelper(int poolSizeTrigonometry)
	{
		if(poolSizeTrigonometry < 1)
			throw new IllegalArgumentException("poolSizeTrigonometry must be greater than 0");
		this.tableCos = new double[poolSizeTrigonometry];
		this.tableSin = new double[poolSizeTrigonometry];
		this.tableTan = new double[poolSizeTrigonometry];
		double multiFactor = 2 * Math.PI / poolSizeTrigonometry;
		for(int i = 0; i < poolSizeTrigonometry; i++)
		{
			this.tableCos[i] = Math.cos(i * multiFactor);
			this.tableSin[i] = Math.sin(i * multiFactor);
			this.tableTan[i] = Math.tan(i * multiFactor);
		}
	}

	/**
	 * @param radian 弧度
	 * */
	public double cos(double radian)
	{
		return tableCos[(int)( tableCos.length * Math.abs(radian) / 2 / Math.PI ) % tableSin.length];
	}
	/**
	 * @param radian 弧度
	 * */
	public double sin(double radian)
	{
		return (radian < 0 ? -1 : 1) * tableSin[(int)( tableSin.length * Math.abs(radian) / 2 / Math.PI ) % tableSin.length];
	}
	/**
	 * @param radian 弧度
	 * */
	public double tan(double radian)
	{
		return (radian < 0 ? -1 : 1) * tableTan[(int)( tableTan.length * Math.abs(radian) / 2 / Math.PI ) % tableSin.length];
	}
}
