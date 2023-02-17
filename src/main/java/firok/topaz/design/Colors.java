package firok.topaz.design;

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
}
