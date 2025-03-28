package firok.topaz.annotation;

import org.intellij.lang.annotations.Language;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 标记指定内容为易变内容
 * */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface Unstable
{
    /**
     * 当前内容的变动是否影响主版本号
     * */
    boolean affectMajorVersion() default true;

    /**
     * 当前内容的变动频率
     * */
    Level speed() default Level.Unknown;

    /**
     * 描述信息
     * */
    @Language("MARKDOWN")
    String[] description() default {};
}
