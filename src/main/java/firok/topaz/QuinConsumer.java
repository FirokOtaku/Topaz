package firok.topaz;

import java.util.*;

/**
 * 函数式编程辅助接口
 * @author Firok
 * @since 3.23.0
 * */
@FunctionalInterface
public interface QuinConsumer<Type1, Type2, Type3, Type4, Type5> // quin!
{
	void accept(Type1 param1, Type2 param2, Type3 param3, Type4 param4, Type5 param5);

	default QuinConsumer<Type1, Type2, Type3, Type4, Type5>
	andThen(QuinConsumer<? super Type1, ? super Type2, ? super Type3, ? super Type4, ? super Type5> after)
	{
		Objects.requireNonNull(after);

		return (a, b, c, d, e) -> {
			accept(a, b, c, d, e);
			after.accept(a, b, c, d, e);
		};
	}
}
