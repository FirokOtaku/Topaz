package firok.topaz.spring;

import lombok.Data;

/**
 * 这玩意给 Spring 项目用来封装返回数据格式用的
 *
 * <code>
 *     return Ret.success();
 * </code>
 *
 * @param <TypeData> 数据类型
 * @since 2.2.0
 * @author Firok
 * @implNote 你用着顺手不顺手不重要 我用着顺手
 */
@Data
@SuppressWarnings("unused")
public class Ret<TypeData>
{
	/**
	 * 封装数据
	 */
	TypeData data;

	/**
	 * 操作是否成功
	 */
	boolean success;

	/**
	 * 如果操作失败 这个存一下错误信息
	 */
	String msg;

	public static <TypeData> Ret<TypeData> success(TypeData data)
	{
		Ret<TypeData> ret = new Ret<>();
		ret.data = data;
		ret.success = true;
		return ret;
	}
	public static <TypeData> Ret<TypeData> success()
	{
		return success(null);
	}
	public static <TypeData> Ret<TypeData> fail(String msg)
	{
		Ret<TypeData> ret = new Ret<>();
		ret.success = false;
		ret.msg = msg;
		return ret;
	}
	public static <TypeData> Ret<TypeData> fail()
	{
		return fail((String) null);
	}
	public static <TypeData> Ret<TypeData> fail(Exception e) { return fail(e.getMessage()); }
}
