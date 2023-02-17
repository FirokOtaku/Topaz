package firok.topaz.function;

import java.util.*;

/**
 * 函数式编程辅助接口
 * @author Firok
 * @since 3.23.0
 * */
@FunctionalInterface
public interface QuadriConsumer<Type1, Type2, Type3, Type4>
{
	void accept(Type1 param1, Type2 param2, Type3 param3, Type4 param4);

	default QuadriConsumer<Type1, Type2, Type3, Type4>
	andThen(QuadriConsumer<? super Type1, ? super Type2, ? super Type3, ? super Type4> after)
	{
		Objects.requireNonNull(after);

		return (a, b, c, d) -> {
			accept(a, b, c, d);
			after.accept(a, b, c, d);
		};
	}
}
