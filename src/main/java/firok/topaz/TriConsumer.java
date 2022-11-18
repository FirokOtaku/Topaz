package firok.topaz;

import java.util.*;

/**
 * 函数式编程辅助接口
 * @author Firok
 * @since 3.23.0
 * */
@FunctionalInterface
public interface TriConsumer<Type1, Type2, Type3>
{
	void accept(Type1 param1, Type2 param2, Type3 param3);

	default TriConsumer<Type1, Type2, Type3>
	andThen(TriConsumer<? super Type1, ? super Type2, ? super Type3> after)
	{
		Objects.requireNonNull(after);

		return (a, b, c) -> {
			accept(a, b, c);
			after.accept(a, b, c);
		};
	}
}
