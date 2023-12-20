package firok.topaz.function;

import firok.topaz.general.CodeException;
import firok.topaz.general.CodeExceptionThrower;

import java.util.function.Supplier;

/**
 * 可能会抛出异常的 {@link Supplier}
 * @since 7.0.0
 * @author Firok
 * @see Supplier
 * */
@FunctionalInterface
public interface MaySupplier<TypeReturn>
{
    TypeReturn get() throws Exception;

    /**
     * 尝试执行, 如果出现异常则抛出规定好的异常类型
     * @since 7.4.0
     * */
    default TypeReturn get(CodeExceptionThrower code) throws CodeException
    {
        try
        {
            return get();
        }
        catch (Exception any)
        {
            return code.occur(any);
        }
    }

    /**
     * 生成一个不关心内部异常的玩意
     * */
    default Supplier<TypeReturn> anyway()
    {
        return anyway(false);
    }
    default Supplier<TypeReturn> anyway(final boolean throwInternalException)
    {
        return () -> {
            try { return MaySupplier.this.get(); }
            catch (Exception any) { if(throwInternalException) throw new RuntimeException(any); return null; }
        };
    }
    /**
     * @since 7.5.0
     * */
    default Supplier<TypeReturn> anyway(CodeExceptionThrower code)
    {
        return () -> {
            try { return MaySupplier.this.get(); }
            catch (Exception any) { return code.occur(any); }
        };
    }

    /**
     * 工具封装方法
     * */
    static <TypeReturn> MaySupplier<TypeReturn> that(MaySupplier<TypeReturn> supplier)
    {
        return supplier;
    }
}
