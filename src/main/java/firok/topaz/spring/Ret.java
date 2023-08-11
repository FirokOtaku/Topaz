package firok.topaz.spring;

import firok.topaz.function.MayRunnable;
import lombok.Data;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * 这玩意给 Spring 项目用来包装返回数据格式用的
 *
 * <code>
 *     return Ret.success();
 * </code>
 *
 * @param <TypeData> 数据类型
 * @since 2.2.0
 * @version 5.8.0
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

	/**
	 * @since 5.8.0
	 * */
	public static <TypeData> Ret<TypeData> now(Callable<TypeData> function)
	{
		try
		{
			return Ret.success(function.call());
		}
		catch (Exception any)
		{
			return Ret.fail(any);
		}
	}
	/**
	 * @since 5.8.0
	 * */
	public static Ret<Void> now(MayRunnable function)
	{
		try
		{
			function.run();
			return Ret.success();
		}
		catch (Exception any)
		{
			return Ret.fail(any);
		}
	}
	/**
	 * @since 5.8.0
	 * */
	public static <TypeData> CompletableFuture<Ret<TypeData>> later(Callable<TypeData> function)
	{
		return CompletableFuture.supplyAsync(() -> Ret.now(function));
	}
	/**
	 * @since 5.8.0
	 * */
	public static CompletableFuture<Ret<Void>> later(MayRunnable function)
	{
		return CompletableFuture.supplyAsync(() -> Ret.now(function));
	}
}
