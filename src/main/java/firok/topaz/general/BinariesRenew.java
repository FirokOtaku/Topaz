package firok.topaz.general;

import firok.topaz.TopazExceptions;
import firok.topaz.general.converts.*;

import java.nio.ByteBuffer;
import java.util.UUID;

import static firok.topaz.general.Collections.sizeOf;

/**
 * 用来替换 {@link firok.topaz.general.Binaries} 的类
 * @since 3.6.0
 * @version 8.0.0
 * @author Firok
 * */
public final class BinariesRenew
{
    private BinariesRenew() { }

    private static final char[] HexChar = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'
    };
    private static char toHexChar(int b)
    {
        return HexChar[b & 0xF];
    }
    private static byte toHexByte(char c)
    {
        if(c >= '0' && c <= '9') return (byte) (c - '0');
        if(c >= 'A' && c <= 'F') return (byte) (c - 'A' + 10);
        if(c >= 'a' && c <= 'f') return (byte) (c - 'a' + 10);
        throw new IllegalArgumentException("invalid hex char: " + c);
    }
    public static String toHexString(byte[] buffer)
    {
        var ret = new char[buffer.length * 2];
        for (int step = 0; step < buffer.length; step ++)
        {
            ret[step * 2] = toHexChar(buffer[step] >>> 4);
            ret[step * 2 + 1] = toHexChar(buffer[step] & 0x0F);
        }
        return new String(ret);
    }
    public static byte[] toHexByte(String hex)
    {
        if(hex.length() % 2 != 0)
            throw new IllegalArgumentException("hex string length must be even");

        var ret = new byte[hex.length() / 2];
        for (int step = 0; step < ret.length; step++)
        {
            int b1 = toHexByte(hex.charAt(step * 2)) << 4;
            int b2 = toHexByte(hex.charAt(step * 2 + 1));
            ret[step] = (byte) (b1 | b2);
        }
        return ret;
    }


    /// 八进制整型转字符串
    ///
    /// 0x20201223 -> "20201223"
    /// @since 7.10.0
    public static String toDirectString(byte[] hexBytes)
    {
        var ret = new StringBuilder();
        for(byte b : hexBytes)
        {
            int num = Byte.toUnsignedInt(b);
            if(num < 10) ret.append('0');
            ret.append(num);
        }
        return ret.toString();
    }
    /// 字符串转八进制整型
    ///
    /// "20201223" -> 0x20201223
    /// @since 7.10.0
    public static byte[] toDirectByte(String hexString)
    {
        while (hexString.length() < 2)
        {
            hexString = "0" + hexString;
        }

        var len = hexString.length() / 2;
        var ret = new byte[len];
        for(int step = 0; step < len; step ++)
        {
            ret[step] = (byte) (Character.digit(hexString.charAt(step * 2), 16) * 10 + Character.digit(hexString.charAt(step * 2 + 1), 16));
        }
        return ret;
    }

    /**
     * 将 UUID 转换为二进制数据
     * @return 16 字节的二进制数据
     * @since 7.33.0
     * */
    public static byte[] toBytes(UUID uuid)
    {
        var bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
    /**
     * 将二进制数据转换为 UUID
     * @param bytes 16 字节的二进制数据.
     *              如果长度不为 16, 则会抛出异常
     * @since 7.33.0
     * */
    public static UUID toUUID(byte[] bytes)
    {
        int len = sizeOf(bytes);
        TopazExceptions.ParamFormatError.ifNotInRange(len, 16, 16);
        var bb = ByteBuffer.wrap(bytes);
        return new UUID(bb.getLong(), bb.getLong());
    }

    public static final TypeBytesConverter<Short> Short = new ShortBytesConverter();
    public static final TypeBytesConverter<Integer> Int = new IntegerBytesConverter();
    public static final TypeBytesConverter<Long> Long = new LongBytesConverter();
    /// @apiNote 请注意, 我们不对 [Float#NaN] 的转换结果做保证. 相关转换可能出现异常
    public static final TypeBytesConverter<Float> Float = new FloatBytesConverter();
    /// @apiNote 请注意, 我们不对 [Double#NaN] 的转换结果做保证. 相关转换可能出现异常
    public static final TypeBytesConverter<Double> Double = new DoubleBytesConverter();
//    public static final TypeBytesConverter<UUID> UUID; // todo 暂未实现

}
