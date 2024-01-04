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

		var bufferLSB = Binaries.toBinLSBByte(value, 4);
		var valueLSB = Binaries.toBinLSBValue(bufferLSB);
		Assertions.assertEquals(value, valueLSB);

		var bufferMSB = Binaries.toBinMSBByte(value, 4);
		var valueMSB = Binaries.toBinMSBValue(bufferMSB);
		Assertions.assertEquals(value, valueMSB);
	}
	@Test
	void testBinConvert()
	{
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
		Assertions.assertArrayEquals(
				new byte[] { 0x11, 0x11, 0x11, 0x11 },
				Binaries.toBinLSBByte(BigInteger.valueOf(286331153), 4)
		);
		Assertions.assertArrayEquals(
				new byte[] { 0x11, 0x11 },
				Binaries.toBinLSBByte(BigInteger.valueOf(4369), 2)
		);
		Assertions.assertArrayEquals(
				new byte[] { 0x11, 0x00, 0x00 },
				Binaries.toBinLSBByte(BigInteger.valueOf(17), 3)
		);
		Assertions.assertArrayEquals(
				new byte[] { 0x67, 0x45, 0x23, 0x01 },
				Binaries.toBinLSBByte(BigInteger.valueOf(0x01234567), 4)
		);

		Assertions.assertArrayEquals(
				new byte[] {},
				Binaries.toBinMSBByte(BigInteger.ZERO, 0)
		);
		Assertions.assertArrayEquals(
				new byte[] { 0x00, 0x00, 0x04, 0x00 },
				Binaries.toBinMSBByte(BigInteger.valueOf(1024), 4)
		);
		Assertions.assertArrayEquals(
				new byte[] { 0x00, 0x01, (byte) 0x90, 0x18 },
				Binaries.toBinMSBByte(BigInteger.valueOf(102424), 4)
		);
		Assertions.assertArrayEquals(
				new byte[] { 0x00, 0x00, 0x44, (byte) 0xBC },
				Binaries.toBinMSBByte(BigInteger.valueOf(17596), 4)
		);
		Assertions.assertArrayEquals(
				new byte[] { 0x11, 0x11, 0x11, 0x11 },
				Binaries.toBinMSBByte(BigInteger.valueOf(286331153), 4)
		);
		Assertions.assertArrayEquals(
				new byte[] { 0x11, 0x11 },
				Binaries.toBinMSBByte(BigInteger.valueOf(4369), 2)
		);
		Assertions.assertArrayEquals(
				new byte[] { 0x00, 0x00, 0x11 },
				Binaries.toBinMSBByte(BigInteger.valueOf(17), 3)
		);
		Assertions.assertArrayEquals(
				new byte[] { 0x01, 0x23, 0x45, 0x67 },
				Binaries.toBinMSBByte(BigInteger.valueOf(0x01234567), 4)
		);

		for(var step = 0; step < 100; step++)
		{
			testBinOne(step);
		}
	}
}
