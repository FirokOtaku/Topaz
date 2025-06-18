package firok.topaz.annotation;

import java.lang.annotation.*;

/**
 * 标记某个类或方法 <b>不是</b> 线程安全的
 * @see ThreadSafe
 * @since 7.51.0
 * @author Firok
 * */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface NotThreadSafe
{
}
