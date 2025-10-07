package firok.topaz.annotation;

import java.lang.annotation.*;

/**
 * 标注于类型, 表示该类型具有可变性 (mutability)
 * @since 8.0.0
 * @author Firok
 * */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Mutable
{
}
