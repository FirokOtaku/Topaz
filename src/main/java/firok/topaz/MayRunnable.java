package firok.topaz;

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
}
