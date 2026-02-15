package firok.topaz.general.converts;

import firok.topaz.TopazExceptions;
import firok.topaz.annotation.Indev;
import firok.topaz.annotation.Level;
import firok.topaz.annotation.PerformanceIssue;
import firok.topaz.annotation.Resource;
import firok.topaz.general.Collections;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/// 对于各个 **定长** 类型跟二进制数组之间的转换.
///
/// 这个类可以用于实现数据容量固定的数据类型 (比如 [Integer], [Long], [Float] 等) 与二进制数据之间的转换和操作,
/// 不能用于处理变长数据容量的数据类型 (比如 [String], [java.math.BigInteger], [java.math.BigDecimal] 等).
///
/// @author Firok
/// @since 8.0.0
public interface TypeBytesConverter<TypeEntity extends Serializable>
{
    /// 类型转换为二进制数据之后占用多少个字节
    int sizeOf();

    /// 检查给定的二进制数据是否可以转换为指定类型
    default void checkBuffer(byte[] buffer, int offset)
    {
        // 如果给定的二进制数据容量小于最低容量, 抛出异常
        var lengthRemain = Collections.sizeOf(buffer) - offset;
        TopazExceptions.ParamFormatError.maybe(
                lengthRemain < this.sizeOf()
        );
    }

    /// 检查给定的二进制数据是否可以转换为多个给定类型数据实例
    @Indev(experimental = true)
    default void checkArrayBuffer(byte[] buffer, int offset)
    {
        // 如果给定的二进制数据不能整除类型字节容量, 抛出异常
        var lengthRemain = Collections.sizeOf(buffer) - offset;
        TopazExceptions.ParamFormatError.maybe(
                lengthRemain % this.sizeOf() != 0
        );
    }

    /// 将低字节序二进制数据转为指定类型
    ///
    /// @throws firok.topaz.general.CodeException 如果输入的二进制数据长度小于类型最低容量, 则抛出异常
    @NotNull TypeEntity fromLE(byte[] buffer, int offset);

    /// 将高字节序二进制数据转为指定类型
    ///
    /// @throws firok.topaz.general.CodeException 如果输入的二进制数据长度小于类型最低容量, 则抛出异常
    @NotNull TypeEntity fromBE(byte[] buffer, int offset);

    /// 将低字节序二进制数据转为指定类型
    ///
    /// @throws firok.topaz.general.CodeException 如果输入的二进制数据长度小于类型最低容量, 则抛出异常
    default @NotNull TypeEntity fromLE(byte[] buffer)
    {
        return this.fromLE(buffer, 0);
    }

    /// 将高字节序二进制数据转为指定类型
    ///
    /// @throws firok.topaz.general.CodeException 如果输入的二进制数据长度小于类型最低容量, 则抛出异常
    default @NotNull TypeEntity fromBE(byte[] buffer)
    {
        return this.fromBE(buffer, 0);
    }

    /// 将低字节序二进制数据转换为多个指定类型的列表
    default List<TypeEntity> fromLEArray(byte[] buffer, int offset)
    {
        checkArrayBuffer(buffer, offset);
        var len = buffer.length / this.sizeOf();
        var ret = new ArrayList<TypeEntity>();
        for(var step = 0; step < len; step++)
        {
            var bufferPart = new byte[this.sizeOf()];
            System.arraycopy(buffer, step * this.sizeOf(), bufferPart, 0, this.sizeOf());
            var value = this.fromLE(bufferPart);
            ret.add(value);
        }
        return ret;
    }
    /// 将高字节序二进制数据转换为多个指定类型的列表
    default List<TypeEntity> fromBEArray(byte[] buffer, int offset)
    {
        checkArrayBuffer(buffer, offset);
        var len = buffer.length / this.sizeOf();
        var ret = new ArrayList<TypeEntity>();
        for(var step = 0; step < len; step++)
        {
            var bufferPart = new byte[this.sizeOf()];
            System.arraycopy(buffer, step * this.sizeOf(), bufferPart, 0, this.sizeOf());
            var value = this.fromBE(bufferPart);
            ret.add(value);
        }
        return ret;
    }

    default List<TypeEntity> fromLEArray(byte[] buffer)
    {
        return this.fromLEArray(buffer, 0);
    }
    default List<TypeEntity> fromBEArray(byte[] buffer)
    {
        return this.fromBEArray(buffer, 0);
    }

    /// 将指定类型转为低字节序二进制数据, 写入到指定缓冲区的指定位置
    byte[] toLE(@NotNull TypeEntity value, byte[] buffer, int offset);
    /// 将指定类型转为高字节序二进制数据, 写入到指定缓冲区的指定位置
    byte[] toBE(@NotNull TypeEntity value, byte[] buffer, int offset);

    /// 将指定类型转为低字节序二进制数据, 写入到指定缓冲区
    default byte[] toLE(@NotNull TypeEntity value, byte[] buffer)
    {
        return toLE(value, buffer, 0);
    }
    /// 将指定类型转为高字节序二进制数据
    default byte[] toBE(@NotNull TypeEntity value, byte[] buffer)
    {
        return toBE(value, buffer, 0);
    }

    /// 将指定类型转为低字节序二进制数据, 写入到指定缓冲区
    default byte[] toLE(@NotNull TypeEntity value)
    {
        var buffer = new byte[this.sizeOf()];
        return toLE(value, buffer);
    }
    /// 将指定类型转为高字节序二进制数据
    default byte[] toBE(@NotNull TypeEntity value)
    {
        var buffer = new byte[this.sizeOf()];
        return toBE(value, buffer);
    }

    /// 将指定类型数组转为低字节序二进制数据
    default byte[] toLEArray(@NotNull List<TypeEntity> values)
    {
        var ret = new byte[values.size() * this.sizeOf()];
        for(var step = 0; step < values.size(); step++)
        {
            var buffer = toLE(values.get(step));
            System.arraycopy(buffer, 0, ret, step * this.sizeOf(), buffer.length);
        }
        return ret;
    }
    /// 将指定类型数组转为高字节序二进制数据
    default byte[] toBEArray(@NotNull List<TypeEntity> values)
    {
        var ret = new byte[values.size() * this.sizeOf()];
        for(var step = 0; step < values.size(); step++)
        {
            var buffer = toBE(values.get(step));
            System.arraycopy(buffer, 0, ret, step * this.sizeOf(), buffer.length);
        }
        return ret;
    }

    /// 以低字节序将数据写入输出流
    /// @apiNote 这个方法不会调用输出流的 [OutputStream#flush] 方法
    default void writeLE(@NotNull TypeEntity value, @NotNull OutputStream os) throws IOException
    {
        os.write(toLE(value));
    }
    /// 以高字节序将数据写入输出流
    /// @apiNote 这个方法不会调用输出流的 [OutputStream#flush] 方法
    default void writeBE(@NotNull TypeEntity value, @NotNull OutputStream os) throws IOException
    {
        os.write(toBE(value));
    }

    /// 以低字节序将若干数据写入输出流.
    /// 这个方法会先将所有数据转换为二进制数据 (缓存在内存里), 再一次性写入输出流.
    /// 如果数据量较大, 这个方法会占用大量内存
    ///
    /// @apiNote 这个方法不会调用输出流的 [OutputStream#flush] 方法
    /// @see #writeLEArrayIterative(List, OutputStream)
    @PerformanceIssue(level = Level.Common, cost = Resource.Mem)
    default void writeLEArrayWaterfall(@NotNull List<TypeEntity> values, @NotNull OutputStream os) throws IOException
    {
        var buffer = toLEArray(values);
        os.write(buffer);
    }
    /// 以低字节序将若干数据写入输出流.
    /// 这个方法会将所有数据逐个转换为二进制数据并写入输出流.
    /// 如果数据量较大, 这个方法会消耗大量时间和 CPU 算力
    ///
    /// @apiNote 这个方法不会调用输出流的 [OutputStream#flush] 方法
    /// @see #writeLEArrayWaterfall(List, OutputStream)
    @PerformanceIssue(level = Level.Common, cost = {Resource.Time, Resource.Cpu})
    default void writeLEArrayIterative(@NotNull List<TypeEntity> values, @NotNull OutputStream os) throws IOException
    {
        for(var value : values)
        {
            var buffer = toLE(value);
            os.write(buffer);
        }
    }
//    default void writeLEArrayBatch(@NotNull List<TypeEntity> values, @NotNull OutputStream os, int batchSize, boolean autoFlush) throws IOException
//    {}

    /// 以高字节序将若干数据写入输出流.
    /// 这个方法会先将所有数据转换为二进制数据 (缓存在内存里), 再一次性写入输出流.
    /// 如果数据量较大, 这个方法会占用大量内存
    ///
    /// @apiNote 这个方法不会调用输出流的 [OutputStream#flush] 方法
    /// @see #writeBEArrayIterative(List, OutputStream)
    @PerformanceIssue(level = Level.Common, cost = Resource.Mem)
    default void writeBEArrayWaterfall(@NotNull List<TypeEntity> values, @NotNull OutputStream os) throws IOException
    {
        var buffer = toBEArray(values);
        os.write(buffer);
    }
    /// 以高字节序将若干数据写入输出流.
    /// 这个方法会将所有数据逐个转换为二进制数据并写入输出流.
    /// 如果数据量较大, 这个方法会消耗大量时间和 CPU 算力
    ///
    /// @apiNote 这个方法不会调用输出流的 [OutputStream#flush] 方法
    /// @see #writeBEArrayWaterfall(List, OutputStream)
    @PerformanceIssue(level = Level.Common, cost = {Resource.Time, Resource.Cpu})
    default void writeBEArrayIterative(@NotNull List<TypeEntity> values, @NotNull OutputStream os) throws IOException
    {
        for(var value : values)
        {
            var buffer = toBE(value);
            os.write(buffer);
        }
    }

    /// 从输入流中以低字节序读取数据
    default TypeEntity readLE(@NotNull InputStream is) throws IOException
    {
        var _sizeOf = this.sizeOf();
        var buffer = new byte[_sizeOf];
        var len = is.read(buffer);
        TopazExceptions.ParamFormatError.ifComparableNotEqual(len, _sizeOf);
        return fromLE(buffer);
    }
    /// 从输入流中以高字节序读取数据
    default TypeEntity readBE(@NotNull InputStream is) throws IOException
    {
        var _sizeOf = this.sizeOf();
        var buffer = new byte[_sizeOf];
        var len = is.read(buffer);
        TopazExceptions.ParamFormatError.ifComparableNotEqual(len, _sizeOf);
        return fromBE(buffer);
    }

    default List<TypeEntity> readLEArrayWaterfall(@NotNull InputStream is, int count) throws IOException
    {
        var _sizeOf = this.sizeOf();
        var buffer = new byte[count * _sizeOf];
        var len = is.read(buffer);
        TopazExceptions.ParamFormatError.ifComparableNotEqual(len, count * _sizeOf);
        return fromLEArray(buffer);
    }
    default List<TypeEntity> readBEArrayWaterfall(@NotNull InputStream is, int count) throws IOException
    {
        var _sizeOf = this.sizeOf();
        var buffer = new byte[count * _sizeOf];
        var len = is.read(buffer);
        TopazExceptions.ParamFormatError.ifComparableNotEqual(len, count * _sizeOf);
        return fromBEArray(buffer);
    }

    default List<TypeEntity> readLEArrayIterative(@NotNull InputStream is, int count) throws IOException
    {
        var _sizeOf = this.sizeOf();
        var ret = new ArrayList<TypeEntity>(count);
        var buffer = new byte[_sizeOf];
        for(var step = 0; step < count; step++)
        {
            var len = is.read(buffer);
            TopazExceptions.ParamFormatError.ifComparableNotEqual(len, _sizeOf);
            ret.add(fromLE(buffer));
        }
        return ret;
    }
    default List<TypeEntity> readBEArrayIterative(@NotNull InputStream is, int count) throws IOException
    {
        var _sizeOf = this.sizeOf();
        var ret = new ArrayList<TypeEntity>(count);
        var buffer = new byte[_sizeOf];
        for(var step = 0; step < count; step++)
        {
            var len = is.read(buffer);
            TopazExceptions.ParamFormatError.ifComparableNotEqual(len, _sizeOf);
            ret.add(fromBE(buffer));
        }
        return ret;
    }
}
