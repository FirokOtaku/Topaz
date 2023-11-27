package firok.topaz.function;

import firok.topaz.annotation.Indev;

import java.util.function.Supplier;

/**
 * 用来限定方法运行环境的条件
 * @since 6.10.0
 * @author Firok
 */
@Indev(experimental = true, description = "暂时还不确定要不要加这么个玩意")
public interface Condition extends Supplier<Boolean>
{
}
