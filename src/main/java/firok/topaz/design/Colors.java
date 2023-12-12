package firok.topaz.design;

import firok.topaz.TopazExceptions;
import firok.topaz.math.Maths;

import java.awt.*;

import static firok.topaz.math.Maths.isInRange;

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
	 * @since 6.15.0
	 * */
	public static Color getColorBetween(Color c1, Color c2, double factor)
	{
		if(!isInRange(factor, 0D, 1D)) TopazExceptions.ColorValueOutOfRange.occur();

		int r1 = c1.getRed(), r2 = c2.getRed(), fr = Math.abs(r1 - r2), tr = (int) (fr * factor + Math.min(r2, r1));
		int g1 = c1.getGreen(), g2 = c2.getGreen(), fg = Math.abs(g1 - g2), tg = (int) (fg * factor + Math.min(g2, g1));
		int b1 = c1.getBlue(), b2 = c2.getBlue(), fb = Math.abs(b1 - b2), tb = (int) (fb * factor + Math.min(b2, b1));
		int a1 = c1.getAlpha(), a2 = c2.getAlpha(), fa = Math.abs(a1 - a2), ta = (int) (fa * factor + Math.min(a2, a1));
		return new Color(tr, tg, tb, ta);
	}

	/**
	 * 非常简易的中间色计算工具方法
	 */
	public static Color getColorBetween(Color c1, Color c2)
	{
		return getColorBetween(c1, c2, 0.5);
	}

	/**
	 * 非常简易的中间色计算工具方法
	 */
	public static int getColorRGBBetween(int c1, int c2, double factor)
	{
		return getColorBetween(new Color(c1), new Color(c2), factor).getRGB();
	}

	/**
	 * 非常简易的中间色计算工具方法
	 */
	public static int getColorRGBBetween(int c1, int c2)
	{
		return getColorRGBBetween(c1, c2, 0.5);
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
