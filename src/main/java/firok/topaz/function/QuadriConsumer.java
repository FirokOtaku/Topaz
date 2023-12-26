package firok.topaz.function;

import java.util.*;

/**
 * 函数式编程辅助接口
 * @author Firok
 * @since 3.23.0
 * */
@FunctionalInterface
public interface QuadriConsumer<TypeParamA, TypeParamB, TypeParamC, TypeParamD>
{
	void accept(TypeParamA paramA, TypeParamB paramB, TypeParamC paramC, TypeParamD paramD);

	default QuadriConsumer<TypeParamA, TypeParamB, TypeParamC, TypeParamD>
	andThen(QuadriConsumer<? super TypeParamA, ? super TypeParamB, ? super TypeParamC, ? super TypeParamD> after)
	{
		Objects.requireNonNull(after);

		return (TypeParamA paramA, TypeParamB paramB, TypeParamC paramC, TypeParamD paramD) -> {
			accept(paramA, paramB, paramC, paramD);
			after.accept(paramA, paramB, paramC, paramD);
		};
	}
}
