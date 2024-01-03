package firok.topaz.general;

import java.math.BigInteger;

/**
 * @since 3.6.0
 * @author Firok
 * */
@SuppressWarnings("DanglingJavadoc")
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
		var len = str.length() / 2;
		var ret = new byte[len];
		for(int step = 0; step < len; step ++)
		{
			ret[step] = (byte) (Character.digit(str.charAt(step * 2), 16) * 10 + Character.digit(str.charAt(step * 2 + 1), 16));
		}
		return ret;
	}

	/**
	 * 低字节序, 数据转十进制
	 * @since 7.10.0
	 * */
	public static BigInteger toBinLSBValue(byte[] bytes)
	{
		var ret = BigInteger.ZERO;
		for(int step = bytes.length - 1; step >= 0; step--)
		{
			var b = bytes[step];
//			ret <<= 8;
//			ret |= Byte.toUnsignedInt(b);
			ret = ret.shiftLeft(8).add(BigInteger.valueOf(Byte.toUnsignedInt(b)));
		}
		return ret;
	}
	/**
	 * 低字节序, 十进制转数据
	 * @since 7.10.0
	 * */
	public static byte[] toBinLSBByte(BigInteger num, int length)
	{
		var ret = new byte[length];
		for(int step = 0; step < length; step++)
		{
//			ret[step] = (byte) (num & 0xFF);
//			num >>>= 8;
			ret[step] = num.and(BigInteger.valueOf(0xFF)).byteValue();
			num = num.shiftRight(8);
		}
		return ret;
	}

	/**
	 * user-friendly-chars-both
	 * no '0', '1', 'l', 'L', 'i', 'I', 's', 'S', 'u', 'U', 'v', 'V', 'w', 'W', 'x', 'X', 'y', 'Y', 'z', 'Z'
	 * */
//	private static final char[] UfcCharBoth = new char[] {
//			'2', '3', '4', '5', '6', '7', '8', '9',
//			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 't',
//			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'T',
//	};

	/**
	 * user-friendly-chars-single
	 * */
//	private static final char[] UfcCharSingle = new char[] {
//			'2', '3', '4', '5', '6', '7', '8', '9',
//			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'O', 'P', 'Q', 'R', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
//	};
}
