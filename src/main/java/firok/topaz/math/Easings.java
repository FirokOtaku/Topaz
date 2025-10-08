package firok.topaz.math;

import org.jetbrains.annotations.NotNull;

/// 渐变计算工具方法.
/// @implNote IDEA 会提示这个类有语法错误, 实际上可以过编, 没有问题
/// @implSpec 公式来源于 <a href="https://easings.net">easings.net</a>
/// @apiNote 所有的 <code>x</code> 形参都介于 0 到 1 之间
/// @since 5.2.0
/// @author Firok
@SuppressWarnings("SpellCheckingInspection")
public class Easings
{
	public static double easeInSine(double x)
	{
		return 1 - Math.cos((x * Math.PI) / 2);
	}
	public static double easeOutSine(double x)
	{
		return Math.sin((x * Math.PI) / 2);
	}
	public static double easeInOutSine(double x)
	{
		return -(Math.cos(Math.PI * x) - 1) / 2;
	}

	public static double easeInQuad(double x)
	{
		return x * x;
	}
	public static double easeOutQuad(double x)
	{
		return 1 - (1 - x) * (1 - x);
	}
	public static double easeInOutQuad(double x)
	{
		return x < 0.5 ? 2 * x * x : 1 - Math.pow(-2 * x + 2, 2) / 2;
	}

	public static double easeInCubic(double x)
	{
		return x * x * x;
	}
	public static double easeOutCubic(double x)
	{
		return 1 - Math.pow(1 - x, 3);
	}
	public static double easeInOutCubic(double x)
	{
		return x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2;
	}

	public static double easeInQuint(double x)
	{
		return x * x * x * x * x;
	}
	public static double easeOutQuint(double x)
	{
		return 1 - Math.pow(1 - x, 5);
	}
	public static double easeInOutQuint(double x)
	{
		return x < 0.5 ? 16 * x * x * x * x * x : 1 - Math.pow(-2 * x + 2, 5) / 2;
	}

	public static double easeInCirc(double x)
	{
		return 1 - Math.sqrt(1 - Math.pow(x, 2));
	}
	public static double easeOutCirc(double x)
	{
		return Math.sqrt(1 - Math.pow(x - 1, 2));
	}
	public static double easeInOutCirc(double x)
	{
		return x < 0.5
				? (1 - Math.sqrt(1 - Math.pow(2 * x, 2))) / 2
				: (Math.sqrt(1 - Math.pow(-2 * x + 2, 2)) + 1) / 2;
	}

	public static double easeInElastic(double x)
	{
		var c4 = (2 * Math.PI) / 3;
		return x == 0 ? 0
				: x == 1 ? 1
				: -Math.pow(2, 10 * x - 10) * Math.sin((x * 10 - 10.75) * c4);
	}
	public static double easeOutElastic(double x)
	{
		var c4 = (2 * Math.PI) / 3;
		return x == 0 ? 0
				: x == 1 ? 1
				: Math.pow(2, -10 * x) * Math.sin((x * 10 - 0.75) * c4) + 1;
	}
	public static double easeInOutElastic(double x)
	{
		var c5 = (2 * Math.PI) / 4.5;
		double temp = Math.sin((20 * x - 11.125) * c5);
		return x == 0 ? 0
				: x == 1 ? 1
				: x < 0.5 ? -(Math.pow(2, 20 * x - 10) * temp) / 2
				: (Math.pow(2, -20 * x + 10) * temp) / 2 + 1;
	}

	public static double easeInExpo(double x)
	{
		return x == 0 ? 0 : Math.pow(2, 10 * x - 10);
	}
	public static double easeOutExpo(double x)
	{
		return x == 1 ? 1 : 1 - Math.pow(2, -10 * x);
	}
	public static double easeInOutExpo(double x)
	{
		return x == 0 ? 0
				: x == 1 ? 1
				: x < 0.5 ? Math.pow(2, 20 * x - 10) / 2
				: (2 - Math.pow(2, -20 * x + 10)) / 2;
	}

	public static double easeInBack(double x)
	{
		var c1 = 1.70158;
		var c3 = c1 + 1;
		return c3 * x * x * x - c1 * x * x;
	}
	public static double easeOutBack(double x)
	{
		var c1 = 1.70158;
		var c3 = c1 + 1;
		return 1 + c3 * Math.pow(x - 1, 3) + c1 * Math.pow(x - 1, 2);
	}
	public static double easeInOutBack(double x)
	{
		var c1 = 1.70158;
		var c2 = c1 * 1.525;
		return x < 0.5 ? (Math.pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2
				: (Math.pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2;
	}

	public static double easeInBounce(double x)
	{
		return 1 - easeOutBounce(1 - x);
	}
	public static double easeOutBounce(double x)
	{
		var n1 = 7.5625;
		var d1 = 2.75;
		if (x < 1 / d1) return n1 * x * x;
		else if (x < 2 / d1) return n1 * (x -= 1.5 / d1) * x + 0.75;
		else if (x < 2.5 / d1) return n1 * (x -= 2.25 / d1) * x + 0.9375;
		else return n1 * (x -= 2.625 / d1) * x + 0.984375;
	}
	public static double easeInOutBounce(double x)
	{
		return x < 0.5 ? (1 - easeOutBounce(1 - 2 * x)) / 2
				: (1 + easeOutBounce(2 * x - 1)) / 2;
	}

	public enum Direction { In, Out, InOut, }
	public enum Method { Sine, Quad, Cubic, Quint, Circ, Elastic, Expo, Back, Bounce, }
	/**
	 * 工具方法的工具方法
	 * */
	public static double calculate(
			@NotNull Direction direction,
			@NotNull Method method,
			double x
	)
	{
		return switch (method) {
			case Sine -> switch (direction) {
				case In -> easeInSine(x);
				case Out -> easeOutSine(x);
				case InOut -> easeInOutSine(x);
			};
			case Quad -> switch (direction) {
				case In -> easeInQuad(x);
				case Out -> easeOutQuad(x);
				case InOut -> easeInOutQuad(x);
			};
			case Cubic -> switch (direction) {
				case In -> easeInCubic(x);
				case Out -> easeOutCubic(x);
				case InOut -> easeInOutCubic(x);
			};
			case Quint -> switch (direction) {
				case In -> easeInQuint(x);
				case Out -> easeOutQuint(x);
				case InOut -> easeInOutQuint(x);
			};
			case Circ -> switch (direction) {
				case In -> easeInCirc(x);
				case Out -> easeOutCirc(x);
				case InOut -> easeInOutCirc(x);
			};
			case Elastic -> switch (direction) {
				case In -> easeInElastic(x);
				case Out -> easeOutElastic(x);
				case InOut -> easeInOutElastic(x);
			};
			case Expo -> switch (direction) {
				case In -> easeInExpo(x);
				case Out -> easeOutExpo(x);
				case InOut -> easeInOutExpo(x);
			};
			case Back -> switch (direction) {
				case In -> easeInBack(x);
				case Out -> easeOutBack(x);
				case InOut -> easeInOutBack(x);
			};
			case Bounce -> switch (direction) {
				case In -> easeInBounce(x);
				case Out -> easeOutBounce(x);
				case InOut -> easeInOutBounce(x);
			};
		};
	}

	/**
	 * 根据字符串名称计算
	 * @return 如果传入的方法名不在包含范围内, 这个方法只会返回 {@code Double.NaN}, 但是不会抛出异常
	 * @since 6.6.0
	 * */
	public static double calculate(
			String method,
			double x
	)
	{
		return switch(method)
		{
			case "easeInCirc" -> easeInCirc(x);
			case "easeInExpo" -> easeInExpo(x);
			case "easeInBack" -> easeInBack(x);
			case "easeInSine" -> easeInSine(x);
			case "easeInQuad" -> easeInQuad(x);
			case "easeOutCubic" -> easeOutCubic(x);
			case "easeOutQuad" -> easeOutQuad(x);
			case "easeOutExpo" -> easeOutExpo(x);
			case "easeInOutBack" -> easeInOutBack(x);
			case "easeInOutBounce" -> easeInOutBounce(x);
			case "easeInQuint" -> easeInQuint(x);
			case "easeInOutSine" -> easeInOutSine(x);
			case "easeInOutCubic" -> easeInOutCubic(x);
			case "easeInBounce" -> easeInBounce(x);
			case "easeInOutCirc" -> easeInOutCirc(x);
			case "easeInOutElastic" -> easeInOutElastic(x);
			case "easeOutElastic" -> easeOutElastic(x);
			case "easeOutSine" -> easeOutSine(x);
			case "easeOutQuint" -> easeOutQuint(x);
			case "easeOutBounce" -> easeOutBounce(x);
			case "easeInElastic" -> easeInElastic(x);
			case "easeInOutQuad" -> easeInOutQuad(x);
			case "easeOutCirc" -> easeOutCirc(x);
			case "easeInOutQuint" -> easeInOutQuint(x);
			case "easeInCubic" -> easeInCubic(x);
			case "easeInOutExpo" -> easeInOutExpo(x);
			case "easeOutBack" -> easeOutBack(x);
			default -> Double.NaN;
		};
	}
}
