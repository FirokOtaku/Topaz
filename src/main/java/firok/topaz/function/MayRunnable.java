package firok.topaz.function;

/**
 * 执行过程可能抛出异常的函数
 * @see Runnable
 * @since 3.14.0
 * @author Firok
 * */
@FunctionalInterface
public interface MayRunnable
{
	void run() throws Exception;

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
	 * 工具封装方法
	 * @since 7.0.0
	 * */
	static MayRunnable that(MayRunnable runnable)
	{
		return runnable;
	}
}
