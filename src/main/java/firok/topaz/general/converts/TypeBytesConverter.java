package firok.topaz.general.converts;

import firok.topaz.TopazExceptions;
import firok.topaz.general.Collections;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/// 对于各个类型跟二进制数组之间的转换
///
/// @author Firok
/// @since 8.0.0
public interface TypeBytesConverter<TypeEntity extends Serializable>
{
    /// 类型转换为二进制数据之后占用多少个字节
    int sizeOf();

    /// 检查给定的二进制数据是否可以转换为指定类型
    default void checkBuffer(byte[] buffer)
    {
        // 如果给定的二进制数据容量小于最低容量, 抛出异常
        TopazExceptions.ParamFormatError.ifLesser(
                Collections.sizeOf(buffer),
                this.sizeOf()
        );
    }

    /// 将低字节序二进制数据转为指定类型
    ///
    /// @throws firok.topaz.general.CodeException 如果输入的二进制数据长度小于类型最低容量, 则抛出异常
    @NotNull TypeEntity fromLE(byte[] buffer);
    /// 将高字节序二进制数据转为指定类型
    ///
    /// @throws firok.topaz.general.CodeException 如果输入的二进制数据长度小于类型最低容量, 则抛出异常
    @NotNull TypeEntity fromBE(byte[] buffer);

    /// 将指定类型转为低字节序二进制数据
    byte[] toLE(@NotNull TypeEntity value);
    /// 将指定类型转为高字节序二进制数据
    byte[] toBE(@NotNull TypeEntity value);

    /// 以低字节序将数据写入输出流
    default void writeLE(@NotNull TypeEntity value, @NotNull OutputStream os) throws IOException
    {
        os.write(toLE(value));
    }
    /// 以高字节序将数据写入输出流
    default void writeBE(@NotNull TypeEntity value, @NotNull OutputStream os) throws IOException
    {
        os.write(toBE(value));
    }

    /// 从输入流中以低字节序读取数据
    default TypeEntity readLE(@NotNull InputStream is) throws IOException
    {
        var buffer = new byte[sizeOf()];
        var len = is.read(buffer);
        TopazExceptions.ParamFormatError.ifComparableNotEqual(len, sizeOf());
        return fromLE(buffer);
    }
    /// 从输入流中以高字节序读取数据
    default TypeEntity readBE(@NotNull InputStream is) throws IOException
    {
        var buffer = new byte[sizeOf()];
        var len = is.read(buffer);
        TopazExceptions.ParamFormatError.ifComparableNotEqual(len, sizeOf());
        return fromBE(buffer);
    }
}
