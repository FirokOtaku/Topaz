package firok.topaz.test;

import firok.topaz.general.Binaries;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BinariesTests
{

	void testHexOne(String hex, int[] ints)
	{
		var bytes = new byte[ints.length];
		for(int step = 0; step < ints.length; step++)
			bytes[step] = (byte) ints[step];

		var buffer = Binaries.toHexByte(hex);
		Assertions.assertArrayEquals(bytes, buffer);
		var hex2 = Binaries.toHexString(buffer);
		Assertions.assertEquals(hex, hex2);
	}
	@Test
	void testHexConvert()
	{
		testHexOne("01", new int[] { 0x01 });
		testHexOne("AA", new int[] { 0xAA });
		testHexOne("AF", new int[] { 0xAF });
		testHexOne("A452", new int[] { 0xA4, 0x52 });
		testHexOne("A45254", new int[] { 0xA4, 0x52, 0x54 });
	}
}
