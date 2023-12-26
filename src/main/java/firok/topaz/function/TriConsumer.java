package firok.topaz.function;

import java.util.*;

/**
 * 函数式编程辅助接口
 * @author Firok
 * @since 3.23.0
 * */
@FunctionalInterface
public interface TriConsumer<TypeParamA, TypeParamB, TypeParamC>
{
	void accept(TypeParamA paramA, TypeParamB paramB, TypeParamC paramC);

	default TriConsumer<TypeParamA, TypeParamB, TypeParamC>
	andThen(TriConsumer<? super TypeParamA, ? super TypeParamB, ? super TypeParamC> after)
	{
		Objects.requireNonNull(after);

		return (TypeParamA paramA, TypeParamB paramB, TypeParamC paramC) -> {
			accept(paramA, paramB, paramC);
			after.accept(paramA, paramB, paramC);
		};
	}
}
