package firok.topaz;

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