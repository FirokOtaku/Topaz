package firok.topaz.function;

import firok.topaz.general.CodeException;
import firok.topaz.general.CodeExceptionThrower;

import java.util.function.Consumer;

/**
 * 可能会抛出异常的 {@link Consumer}
 * @since 4.1.0
 * @author Firok
 * @see Consumer
 * */
@FunctionalInterface
public interface MayConsumer<TypeParam>
{
	void accept(TypeParam param) throws Exception;

	/**
	 * 尝试执行, 如果出现异常则抛出规定好的异常类型
	 * @since 7.4.0
	 * */
	default void accept(TypeParam param, CodeExceptionThrower code) throws CodeException
	{
		try
		{
			accept(param);
		}
		catch (Exception any)
		{
			code.occur(any);
		}
	}

	/**
	 * 生成一个不关心内部异常的玩意
	 * @since 5.11.0
	 * */
	default Consumer<TypeParam> anyway()
	{
		return anyway(false);
	}
	/**
	 * @since 5.11.0
	 * */
	default Consumer<TypeParam> anyway(final boolean throwInternalException)
	{
		return (TypeParam param) -> {
			try { MayConsumer.this.accept(param); }
			catch (Exception any) { if(throwInternalException) throw new RuntimeException(any); }
		};
	}
	/**
	 * @since 7.5.0
	 * */
	default Consumer<TypeParam> anyway(CodeExceptionThrower code)
	{
		return (TypeParam param) -> {
			try { MayConsumer.this.accept(param); }
			catch (Exception any) { code.occur(any); }
		};
	}

	/**
	 * 工具封装方法
	 * @since 7.0.0
	 * */
	static <Type> MayConsumer<Type> that(MayConsumer<Type> consumer)
	{
		return consumer;
	}
}
