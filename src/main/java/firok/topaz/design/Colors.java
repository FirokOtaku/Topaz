package firok.topaz.design;

import firok.topaz.math.Maths;

import java.awt.*;

/**
 * util class for color computing
 *
 * @since 3.11.0
 * @author Firok
 */
@SuppressWarnings("all")
public final class Colors
{
	private Colors(){}

	/**
	 * 非常简易的中间色计算工具方法
	 * */
	public static Color getColorBetween(Color c1,Color c2)
	{
		return new Color(
				(c1.getRed()+c2.getRed())/2,
				(c1.getGreen()+c2.getGreen())/2,
				(c1.getBlue()+c2.getBlue())/2);
	}
	/**
	 * 非常简易的中间色计算工具方法
	 * */
	public static int getColorRGBBetween(int c1,int c2)
	{
		return getColorBetween(new Color(c1),new Color(c2)).getRGB();
	}

	/**
	 * 计算色彩饱和度
	 * @params r,g,b 介于 0 到 1 之间的 RGB 通道量
	 * @since 5.4.0
	 * @author Firok
	 * */
	public static float saturationOf(float r, float g, float b)
	{
		float max = Maths.max(r, g, b), min = Maths.min(r, g, b);
		return 1 - min / max;
	}

	/**
	 * 计算色彩亮度
	 * @params r,g,b 介于 0 到 1 之间的 RGB 通道量
	 * @since 5.4.0
	 * @author Firok
	 * */
	public static float lightnessOf(float r, float g, float b)
	{
		return r * 0.299F + g * 0.587F + b * 0.114F;
	}

	/**
	 * 计算色彩灰度
	 * @params r,g,b 介于 0 到 1 之间的 RGB 通道量
	 * @since 5.4.0
	 * @author Firok
	 * */
	public static float grayOf(float r, float g, float b)
	{
		return (r + g + b) / 3;
	}

	/**
	 * 计算色彩色相
	 * <table>
	 *     <tr>
	 *         <td></td>
	 *         <td> 0° - <span style="color: rgb(255,0,0)">R</span> </td>
	 *         <td></td>
	 *     </tr>
	 *     <tr>
	 *         <td> 300° - <span style="color: rgb(255,0,255)">M</span> </td>
	 *         <td>  </td>
	 *         <td> 60° - <span style="color: rgb(255,255,0)">Y</span> </td>
	 *     </tr>
	 *     <tr>
	 *         <td> 240° - <span style="color: rgb(0,0,255)">B</span> </td>
	 *         <td>  </td>
	 *         <td> 120° - <span style="color: rgb(0,255,0)">G</span> </td>
	 *     </tr>
	 *     <tr>
	 *         <td></td>
	 *         <td> 180° - <span style="color: rgb(0,255,255)">C</span> </td>
	 *         <td></td>
	 *     </tr>
	 * </table>
	 * @params r,g,b 介于 0 到 1 之间的 RGB 通道量
	 * @return 色相度数
	 * @since 5.4.0
	 * @author Firok
	 * */
	public static float hueOf(float r, float g, float b)
	{
		final float max = Maths.max(r, g, b), min = Maths.min(r, g, b);
		final float interval = max - min;
		if(max == min) return 0;
		else if(max == r && g >= b) return 60F * (g - b) / interval + 0F;
		else if(max == r && g < b) return 60F * (g - b) / interval + 360F;
		else if(max == g) return 60F * (b - r) / interval + 120F;
		else if(max == b) return 60F * (r - g) / interval + 240F;
		else return Float.NaN; // 虽然应该不会到这里
	}
}
