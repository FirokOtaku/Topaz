package firok.topaz.spring;

import firok.topaz.function.MayRunnable;
import firok.topaz.function.MaySupplier;
import firok.topaz.general.CodeException;
import firok.topaz.general.CodeExceptionContext;
import firok.topaz.internal.SerializableInfo;
import lombok.Data;

import java.io.Serial;
import java.util.concurrent.CompletableFuture;

/// 支持状态码的返回体
/// @see Ret<TypeData>
/// @see CodeException
/// @implNote 静态方法的 {@link #code} 参数都是值类型 int, 用来提醒调用者不要忘记状态码.
///           默认状态下的静态接口可能不符合你系统的运行逻辑, 比如你可能会想要自己实现一套静态接口, 用来提供默认状态码等.
/// @since 7.20.0
/// @version 8.0.0
/// @author Firok
@Data
@SuppressWarnings("unused")
public class CodeRet<TypeData> extends Ret<TypeData> implements java.io.Serializable
{
    @Serial
    private static final long serialVersionUID = SerializableInfo.SIDBase + 20000 + 1;

    /**
     * 请求状态码
     * */
    Integer code;

    public static <TypeData> CodeRet<TypeData> success(TypeData data, int code)
    {
        var ret = new CodeRet<TypeData>();
        ret.data = data;
        ret.success = true;
        ret.code = code;
        return ret;
    }

    public static <TypeData> CodeRet<TypeData> success(int code)
    {
        return success(null, code);
    }

    public static <TypeData> CodeRet<TypeData> fail(String msg, int code)
    {
        var ret = new CodeRet<TypeData>();
        ret.success = false;
        ret.msg = msg;
        ret.code = code;
        return ret;
    }

    public static <TypeData> CodeRet<TypeData> fail(int code)
    {
        return fail(null, code);
    }

    /// 执行相关函数, 将执行结果封装为 [CodeRet].
    ///
    /// 如果函数抛出的是一个 [CodeException],
    /// 则返回 [CodeRet] 的状态码为 [CodeException] 定义的错误码,
    /// 忽略 codeFail 参数
    ///
    /// @since 8.0.0
    public static <TypeData> CodeRet<TypeData> now(MaySupplier<TypeData> function, int codeSuccess, int codeFail)
    {
        try
        {
            var data = function.get();
            var ret = new CodeRet<TypeData>();
            ret.data = data;
            ret.success = true;
            ret.code = codeSuccess;
            return ret;
        }
        catch (CodeException ce)
        {
            return CodeRet.fail(ce.context.message(), ce.context.code().getExceptionCode());
        }
        catch (Exception e)
        {
            return CodeRet.fail(e.getMessage(), codeFail);
        }
    }
    /// 执行相关函数, 将执行结果封装为 [CodeRet].
    ///
    /// 如果函数抛出的是一个 [CodeException],
    /// 则返回 [CodeRet] 的状态码为 [CodeException] 定义的错误码,
    /// 忽略 codeFail 参数
    ///
    /// @since 8.0.0
    public static <TypeData> CodeRet<TypeData> now(MayRunnable function, int codeSuccess, int codeFail)
    {
        try
        {
            function.run();
            var ret = new CodeRet<TypeData>();
            ret.success = true;
            ret.code = codeSuccess;
            return ret;
        }
        catch (CodeException ce)
        {
            return CodeRet.fail(ce.context.message(), ce.context.code().getExceptionCode());
        }
        catch (Exception e)
        {
            return CodeRet.fail(e.getMessage(), codeFail);
        }
    }

    public static <TypeData> CompletableFuture<CodeRet<TypeData>> later(MaySupplier<TypeData> function, int codeSuccess, int codeFail)
    {
        return CompletableFuture.supplyAsync(() -> now(function, codeSuccess, codeFail));
    }
    public static <TypeData> CompletableFuture<CodeRet<TypeData>> later(MayRunnable function, int codeSuccess, int codeFail)
    {
        return CompletableFuture.supplyAsync(() -> now(function, codeSuccess, codeFail));
    }
}
