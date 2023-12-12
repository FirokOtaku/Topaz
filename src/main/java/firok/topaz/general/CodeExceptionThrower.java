package firok.topaz.general;

import org.jetbrains.annotations.Nullable;

/**
 * 可以抛出异常的玩意
 * @since 7.0.0
 * @author Firok
 * @see firok.topaz.TopazExceptions 示例用法
 * */
public interface CodeExceptionThrower
{
    /**
     * 用于为异常类型标注一个唯一异常码. 与此相对的是每个异常码都可能对应任意变化的本地化异常信息
     * */
    int getExceptionCode();

    /**
     * 获取当前异常类型的本地化信息. 如果为空, 则创建和抛出的异常将不会带有本地化信息.
     * */
    @Nullable I18N getI18N();

    default <TypeAny> TypeAny occur()
    {
        return occur(null);
    }
    default <TypeAny> TypeAny occur(@Nullable Throwable exception)
    {
        var code = getExceptionCode();
        var i18n = getI18N();
        var msg = i18n == null ? null : i18n.localize("error-" + code);
        throw new CodeException(getExceptionCode(), msg, exception);
    }

    default void maybe(boolean expression)
    {
        maybe(expression, null);
    }
    default void maybe(boolean expression, @Nullable Throwable exception)
    {
        if(expression) occur(exception);
    }
}
