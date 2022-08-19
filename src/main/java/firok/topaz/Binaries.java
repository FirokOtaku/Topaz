package firok.topaz;

public final class Binaries
{
	private Binaries() { }

	private static final char[] chars = new char[] {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F'
	};
	private static char toHexChar(int b)
	{
		return chars[b & 0xF];
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
}
