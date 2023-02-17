package firok.topaz.general;

/**
 * 带错误码的异常信息
 *
 * <h3>直接抛出异常</h3>
 * <code lang="java">
 *     return CodeException.occur(Errors.ERR_404);
 * </code>
 *
 * <h3>根据条件抛出异常</h3>
 * <code lang="java">
 *     CodeException.maybe(user == null, Errors.ERR_USER_NOT_FOUND);
 * </code>
 *
 * @since 3.27.0
 * @author Firok
 * */
public class CodeException extends RuntimeException
{
	public final int code;
	public CodeException(int code)
	{
		super();
		this.code = code;
	}
	public CodeException(int code, String msg)
	{
		super(msg);
		this.code = code;
	}
	public CodeException(int code, String msg, Throwable exception)
	{
		super(msg, exception);
		this.code = code;
	}

	// 为了让这个方法可以用在 if-else 里直接抛出异常而不用写别的 return 语句
	public static <TypeAny> TypeAny occur(int code)
	{
		throw new CodeException(code);
	}
	public static <TypeAny> TypeAny occur(int code, String msg)
	{
		throw new CodeException(code, msg);
	}
	public static <TypeAny> TypeAny occur(int code, String msg, Throwable exception)
	{
		throw new CodeException(code, msg, exception);
	}

	public static void maybe(boolean expression, int code)
	{
		if(expression)
			throw new CodeException(code);
	}
	public static void maybe(boolean expression, int code, String msg)
	{
		if(expression)
			throw new CodeException(code, msg);
	}
}
