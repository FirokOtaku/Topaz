package firok.topaz.annotation;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 声明该实体最低支持的 JDK 版本
 * @implNote {@code javax.annotation.processing.SupportedSourceVersion} 的语义化替代
 * @see SupportedSourceVersion
 * */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface SupportedMinimalVersion
{
    SourceVersion value();
}
