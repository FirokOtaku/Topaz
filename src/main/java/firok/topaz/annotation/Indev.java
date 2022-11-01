package firok.topaz.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 标注某个内容仍在开发
 * */
@Retention(RetentionPolicy.SOURCE)
public @interface Indev
{
	/**
	 * 当前是否可用 (功能完整)
	 * */
	boolean usable() default false;

	/**
	 * 描述信息
	 * */
	String[] description() default {};
}
