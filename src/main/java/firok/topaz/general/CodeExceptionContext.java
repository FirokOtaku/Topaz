package firok.topaz.general;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/// {@link CodeException} 所用的错误信息实体类.
/// @param code 错误类型
/// @param cause 内部异常
/// @param message 错误信息重载
/// @param source 错误来源 (调用者引用, 或其它任何数据. 由使用者自行规定)
/// @param description 错误附加描述
///
/// @since 8.0.0
/// @author Firok
public record CodeExceptionContext(
        @NotNull CodeExceptionThrower code,

        @Nullable Throwable cause,

        @Nullable String message,

        @Nullable Object source,

        @Nullable String description
)
{
}
