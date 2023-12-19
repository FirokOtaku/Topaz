package firok.topaz.function;

import firok.topaz.general.CodeException;
import firok.topaz.general.CodeExceptionThrower;

import java.util.function.Function;

/**
 * 可能会抛出异常的 {@link Function}
 * @since 6.18.0
 * @author Firok
 * @see Function
 * */
@FunctionalInterface
public interface MayFunction<TypeParam, TypeReturn>
{
    TypeReturn apply(TypeParam typeParam) throws Exception;

    /**
     * 尝试执行, 如果出现异常则抛出规定好的异常类型
     * @since 7.4.0
     * */
    default TypeReturn apply(TypeParam param, CodeExceptionThrower code) throws CodeException
    {
        try
        {
            return apply(param);
        }
        catch (Exception any)
        {
            return code.occur(any);
        }
    }

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
