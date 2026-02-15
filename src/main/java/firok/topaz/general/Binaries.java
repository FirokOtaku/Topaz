package firok.topaz.general;

import firok.topaz.TopazExceptions;
import firok.topaz.general.converts.*;

/**
 * 二进制数据相关操作
 * @since 3.6.0
 * @version 8.0.0
 * @author Firok
 * */
public final class Binaries
{
    private Binaries() { }

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
        return TopazExceptions.ParamFormatError.occur(); // "invalid hex char: " + c
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
        TopazExceptions.ParamFormatError.maybe(hex.length() % 2 != 0); // hex string length must be even

        var ret = new byte[hex.length() / 2];
        for (int step = 0; step < ret.length; step++)
        {
            int b1 = toHexByte(hex.charAt(step * 2)) << 4;
            int b2 = toHexByte(hex.charAt(step * 2 + 1));
            ret[step] = (byte) (b1 | b2);
        }
        return ret;
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
