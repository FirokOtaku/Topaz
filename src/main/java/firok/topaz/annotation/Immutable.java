package firok.topaz.annotation;

import java.lang.annotation.*;

/**
 * 标注于类型, 表示该类型具有不可变性 (immutability)
 * @since 8.0.0
 * @author Firok
 * */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Immutable
{
}
