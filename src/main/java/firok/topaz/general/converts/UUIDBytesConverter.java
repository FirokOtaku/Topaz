package firok.topaz.general.converts;

import firok.topaz.annotation.Indev;
import firok.topaz.general.Binaries;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("PointlessArithmeticExpression")
@Indev(experimental = true)
public class UUIDBytesConverter implements TypeBytesConverter<UUID>
{
    @Override
    public int sizeOf()
    {
        return Long.BYTES * 2;
    }

    @Override
    public @NotNull UUID fromLE(byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);

        var bufferMost = new byte[Long.BYTES];
        var bufferLeast = new byte[Long.BYTES];

        System.arraycopy(buffer, offset + 0, bufferMost, 0, Long.BYTES);
        System.arraycopy(buffer, offset + Long.BYTES, bufferLeast, 0, Long.BYTES);

        return new UUID(
                Binaries.Long.fromBE(bufferMost),
                Binaries.Long.fromBE(bufferLeast)
        );
    }

    @Override
    public @NotNull UUID fromBE(byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);

        var bufferMost = new byte[Long.BYTES];
        var bufferLeast = new byte[Long.BYTES];

        System.arraycopy(buffer, offset + 0, bufferMost, 0, Long.BYTES);
        System.arraycopy(buffer, offset + Long.BYTES, bufferLeast, 0, Long.BYTES);

        return new UUID(
                Binaries.Long.fromBE(bufferLeast),
                Binaries.Long.fromBE(bufferMost)
        );
    }

    @Override
    public byte[] toBE(@NotNull UUID value, byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);

        var bitMost = value.getMostSignificantBits();
        var bitLeast = value.getLeastSignificantBits();

        var bufferMost = Binaries.Long.toBE(bitMost);
        var bufferLeast = Binaries.Long.toBE(bitLeast);

        buffer[offset + 0] = bufferMost[0];
        buffer[offset + 1] = bufferMost[1];
        buffer[offset + 2] = bufferMost[2];
        buffer[offset + 3] = bufferMost[3];
        buffer[offset + 4] = bufferMost[4];
        buffer[offset + 5] = bufferMost[5];
        buffer[offset + 6] = bufferMost[6];
        buffer[offset + 7] = bufferMost[7];
        buffer[offset + 8] = bufferLeast[0];
        buffer[offset + 9] = bufferLeast[1];
        buffer[offset + 10] = bufferLeast[2];
        buffer[offset + 11] = bufferLeast[3];
        buffer[offset + 12] = bufferLeast[4];
        buffer[offset + 13] = bufferLeast[5];
        buffer[offset + 14] = bufferLeast[6];
        buffer[offset + 15] = bufferLeast[7];
        return buffer;
    }

    @Override
    public byte[] toLE(@NotNull UUID value, byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);

        var bitMost = value.getMostSignificantBits();
        var bitLeast = value.getLeastSignificantBits();

        var bufferMost = Binaries.Long.toBE(bitMost);
        var bufferLeast = Binaries.Long.toBE(bitLeast);

        buffer[offset + 0] = bufferLeast[0];
        buffer[offset + 1] = bufferLeast[1];
        buffer[offset + 2] = bufferLeast[2];
        buffer[offset + 3] = bufferLeast[3];
        buffer[offset + 4] = bufferLeast[4];
        buffer[offset + 5] = bufferLeast[5];
        buffer[offset + 6] = bufferLeast[6];
        buffer[offset + 7] = bufferLeast[7];
        buffer[offset + 8] = bufferMost[0];
        buffer[offset + 9] = bufferMost[1];
        buffer[offset + 10] = bufferMost[2];
        buffer[offset + 11] = bufferMost[3];
        buffer[offset + 12] = bufferMost[4];
        buffer[offset + 13] = bufferMost[5];
        buffer[offset + 14] = bufferMost[6];
        buffer[offset + 15] = bufferMost[7];
        return buffer;
    }
}
