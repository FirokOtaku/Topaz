package firok.topaz.function;

import java.util.*;

/**
 * 函数式编程辅助接口
 * @author Firok
 * @since 3.23.0
 * */
@FunctionalInterface
public interface QuinConsumer<TypeParamA, TypeParamB, TypeParamC, TypeParamD, TypeParamE> // quin!
{
	void accept(TypeParamA paramA, TypeParamB paramB, TypeParamC paramC, TypeParamD paramD, TypeParamE paramE);

	default QuinConsumer<TypeParamA, TypeParamB, TypeParamC, TypeParamD, TypeParamE>
	andThen(QuinConsumer<? super TypeParamA, ? super TypeParamB, ? super TypeParamC, ? super TypeParamD, ? super TypeParamE> after)
	{
		Objects.requireNonNull(after);

		return (TypeParamA paramA, TypeParamB paramB, TypeParamC paramC, TypeParamD paramD, TypeParamE paramE) -> {
			accept(paramA, paramB, paramC, paramD, paramE);
			after.accept(paramA, paramB, paramC, paramD, paramE);
		};
	}
}
