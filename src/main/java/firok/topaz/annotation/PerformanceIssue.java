package firok.topaz.annotation;

import org.intellij.lang.annotations.Language;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 声明指定位置存在性能问题
 * */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface PerformanceIssue
{
	/**
	 * 可能存在问题的描述
	 * */
	@Language("MARKDOWN")
	String[] value() default {};

	/**
	 * 问题严重性
	 * */
	Level level() default Level.Unknown;

	/**
	 * 可能浪费的资源
	 * */
	Resource[] cost() default Resource.Unknown;
}
