package firok.topaz.function;

import firok.topaz.general.CodeException;
import firok.topaz.general.CodeExceptionThrower;

/**
 * 执行过程可能抛出异常的 {@link Runnable}
 * @since 3.14.0
 * @author Firok
 * @see Runnable
 * */
@FunctionalInterface
public interface MayRunnable
{
	void run() throws Exception;

	/**
	 * 尝试执行, 如果出现异常则抛出规定好的异常类型
	 * @since 7.4.0
	 * */
	default void run(CodeExceptionThrower code) throws CodeException
	{
		try
		{
			run();
		}
		catch (Exception any)
		{
			code.occur(any);
		}
	}

	/**
	 * 生成一个不关心内部异常的玩意
	 * @since 3.25.0
	 * */
	default Runnable anyway()
	{
		return anyway(false);
	}
	/**
	 * @since 3.25.0
	 * */
	default Runnable anyway(final boolean throwInternalException)
	{
		return () -> {
			try { MayRunnable.this.run(); }
			catch (Exception any) { if(throwInternalException) throw new RuntimeException(any); }
		};
	}
	/**
	 * @since 7.5.0
	 * */
	default Runnable anyway(CodeExceptionThrower code)
	{
		return () -> {
			try { MayRunnable.this.run(); }
			catch (Exception any) { code.occur(any); }
		};
	}

	/**
	 * 工具封装方法
	 * @since 7.0.0
	 * */
	static MayRunnable that(MayRunnable runnable)
	{
		return runnable;
	}
}
