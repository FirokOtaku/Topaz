package firok.topaz.function;

import firok.topaz.TopazExceptions;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static firok.topaz.general.Collections.isEmpty;

/**
 * 逻辑运算相关的工具.
 *
 * Vavr 项目中 <em>应该</em> 有类似的功能, 但是一直没看 Vavr 项目的文档, 自己了实现一套先用着.
 * @since 7.3.0
 * @author Firok
 * */
public final class Logics
{
    private Logics() { }

    /**
     * @throws firok.topaz.general.CodeException 当 conditions 为空时
     * */
    public static boolean And(boolean... conditions)
    {
        TopazExceptions.LogicsExpressionEmpty.ifEmpty(conditions);

        for(var condition : conditions)
        {
            if(!condition)
                return false;
        }
        return true;
    }

    /**
     * @throws firok.topaz.general.CodeException 当 conditions 为空或包含任何 null 元素时
     * */
    public static boolean And(Supplier<Boolean>... conditions)
    {
        TopazExceptions.LogicsExpressionEmpty.ifEmpty(conditions);

        for(var condition : conditions)
        {
            TopazExceptions.LogicsExpressionEmpty.ifNull(condition);

            if(!condition.get())
                return false;
        }
        return true;
    }

    /**
     * @throws firok.topaz.general.CodeException 当 conditions 为空或包含任何 null 元素时
     * */
    public static boolean And(MaySupplier<Boolean>... conditions) throws Exception
    {
        TopazExceptions.LogicsExpressionEmpty.maybe(isEmpty(conditions));

        for(var condition : conditions)
        {
            TopazExceptions.LogicsExpressionEmpty.maybe(condition == null);

            if(!condition.get())
                return false;
        }
        return true;
    }

    /**
     * @throws firok.topaz.general.CodeException 当 conditions 为空时
     * */
    public static boolean Or(boolean... conditions)
    {
        TopazExceptions.LogicsExpressionEmpty.maybe(isEmpty(conditions));

        for(var condition : conditions)
        {
            if(condition)
                return true;
        }
        return false;
    }

    /**
     * @throws firok.topaz.general.CodeException 当 conditions 为空或包含任何 null 元素时
     * */
    public static boolean Or(Supplier<Boolean>... conditions)
    {
        TopazExceptions.LogicsExpressionEmpty.ifEmpty(conditions);

        for(var condition : conditions)
        {
            TopazExceptions.LogicsExpressionEmpty.ifNull(condition);

            if(condition.get())
                return true;
        }
        return false;
    }

    /**
     * @throws firok.topaz.general.CodeException 当 conditions 为空或包含任何 null 元素时
     * */
    public static boolean Or(MaySupplier<Boolean>... conditions) throws Exception
    {
        TopazExceptions.LogicsExpressionEmpty.ifEmpty(conditions);

        for(var condition : conditions)
        {
            TopazExceptions.LogicsExpressionEmpty.ifNull(condition);

            if(condition.get())
                return true;
        }
        return false;
    }

    /**
     * @throws firok.topaz.general.CodeException 当 conditions 为空时
     * */
    public static boolean Xor(boolean... conditions)
    {
        TopazExceptions.LogicsExpressionEmpty.ifEmpty(conditions);

        boolean result = false;
        for(var condition : conditions)
        {
            if(condition)
            {
                if(result)
                    return false;
                else
                    result = true;
            }
        }
        return result;
    }

    /**
     * @throws firok.topaz.general.CodeException 当 conditions 为空或包含任何 null 元素时
     * */
    public static boolean Xor(Supplier<Boolean>... conditions)
    {
        TopazExceptions.LogicsExpressionEmpty.ifEmpty(conditions);

        boolean result = false;
        for(var condition : conditions)
        {
            TopazExceptions.LogicsExpressionEmpty.ifNull(condition);

            if(condition.get())
            {
                if(result)
                    return false;
                else
                    result = true;
            }
        }
        return result;
    }

    /**
     * @throws firok.topaz.general.CodeException 当 conditions 为空或包含任何 null 元素时
     * */
    public static boolean Xor(MaySupplier<Boolean>... conditions) throws Exception
    {
        TopazExceptions.LogicsExpressionEmpty.ifEmpty(conditions);

        boolean result = false;
        for(var condition : conditions)
        {
            TopazExceptions.LogicsExpressionEmpty.ifNull(condition);

            if(condition.get())
            {
                if(result)
                    return false;
                else
                    result = true;
            }
        }
        return result;
    }

    /**
     * @since 7.50.0
     * */
    public static <TypeBean> boolean And(Collection<TypeBean> beans, Predicate<TypeBean> function)
    {
        TopazExceptions.LogicsExpressionEmpty.ifEmpty(beans);

        for(var bean : beans)
        {
            if(!function.test(bean))
                return false;
        }
        return true;
    }
    /**
     * @since 7.50.0
     * */
    public static <TypeBean> boolean And(TypeBean[] beans, Predicate<TypeBean> function)
    {
        TopazExceptions.LogicsExpressionEmpty.ifEmpty(beans);

        for(var bean : beans)
        {
            if(!function.test(bean))
                return false;
        }
        return true;
    }

    /**
     * @since 7.50.0
     * */
    public static <TypeBean> boolean Or(Collection<TypeBean> beans, Predicate<TypeBean> function)
    {
        TopazExceptions.LogicsExpressionEmpty.ifEmpty(beans);

        for(var bean : beans)
        {
            if(function.test(bean))
                return true;
        }
        return false;
    }
    /**
     * @since 7.50.0
     * */
    public static <TypeBean> boolean Or(TypeBean[] beans, Predicate<TypeBean> function)
    {
        TopazExceptions.LogicsExpressionEmpty.ifEmpty(beans);

        for(var bean : beans)
        {
            if(function.test(bean))
                return true;
        }
        return false;
    }
}
