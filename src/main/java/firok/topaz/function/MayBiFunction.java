package firok.topaz.function;

import firok.topaz.general.CodeException;
import firok.topaz.general.CodeExceptionThrower;

import java.util.function.BiFunction;

/**
 * 可能会抛出异常的 {@link BiFunction}
 * @since 7.5.0
 * @author Firok
 * @see BiFunction
 * */
@FunctionalInterface
public interface MayBiFunction<TypeParamA, TypeParamB, TypeReturn>
{
    TypeReturn apply(TypeParamA paramA, TypeParamB paramB) throws Exception;

    /**
     * 尝试执行, 如果出现异常则抛出规定好的异常类型
     * */
    default TypeReturn apply(TypeParamA paramA, TypeParamB paramB, CodeExceptionThrower code) throws CodeException
    {
        try
        {
            return apply(paramA, paramB);
        }
        catch (Exception any)
        {
            return code.occur(any);
        }
    }


    /**
     * 生成一个不关心内部异常的玩意
     * @since 5.11.0
     * */
    default BiFunction<TypeParamA, TypeParamB, TypeReturn> anyway()
    {
        return anyway(false);
    }
    default BiFunction<TypeParamA, TypeParamB, TypeReturn> anyway(final boolean throwInternalException)
    {
        return (paramA, paramB) -> {
            try { return MayBiFunction.this.apply(paramA, paramB); }
            catch (Exception any)
            {
                if(throwInternalException) throw new RuntimeException(any);
                return null;
            }
        };
    }
    default BiFunction<TypeParamA, TypeParamB, TypeReturn> anyway(CodeExceptionThrower code)
    {
        return (paramA, paramB) -> {
            try { return MayBiFunction.this.apply(paramA, paramB); }
            catch (Exception any) { return code.occur(any); }
        };
    }

    static <TypeParamA, TypeParamB, TypeReturn>
    MayBiFunction<TypeParamA, TypeParamB, TypeReturn> that(MayBiFunction<TypeParamA, TypeParamB, TypeReturn> function)
    {
        return function;
    }
}
