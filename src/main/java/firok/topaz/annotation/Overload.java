package firok.topaz.annotation;

import org.intellij.lang.annotations.Language;

import java.lang.annotation.*;

/**
 * 标明某个方法是其它方法的重载方法
 * @since 7.37.0
 * */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Overload
{
    /**
     * 可选的描述信息
     * */
    @Language("MARKDOWN")
    String[] description() default {};
}
