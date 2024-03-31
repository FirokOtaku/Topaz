package firok.topaz.indev;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @since 7.25.0
 * */
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationProcessingShell
{
    Class<? extends EndAnnotationProcessor> value();
}
