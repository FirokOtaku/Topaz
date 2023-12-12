package firok.topaz.function;

import java.util.function.Supplier;

/**
 * @since 7.0.0
 * @author Firok
 * */
@FunctionalInterface
public interface MaySupplier<TypeReturn>
{
    TypeReturn get() throws Exception;

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
     * 工具封装方法
     * */
    static <TypeReturn> MaySupplier<TypeReturn> that(MaySupplier<TypeReturn> supplier)
    {
        return supplier;
    }
}
