package firok.topaz.annotation;

import firok.topaz.reflection.Reflections;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 声明该实体最高支持的 JDK 版本
 * @implNote {@code javax.annotation.processing.SupportedSourceVersion} 的语义化替代
 * @see SupportedSourceVersion
 * @see Reflections#supportStatusOf
 * */

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface SupportedMaximalVersion
{
    SourceVersion value();
}
