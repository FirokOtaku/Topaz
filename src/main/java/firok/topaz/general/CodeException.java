package firok.topaz.general;

import org.jetbrains.annotations.Contract;

/**
 * 带错误码的异常信息
 *
 * @see CodeExceptionThrower 推荐使用此实现方式创建 CodeException
 * @apiNote 推荐调用者不要直接覆盖 {@link #getMessage()} 字段的内容, 而是在 {@link #getCause()} 里储存更多信息
 * @since 3.27.0
 * @version 7.0.0
 * @author Firok
 * */
public class CodeException extends RuntimeException
{
	public final int code;
    /**
     * 一个描述性信息, 用来导致此异常的数据
     * */
    public final Object source;
    public final String message;
//    public CodeException(int code, Object data)
//    {
//        super();
//        this.code = code;
//        this.data = data;
//    }
//    public CodeException(int code)
//    {
//        this(code, null);
//    }
//    public CodeException(int code, String msg, Object data)
//    {
//        super(msg);
//        this.code = code;
//        this.data = data;
//    }
//	public CodeException(int code, String msg)
//	{
//		this(code, msg, null);
//	}
//	public CodeException(int code, String msg, Throwable exception)
//	{
//		super(msg, exception);
//		this.code = code;
//	}
    CodeException(int code, Object source, String message)
    {
        this.code = code;
        this.source = source;
        this.message = message;
    }

	// 为了让这个方法可以用在 if-else 里直接抛出异常而不用写别的 return 语句
	/**
	 * 直接抛出一个异常
	 * @deprecated 推荐使用 {@link CodeExceptionThrower#occur()}
	 * */
	@Deprecated
	@Contract("_ -> fail")
	public static <TypeAny> TypeAny occur(int code)
	{
		throw new CodeException(code, null, null); // fixme
	}
	/**
	 * 视情况抛出一个异常
	 * @deprecated 推荐使用 {@link CodeExceptionThrower#maybe(boolean)}
	 * */
	@Contract("true, _ -> fail")
	public static void maybe(boolean expression, int code)
	{
		if(expression)
            throw new CodeException(code, null, null); // fixme
	}

	/**
	 * 判断是否是指定的异常
	 * @since 7.39.0
	 * */
	public boolean is(CodeExceptionThrower thrower)
	{
		return this.code == thrower.getExceptionCode();
	}

	/**
	 * @since 7.22.0
	 * */
	@Override
	public String getMessage()
	{
		var ret = new StringBuilder();
		ret.append(super.getMessage());
		Throwable cause = getCause();
		while(cause != null)
		{
			ret.append(": ").append(cause.getMessage());
			cause = cause.getCause();
		}
		return ret.toString();
	}

	/**
	 * @since 7.22.0
	 * */
	@Override
	public String getLocalizedMessage()
	{
		var ret = new StringBuilder();
		ret.append(super.getLocalizedMessage());
		Throwable cause = getCause();
		while(cause != null)
		{
			ret.append(": ").append(cause.getLocalizedMessage());
			cause = cause.getCause();
		}
		return ret.toString();
	}
}
