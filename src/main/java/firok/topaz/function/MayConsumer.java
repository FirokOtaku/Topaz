package firok.topaz.function;

import java.util.function.Consumer;

/**
 * @since 4.1.0
 * @author Firok
 * */
public interface MayConsumer<Type>
{
	void accept(Type param) throws Exception;

	/**
	 * 生成一个不关心内部异常的玩意
	 * @since 5.11.0
	 * */
	default Consumer<Type> anyway()
	{
		return anyway(false);
	}
	/**
	 * @since 5.11.0
	 * */
	default Consumer<Type> anyway(final boolean throwInternalException)
	{
		return (param) -> {
			try { MayConsumer.this.accept(param); }
			catch (Exception any) { if(throwInternalException) throw new RuntimeException(any); }
		};
	}
}
