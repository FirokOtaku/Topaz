package firok.topaz.function;

import java.util.function.Function;

/**
 * @since 6.18.0
 * */
@FunctionalInterface
public interface MayFunction<TypeParam, TypeReturn>
{
    TypeReturn apply(TypeParam typeParam) throws Exception;

    default Function<TypeParam, TypeReturn> anyway()
    {
        return anyway(false);
    }
    default Function<TypeParam, TypeReturn> anyway(final boolean throwInternalException)
    {
        return (param) -> {
            try { return MayFunction.this.apply(param); }
            catch (Exception any) { if(throwInternalException) throw new RuntimeException(any); return null; }
        };
    }

    /**
     * 工具封装方法
     * @since 7.0.0
     * */
    static <TypeParam, TypeReturn> MayFunction<TypeParam, TypeReturn> that(MayFunction<TypeParam, TypeReturn> function)
    {
        return function;
    }
}
