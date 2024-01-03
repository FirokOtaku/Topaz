package firok.topaz.test;

import firok.topaz.general.Binaries;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

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

	void testBinOne(int raw)
	{
		var value = BigInteger.valueOf(raw);

		var buffer = Binaries.toBinLSBByte(value, 4);
		var value2 = Binaries.toBinLSBValue(buffer);
		Assertions.assertEquals(value, value2);
	}
	@Test
	void testBinConvert()
	{
		for(var step = 0; step < 100; step++)
		{
			testBinOne(step);
		}

		Assertions.assertArrayEquals(
				new byte[] {},
				Binaries.toBinLSBByte(BigInteger.ZERO, 0)
		);
		Assertions.assertArrayEquals(
				new byte[] { 0x00, 0x04, 0x00, 0x00 },
				Binaries.toBinLSBByte(BigInteger.valueOf(1024), 4)
		);
		Assertions.assertArrayEquals(
				new byte[] { 0x18, (byte) 0x90, 0x01, 0x00 },
				Binaries.toBinLSBByte(BigInteger.valueOf(102424), 4)
		);
		Assertions.assertArrayEquals(
				new byte[] { (byte) 0xBC, 0x44, 0x00, 0x00 },
				Binaries.toBinLSBByte(BigInteger.valueOf(17596), 4)
		);
	}
}
