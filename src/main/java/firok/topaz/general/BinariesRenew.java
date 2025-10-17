package firok.topaz.general;

import firok.topaz.TopazExceptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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


    /**
     * 0x20201223 -> "20201223"
     * @since 7.10.0
     * */
    public static String toDirectString(byte[] bytes)
    {
        var ret = new StringBuilder();
        for(byte b : bytes)
        {
            int num = Byte.toUnsignedInt(b);
            if(num < 10) ret.append('0');
            ret.append(num);
        }
        return ret.toString();
    }
    /**
     * "20201223" -> 0x20201223
     * @since 7.10.0
     * */
    public static byte[] toDirectByte(String str)
    {
        while (str.length() < 2)
        {
            str = "0" + str;
        }

        var len = str.length() / 2;
        var ret = new byte[len];
        for(int step = 0; step < len; step ++)
        {
            ret[step] = (byte) (Character.digit(str.charAt(step * 2), 16) * 10 + Character.digit(str.charAt(step * 2 + 1), 16));
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

    /// 将值以低字节序转为字节数组
    /// @since 8.0.0
    public static byte[] toBytesLE(short value)
    {
        return new byte[] {
                (byte) (value & 0xFF),
                (byte) (value >> 8 & 0xFF)
        };
    }
    /// 将值以低字节序转为字节数组
    /// @since 8.0.0
    public static byte[] toBytesLE(int value)
    {
        return new byte[] {
                (byte) (value & 0xFF),
                (byte) (value >> 8 & 0xFF),
                (byte) (value >> 16 & 0xFF),
                (byte) (value >> 24 & 0xFF)
        };
    }
    /// 将值以低字节序转为字节数组
    /// @since 8.0.0
    public static byte[] toBytesLE(long value)
    {
        return new byte[] {
                (byte) (value & 0xFF),
                (byte) (value >> 8 & 0xFF),
                (byte) (value >> 16 & 0xFF),
                (byte) (value >> 24 & 0xFF),
                (byte) (value >> 32 & 0xFF),
                (byte) (value >> 40 & 0xFF),
                (byte) (value >> 48 & 0xFF),
                (byte) (value >> 56 & 0xFF)
        };
    }
    /// 将值以低字节序转为字节数组
    /// @since 8.0.0
    public static byte[] toBytesLE(float value)
    {
        return toBytesLE(Float.floatToRawIntBits(value));
    }
    /// 将值以低字节序转为字节数组
    /// @since 8.0.0
    public static byte[] toBytesLE(double value)
    {
        return toBytesLE(Double.doubleToRawLongBits(value));
    }

    /// 将值以高字节序转为字节数组
    /// @since 8.0.0
    public static byte[] toBytesBE(short value)
    {
        return new byte[] {
                (byte) (value >> 8 & 0xFF),
                (byte) (value & 0xFF)
        };
    }
    /// 将值以高字节序转为字节数组
    /// @since 8.0.0
    public static byte[] toBytesBE(int value)
    {
        return new byte[] {
                (byte) (value >> 24 & 0xFF),
                (byte) (value >> 16 & 0xFF),
                (byte) (value >> 8 & 0xFF),
                (byte) (value & 0xFF)
        };
    }
    /// 将值以高字节序转为字节数组
    /// @since 8.0.0
    public static byte[] toBytesBE(long value)
    {
        return new byte[] {
                (byte) (value >> 56 & 0xFF),
                (byte) (value >> 48 & 0xFF),
                (byte) (value >> 40 & 0xFF),
                (byte) (value >> 32 & 0xFF),
                (byte) (value >> 24 & 0xFF),
                (byte) (value >> 16 & 0xFF),
                (byte) (value >> 8 & 0xFF),
                (byte) (value & 0xFF)
        };
    }
    /// 将值以高字节序转为字节数组
    /// @since 8.0.0
    public static byte[] toBytesBE(float value)
    {
        return toBytesBE(Float.floatToRawIntBits(value));
    }
    /// 将值以高字节序转为字节数组
    /// @since 8.0.0
    public static byte[] toBytesBE(double value)
    {
        return toBytesBE(Double.doubleToRawLongBits(value));
    }

    /// 将值以低字节序写入流
    /// @since 8.0.0
    public static void writeShortLE(short value, OutputStream os) throws IOException
    {
        os.write(toBytesLE(value));
    }
    /// 将值以低字节序写入流
    /// @since 8.0.0
    public static void writeIntLE(int value, OutputStream os) throws IOException
    {
        os.write(toBytesLE(value));
    }
    /// 将值以低字节序写入流
    /// @since 8.0.0
    public static void writeLongLE(long value, OutputStream os) throws IOException
    {
        os.write(toBytesLE(value));
    }
    /// 将值以低字节序写入流
    /// @since 8.0.0
    public static void writeFloatLE(float value, OutputStream os) throws IOException
    {
        os.write(toBytesLE(value));
    }
    /// 将值以低字节序写入流
    /// @since 8.0.0
    public static void writeDoubleLE(double value, OutputStream os) throws IOException
    {
        os.write(toBytesLE(value));
    }

    /// 将值以高字节序写入流
    /// @since 8.0.0
    public static void writeShortBE(short value, OutputStream os) throws IOException
    {
        os.write(toBytesBE(value));
    }
    /// 将值以高字节序写入流
    /// @since 8.0.0
    public static void writeIntE(int value, OutputStream os) throws IOException
    {
        os.write(toBytesBE(value));
    }
    /// 将值以高字节序写入流
    /// @since 8.0.0
    public static void writeLongBE(long value, OutputStream os) throws IOException
    {
        os.write(toBytesBE(value));
    }
    /// 将值以高字节序写入流
    /// @since 8.0.0
    public static void writeFloatBE(float value, OutputStream os) throws IOException
    {
        os.write(toBytesBE(value));
    }
    /// 将值以高字节序写入流
    /// @since 8.0.0
    public static void writeDoubleBE(double value, OutputStream os) throws IOException
    {
        os.write(toBytesBE(value));
    }

    /// 从流中以低字节序读取一个 short
    /// @since 8.0.0
    public static short readShortLE(InputStream is) throws IOException
    {
        var b = new byte[2];
        TopazExceptions.IOError.unless(is.read(b) == 2);
        return (short) (b[0] & 0xFF | (b[1] & 0xFF) << 8);
    }
    /// 从流中以低字节序读取一个 int
    /// @since 8.0.0
    public static int readIntLE(InputStream is) throws IOException
    {
        var b = new byte[4];
        TopazExceptions.IOError.unless(is.read(b) == 4);
        return b[0] & 0xFF | (b[1] & 0xFF) << 8 |
                (b[2] & 0xFF) << 16 | (b[3] & 0xFF) << 24;
    }
    /// 从流中以低字节序读取一个 long
    /// @since 8.0.0
    public static long readLongLE(InputStream is) throws IOException
    {
        var b = new byte[8];
        TopazExceptions.IOError.unless(is.read(b) == 8);
        return b[0] & 0xFF | (b[1] & 0xFF) << 8 |
                (b[2] & 0xFF) << 16 | (long) (b[3] & 0xFF) << 24 |
                (long) (b[4] & 0xFF) << 32 | (long) (b[5] & 0xFF) << 40 |
                (long) (b[6] & 0xFF) << 48 | (long) (b[7] & 0xFF) << 56;
    }
    /// 从流中以低字节序读取一个 float
    /// @since 8.0.0
    public static float readFloatLE(InputStream is) throws IOException
    {
        return Float.intBitsToFloat(readIntLE(is));
    }
    /// 从流中以低字节序读取一个 double
    /// @since 8.0.0
    public static double readDoubleLE(InputStream is) throws IOException
    {
        return Double.longBitsToDouble(readLongLE(is));
    }

    /// 从流中以高字节序读取一个 short
    /// @since 8.0.0
    public static short readShortBE(InputStream is) throws IOException
    {
        var b = new byte[2];
        TopazExceptions.IOError.unless(is.read(b) == 2);
        return (short) (b[1] & 0xFF | (b[0] & 0xFF) << 8);
    }
    /// 从流中以高字节序读取一个 int
    /// @since 8.0.0
    public static int readIntBE(InputStream is) throws IOException
    {
        var b = new byte[4];
        TopazExceptions.IOError.unless(is.read(b) == 4);
        return b[3] & 0xFF | (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
    }
    /// 从流中以高字节序读取一个 long
    /// @since 8.0.0
    public static long readLongBE(InputStream is) throws IOException
    {
        var b = new byte[8];
        TopazExceptions.IOError.unless(is.read(b) == 8);
        return b[7] & 0xFF | (b[6] & 0xFF) << 8 |
                (b[5] & 0xFF) << 16 | (long) (b[4] & 0xFF) << 24 |
                (long) (b[3] & 0xFF) << 32 | (long) (b[2] & 0xFF) << 40 |
                (long) (b[1] & 0xFF) << 48 | (long) (b[0] & 0xFF) << 56;
    }
    /// 从流中以高字节序读取一个 float
    /// @since 8.0.0
    public static float readFloatBE(InputStream is) throws IOException
    {
        return Float.intBitsToFloat(readIntBE(is));
    }
    /// 从流中以高字节序读取一个 double
    /// @since 8.0.0
    public static double readDoubleBE(InputStream is) throws IOException
    {
        return Double.longBitsToDouble(readLongBE(is));
    }

}
