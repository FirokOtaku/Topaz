package firok.topaz.annotation;

import org.intellij.lang.annotations.Language;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 标注某个内容仍在开发.<br>
 * <i>对于 Topaz 自身来说, 任何标注了 Indev 注解的内容产生的 breaking change 都不会影响主版本号.</i>
 * */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface Indev
{
	/**
	 * 当前是否可用 (功能完整)
	 * */
	boolean usable() default false;

	/**
	 * 是否是实验性 (如使用了不稳定的特性)
	 * @since 5.16.0
	 * */
	boolean experimental() default false;

	/**
	 * 描述信息
	 * */
	@Language("MARKDOWN")
	String[] description() default {};
}
