package firok.topaz.function;

import java.util.function.Supplier;

/**
 * 用来限定方法运行环境的条件
 * @since 6.10.0
 * @author Firok
 */
public interface Condition extends Supplier<Boolean>
{
}
