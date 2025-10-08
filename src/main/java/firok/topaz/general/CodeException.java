package firok.topaz.general;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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
    /**
     * 一个描述性信息, 用来导致此异常的数据
     * */
    @NotNull
    public final CodeExceptionContext context;
    CodeException(@NotNull CodeExceptionContext context)
    {
        super(context.message(), context.cause());
        this.context = context;
    }

	/**
	 * 判断是否是指定的异常
	 * @since 7.39.0
	 * */
	public boolean is(CodeExceptionThrower code)
	{
		return Objects.equals(this.context.code(), code);
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
