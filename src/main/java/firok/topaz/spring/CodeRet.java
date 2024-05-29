package firok.topaz.spring;

import firok.topaz.function.MayRunnable;
import firok.topaz.function.MaySupplier;
import firok.topaz.general.CodeException;
import firok.topaz.internal.SerializableInfo;
import lombok.Data;

import java.io.Serial;
import java.util.concurrent.CompletableFuture;

/**
 * 支持状态码的返回体
 * @see Ret<TypeData>
 * @see CodeException
 * @implNote 静态方法的 {@link #code} 参数都是值类型 int, 用来提醒调用者不要忘记状态码.
 *           默认状态下的静态接口可能不符合你系统的运行逻辑, 比如你可能会想要自己实现一套静态接口, 用来提供默认状态码等.
 * @since 7.20.0
 * @author Firok
 * */
@Data
@SuppressWarnings("unused")
public class CodeRet<TypeData> extends Ret<TypeData> implements java.io.Serializable
{
    @SuppressWarnings("PointlessArithmeticExpression")
    @Serial
    private static final long serialVersionUID = SerializableInfo.SIDBase + 20000 + 0;

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

    /**
     * @apiNote 这个接口出来的 {@link CodeRet} 对象里的 {@link #code} 可能为 null
     * */
    public static <TypeData> CodeRet<TypeData> fail(Exception e)
    {
        var msg = e != null ? e.getLocalizedMessage() : null;
        var code = e instanceof CodeException ce ? ce.code : null;
        var ret = new CodeRet<TypeData>();
        ret.success = false;
        ret.msg = msg;
        ret.code = code;
        return ret;
    }

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
        catch (Exception e)
        {
            var ret = CodeRet.<TypeData>fail(e);
            if(ret.code == null) ret.code = codeFail;
            return ret;
        }
    }
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
        catch (Exception e)
        {
            var ret = CodeRet.<TypeData>fail(e);
            if(ret.code == null) ret.code = codeFail;
            return ret;
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
