package firok.topaz.general;

import firok.topaz.math.Maths;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/// 可以抛出异常的玩意.
/// 推荐直接让枚举类实现此接口, 然后在枚举类中编写系统中可能存在的错误类型和对应的错误码.
///
/// @since 7.0.0
/// @version 8.0.0
/// @author Firok
/// @see firok.topaz.TopazExceptions 示例用法
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

    /// 抛出此异常. 不包含内部异常信息
    @Contract("-> fail")
    default <TypeAny> TypeAny occur()
    {
        var context = new CodeExceptionContext(
                this,
                null,
                null,
                null,
                null
        );
        throw new CodeException(context);
    }
    /// 抛出此异常. 包含内部异常信息
    @Contract("_ -> fail")
    default <TypeAny> TypeAny occur(@Nullable Throwable exception)
    {
        var context = new CodeExceptionContext(
                this,
                exception,
                null,
                null,
                null
        );
        throw new CodeException(context);
    }
    /// 抛出此异常. 包含上下文信息
    @Contract("_ -> fail")
    default <TypeAny> TypeAny occur(@Nullable CodeExceptionContext context)
    {
        var contextNew = new CodeExceptionContext(
                this,
                context != null ? context.cause() : null,
                context != null ? context.message() : null,
                context != null ? context.source() : null,
                context != null ? context.description() : null
        );
        throw new CodeException(contextNew);
    }

    /// 如果表达式为真, 则抛出此 CodeException. 不包含内部异常信息
    @Contract("true -> fail")
    default void maybe(boolean expression)
    {
        if(expression)
            occur();
    }

    /// 如果表达式为真, 则抛出此 CodeException. 包含内部异常信息
    @Contract("true, _ -> fail")
    default void maybe(boolean expression, @Nullable Throwable exception)
    {
        if(expression)
            occur(exception);
    }
    /// 如果表达式为真, 则抛出此 CodeException. 包含上下文信息
    @Contract("true, _ -> fail")
    default void maybe(boolean expression, @Nullable CodeExceptionContext context)
    {
        if(expression)
            occur(context);
    }

    /// 如果表达式为假, 则抛出此 CodeException. 不包含内部异常信息
    /// @since 8.0.0
    @Contract("false -> fail")
    default void unless(boolean expression)
    {
        maybe(!expression);
    }

    /// 如果表达式为假, 则抛出此 CodeException. 包含内部异常信息
    /// @since 8.0.0
    @Contract("false, _ -> fail")
    default void unless(boolean expression, @Nullable Throwable exception)
    {
        maybe(!expression, exception);
    }

    /// 如果表达式为假, 则抛出此 CodeException. 包含上下文信息
    /// @since 8.0.0
    @Contract("false, _ -> fail")
    default void unless(boolean expression, @Nullable CodeExceptionContext context)
    {
        maybe(!expression, context);
    }

    /**
     * 如果对象为 null, 则抛出此 CodeException
     * @since 7.48.0
     * */
    @Contract("null -> fail")
    default void ifNull(Object obj)
    {
        maybe(obj == null);
    }

    /**
     * 如果对象不为 null, 则抛出此 CodeException
     * @since 7.48.0
     * */
    @Contract("!null -> fail")
    default void ifNotNull(Object obj)
    {
        maybe(obj != null);
    }

    /**
     * 如果集合为空, 则抛出此 CodeException
     * @since 7.23.0
     * @see Collections#isEmpty(Object)
     * */
    @Contract("null -> fail")
    default void ifEmpty(Object obj)
    {
        maybe(Collections.isEmpty(obj));
    }

    /**
     * 如果集合不为空, 则抛出此 CodeException
     * @since 7.23.0
     * @see Collections#isNotEmpty(Object)
     * */
    @Contract("!null -> fail")
    default void ifNotEmpty(Object obj)
    {
        maybe(Collections.isNotEmpty(obj));
    }

    /**
     * 如果对象在指定范围内, 则抛出此 CodeException
     * @since 7.23.0
     * @see Maths#isInRange(Comparable, Comparable, Comparable)
     * */
    default <Type extends Comparable<Type>> void ifInRange(Type value, Type min, Type max)
    {
        maybe(Maths.isInRange(value, min, max));
    }

    /**
     * 如果对象不在指定范围内, 则抛出此 CodeException
     * @since 7.23.0
     * @see Maths#isInRange(Comparable, Comparable, Comparable)
     * */
    default <Type extends Comparable<Type>> void ifNotInRange(Type value, Type min, Type max)
    {
        maybe(!Maths.isInRange(value, min, max));
    }

    /**
     * 如果对象小于指定值, 则抛出此 CodeException
     * @since 7.29.0
     * */
    default <Type extends Comparable<Type>> void ifLesser(Type value, Type min)
    {
        maybe(value.compareTo(min) < 0);
    }

    /**
     * 如果对象小于等于指定值, 则抛出此 CodeException
     * @since 7.29.0
     * */
    default <Type extends Comparable<Type>> void ifLesserOrEqual(Type value, Type min)
    {
        maybe(value.compareTo(min) <= 0);
    }

    /**
     * 如果对象大于指定值, 则抛出此 CodeException
     * @since 7.29.0
     * */
    default <Type extends Comparable<Type>> void ifGreater(Type value, Type max)
    {
        maybe(value.compareTo(max) > 0);
    }

    /**
     * 如果对象大于等于指定值, 则抛出此 CodeException
     * @since 7.29.0
     * */
    default <Type extends Comparable<Type>> void ifGreaterOrEqual(Type value, Type max)
    {
        maybe(value.compareTo(max) >= 0);
    }

    /**
     * 如果两个参数相等, 则抛出此 CodeException
     * @since 7.33.0
     * */
    default <Type extends Comparable<Type>> void ifComparableEqual(Type value1, Type value2)
    {
        maybe(value1.compareTo(value2) == 0);
    }
    /**
     * 如果两个参数不相等, 则抛出此 CodeException
     * @since 7.33.0
     * */
    default <Type extends Comparable<Type>> void ifComparableNotEqual(Type value1, Type value2)
    {
        maybe(value1.compareTo(value2) != 0);
    }

    /**
     * 如果两个参数相等, 则抛出此 CodeException
     * @since 7.33.0
     * */
    default <Type> void ifObjectEqual(Type value1, Type value2)
    {
        maybe(Objects.equals(value1, value2));
    }
    /**
     * 如果两个参数不相等, 则抛出此 CodeException
     * @since 7.33.0
     * */
    default <Type> void ifObjectNotEqual(Type value1, Type value2)
    {
        maybe(!Objects.equals(value1, value2));
    }

    /**
     * 如果两个参数的哈希值相等, 则抛出此 CodeException
     * @since 7.33.0
     * */
    default <Type> void ifHashEqual(Type value1, Type value2)
    {
        maybe(Objects.hash(value1) == Objects.hash(value2));
    }
    /**
     * 如果两个参数的哈希值不相等, 则抛出此 CodeException
     * @since 7.33.0
     * */
    default <Type> void ifHashNotEqual(Type value1, Type value2)
    {
        maybe(Objects.hash(value1) != Objects.hash(value2));
    }
}
