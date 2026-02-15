package firok.topaz.test;

import firok.topaz.general.BinariesRenew;
import firok.topaz.general.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

public class BinariesTests
{

	void testHexStringConvertOne(String hex, int[] ints)
	{
		var bytes = new byte[ints.length];
		for(int step = 0; step < ints.length; step++)
			bytes[step] = (byte) ints[step];

		var buffer = BinariesRenew.toHexByte(hex);
		Assertions.assertArrayEquals(bytes, buffer);
		var hex2 = BinariesRenew.toHexString(bytes);
		Assertions.assertEquals(hex, hex2);
	}
	@Test
	void testHexStringConvert()
	{
		testHexStringConvertOne("01", new int[] { 0x01 });
		testHexStringConvertOne("AA", new int[] { 0xAA });
		testHexStringConvertOne("AF", new int[] { 0xAF });
		testHexStringConvertOne("A452", new int[] { 0xA4, 0x52 });
		testHexStringConvertOne("A45254", new int[] { 0xA4, 0x52, 0x54 });
        testHexStringConvertOne("0102AB", new int[] { 0x01, 0x02, 0xAB });
        testHexStringConvertOne("002233", new int[] { 0x00, 0x22, 0x33 });
        testHexStringConvertOne("00000000", new int[] { 0x00, 0x00, 0x00, 0x00 });
        testHexStringConvertOne("01020304", new int[] { 0x01, 0x02, 0x03, 0x04 });
        testHexStringConvertOne("00", new int[] { 0x00 });
	}

	private <TypeNumber extends Number> void testBytesConvert(
            TypeNumber valueBERaw,
            byte[] bufferBERaw,
            Function<TypeNumber, byte[]> funValueToBytesLE,
            Function<byte[], TypeNumber> funBytesToValueLE,
            Function<TypeNumber, byte[]> funValueToBytesBE,
            Function<byte[], TypeNumber> funBytesToValueBE
    )
	{
        var bufferBE = funValueToBytesBE.apply(valueBERaw);
        Assertions.assertArrayEquals(bufferBERaw, bufferBE);
        var valueBE = funBytesToValueBE.apply(bufferBE);
        Assertions.assertEquals(valueBERaw, valueBE);

        var bufferLE = funValueToBytesLE.apply(valueBERaw);

        var bufferLERaw = Collections.reverseOf(bufferBERaw);
        Assertions.assertArrayEquals(bufferLERaw, bufferLE);
        var valueLE = funBytesToValueLE.apply(bufferLE);
        var bufferLE2 = funValueToBytesLE.apply(valueLE);
        var bufferBE2 = Collections.reverseOf(bufferLE2);
        Assertions.assertArrayEquals(bufferBERaw, bufferBE2);
	}
    private void testLongBytesConvert(long value, byte[] buffer)
    {
        testBytesConvert(
                value,
                buffer,
                BinariesRenew.Long::toLE,
                BinariesRenew.Long::fromLE,
                BinariesRenew.Long::toBE,
                BinariesRenew.Long::fromBE
        );
    }
    private void testIntBytesConvert(int value, byte[] buffer)
    {
        testBytesConvert(
                value,
                buffer,
                BinariesRenew.Int::toLE,
                BinariesRenew.Int::fromLE,
                BinariesRenew.Int::toBE,
                BinariesRenew.Int::fromBE
        );
    }
    private void testShortBytesConvert(short value, byte[] buffer)
    {
        testBytesConvert(
                value,
                buffer,
                BinariesRenew.Short::toLE,
                BinariesRenew.Short::fromLE,
                BinariesRenew.Short::toBE,
                BinariesRenew.Short::fromBE
        );
    }
    private void testFloatBytesConvert(float value, int intBits)
    {
        testBytesConvert(
                value,
                BinariesRenew.Int.toBE(intBits),
                BinariesRenew.Float::toLE,
                BinariesRenew.Float::fromLE,
                BinariesRenew.Float::toBE,
                BinariesRenew.Float::fromBE
        );
    }
    private void testDoubleBytesConvert(double value, long longBits)
    {
        testBytesConvert(
                value,
                BinariesRenew.Long.toBE(longBits),
                BinariesRenew.Double::toLE,
                BinariesRenew.Double::fromLE,
                BinariesRenew.Double::toBE,
                BinariesRenew.Double::fromBE
        );
    }
//    private void testUUIDBytesConvert(UUID uuid)
//    {
//        testBytesConvert();
//    }


	@Test
	void testBinConvert()
	{
        // long

        testLongBytesConvert(
                0x0A0B0C0D1A2B3C4DL,
                new byte[] { 0x0A, 0x0B, 0x0C, 0x0D, 0x1A, 0x2B, 0x3C, 0x4D }
        );
        testLongBytesConvert(
                0x0D0C0B0A4D3C2B1AL,
                new byte[] { 0x0D, 0x0C, 0x0B, 0x0A, 0x4D, 0x3C, 0x2B, 0x1A }
        );
        testLongBytesConvert(
                0x1122334455667788L,
                new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, (byte) 0x88 }
        );
        testLongBytesConvert(
                0xAAAA0000BBBB0000L,
                new byte[] { (byte) 0xAA, (byte) 0xAA, 0x00, 0x00, (byte) 0xBB, (byte) 0xBB, 0x00, 0x00 }
        );
        testLongBytesConvert(
                0x0000000000000000L,
                new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }
        );

        // int

        testIntBytesConvert(
                0x0A0B0C0D,
                new byte[] { 0x0A, 0x0B, 0x0C, 0x0D }
        );

        testIntBytesConvert(
                0x11223344,
                new byte[] { 0x11, 0x22, 0x33, 0x44 }
        );

        testIntBytesConvert(
                0x00112233,
                new byte[] { 0x00, 0x11, 0x22, 0x33 }
        );

        testIntBytesConvert(
                0x01020304,
                new byte[] { 0x01, 0x02, 0x03, 0x04 }
        );

        // short

        testShortBytesConvert(
                (short) 0x0A0B,
                new byte[] { 0x0A, 0x0B }
        );

        testShortBytesConvert(
                (short) 0x1122,
                new byte[] { 0x11, 0x22 }
        );

        testShortBytesConvert(
                (short) 0x1234,
                new byte[] { 0x12, 0x34 }
        );

        // float
        // 例子来源 https://zh.wikipedia.org/zh-cn/%E5%8D%95%E7%B2%BE%E5%BA%A6%E6%B5%AE%E7%82%B9%E6%95%B0

        testFloatBytesConvert(
                0.15625F,
                0b001111100_01000000000000000000000
        );

        // double
        // 例子来源 https://zh.wikipedia.org/zh-cn/%E5%8F%8C%E7%B2%BE%E5%BA%A6%E6%B5%AE%E7%82%B9%E6%95%B0

        testDoubleBytesConvert(
                1D,
                0b0_01111111111_0000000000000000000000000000000000000000000000000000L
        );

        testDoubleBytesConvert(
                1.0000000000000002D,
                0b0_01111111111_0000000000000000000000000000000000000000000000000001L
        );

        testDoubleBytesConvert(
                1.0000000000000004D,
                0b0_01111111111_0000000000000000000000000000000000000000000000000010L
        );

        testDoubleBytesConvert(
                2D,
                0b0_10000000000_0000000000000000000000000000000000000000000000000000L
        );

        testDoubleBytesConvert(
                -2D,
                0b1_10000000000_0000000000000000000000000000000000000000000000000000L
        );

        testDoubleBytesConvert(
                3D,
                0b0_10000000000_1000000000000000000000000000000000000000000000000000L
        );

        testDoubleBytesConvert(
                4D,
                0b0_10000000001_0000000000000000000000000000000000000000000000000000L
        );

        testDoubleBytesConvert(
                5D,
                0b0_10000000001_0100000000000000000000000000000000000000000000000000L
        );

        testDoubleBytesConvert(
                6D,
                0b0_10000000001_1000000000000000000000000000000000000000000000000000L
        );

        testDoubleBytesConvert(
                23D,
                0b0_10000000011_0111000000000000000000000000000000000000000000000000L
        );

        testDoubleBytesConvert(
                0.01171875D,
                0b0_01111111000_1000000000000000000000000000000000000000000000000000L
        );

        testDoubleBytesConvert(
                Double.MAX_VALUE,
                0b0_11111111110_1111111111111111111111111111111111111111111111111111L
        );

        testDoubleBytesConvert(
                +0D,
                0b0_00000000000_0000000000000000000000000000000000000000000000000000L
        );

        testDoubleBytesConvert(
                -0D,
                0b1_00000000000_0000000000000000000000000000000000000000000000000000L
        );

        testDoubleBytesConvert(
                Double.POSITIVE_INFINITY,
                0b0_11111111111_0000000000000000000000000000000000000000000000000000L
        );

        testDoubleBytesConvert(
                Double.NEGATIVE_INFINITY,
                0b1_11111111111_0000000000000000000000000000000000000000000000000000L
        );

//        testDoubleBytesConvert(
//                Double.NaN,
//                0b0_11111111111_0000000000000000000000000000000000000000000000000001L
//        );
//
//        testDoubleBytesConvert(
//                Double.NaN,
//                0b0_11111111111_1000000000000000000000000000000000000000000000000001L
//        );
//
//        testDoubleBytesConvert(
//                Double.NaN,
//                0b0_11111111111_1111111111111111111111111111111111111111111111111111L
//        );

        testDoubleBytesConvert(
                1 / 3D,
                0b0_01111111101_0101010101010101010101010101010101010101010101010101L
        );

        testDoubleBytesConvert(
                Math.PI,
                0b0_10000000000_1001001000011111101101010100010001000010110100011000L
        );

        // uuid

//        testUUIDBytesConvert(UUID.randomUUID());
//        testUUIDBytesConvert(UUID.randomUUID());
//        testUUIDBytesConvert(UUID.randomUUID());
//        testUUIDBytesConvert(UUID.randomUUID());
	}
}
