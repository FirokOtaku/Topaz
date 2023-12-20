package firok.topaz.function;

import firok.topaz.general.CodeException;
import firok.topaz.general.CodeExceptionThrower;

/**
 * 可能会抛出异常的三参数 {@link TriFunction}
 * @since 7.5.0
 * @author Firok
 * @see TriFunction
 * */
@FunctionalInterface
public interface MayTriFunction<TypeParamA, TypeParamB, TypeParamC, TypeReturn>
{
    TypeReturn apply(TypeParamA paramA, TypeParamB paramB, TypeParamC paramC) throws Exception;

    /**
     * 尝试执行, 如果出现异常则抛出规定好的异常类型
     * */
    default TypeReturn apply(TypeParamA paramA, TypeParamB paramB, TypeParamC paramC, CodeExceptionThrower code) throws CodeException
    {
        try
        {
            return apply(paramA, paramB, paramC);
        }
        catch (Exception any)
        {
            return code.occur(any);
        }
    }

    /**
     * 生成一个不关心内部异常的玩意
     * */
    default TriFunction<TypeParamA, TypeParamB, TypeParamC, TypeReturn> anyway()
    {
        return anyway(false);
    }
    default TriFunction<TypeParamA, TypeParamB, TypeParamC, TypeReturn> anyway(final boolean throwInternalException)
    {
        return (paramA, paramB, paramC) -> {
            try { return MayTriFunction.this.apply(paramA, paramB, paramC); }
            catch (Exception any)
            {
                if(throwInternalException) throw new RuntimeException(any);
                return null;
            }
        };
    }
    default TriFunction<TypeParamA, TypeParamB, TypeParamC, TypeReturn> anyway(CodeExceptionThrower code)
    {
        return (paramA, paramB, paramC) -> {
            try { return MayTriFunction.this.apply(paramA, paramB, paramC); }
            catch (Exception any) { return code.occur(any); }
        };
    }

    static <TypeParamA, TypeParamB, TypeParamC, TypeReturn>
    MayTriFunction<TypeParamA, TypeParamB, TypeParamC, TypeReturn> that(MayTriFunction<TypeParamA, TypeParamB, TypeParamC, TypeReturn> function)
    {
        return function;
    }
}
