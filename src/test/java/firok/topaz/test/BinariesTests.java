package firok.topaz.test;

import firok.topaz.general.Binaries;
import firok.topaz.general.BinariesRenew;
import firok.topaz.general.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.Function;

public class BinariesTests
{

	void testHexOne(String hex, int[] ints)
	{
		var bytes = new byte[ints.length];
		for(int step = 0; step < ints.length; step++)
			bytes[step] = (byte) ints[step];

		var buffer = BinariesRenew.toHexByte(hex);
		Assertions.assertArrayEquals(bytes, buffer);
		var hex2 = BinariesRenew.toHexString(buffer);
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

	@Test
	void testDirectConvert()
	{
		var buffer = BinariesRenew.toDirectByte("20201223");
		Assertions.assertArrayEquals(new byte[] { 0x20, 0x20, 0x12, 0x23 }, buffer);
		Assertions.assertEquals("2008", BinariesRenew.toDirectString(new byte[] { 0x20, 0x08}));
	}

	<TypeNumber extends Number> void testBinOne(
            TypeNumber valueRaw,
            byte[] bufferBERaw,
            Function<TypeNumber, byte[]> funValueToBytesLE,
            Function<byte[], TypeNumber> funBytesToValueLE,
            Function<TypeNumber, byte[]> funValueToBytesBE,
            Function<byte[], TypeNumber> funBytesToValueBE
    )
	{
        var bufferBE = funValueToBytesBE.apply(valueRaw);
        Assertions.assertArrayEquals(bufferBERaw, bufferBE);
        var valueBE = funBytesToValueBE.apply(bufferBE);
        Assertions.assertEquals(valueRaw, valueBE);

        var bufferLE = funValueToBytesLE.apply(valueRaw);
        Collections.reverseOf(bufferLE);

	}
//	@Test
	void testBinConvert()
	{
	}
}
