package firok.topaz.general.converts;

import firok.topaz.annotation.Indev;
import firok.topaz.general.BinariesRenew;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Indev(experimental = true)
public class UUIDBytesConverter implements TypeBytesConverter<UUID>
{
    @Override
    public int sizeOf()
    {
        return Long.BYTES * 2;
    }

    @Override
    public @NotNull UUID fromLE(byte[] buffer)
    {
        checkBuffer(buffer);

        var bufferMost = new byte[8];
        var bufferLeast = new byte[8];

        System.arraycopy(buffer, 0, bufferMost, 0, 8);
        System.arraycopy(buffer, 8, bufferLeast, 0, 8);

        return new UUID(
                BinariesRenew.Long.fromBE(bufferMost),
                BinariesRenew.Long.fromBE(bufferLeast)
        );
    }

    @Override
    public @NotNull UUID fromBE(byte[] buffer)
    {
        checkBuffer(buffer);

        var bufferMost = new byte[8];
        var bufferLeast = new byte[8];

        System.arraycopy(buffer, 0, bufferMost, 0, 8);
        System.arraycopy(buffer, 8, bufferLeast, 0, 8);

        return new UUID(
                BinariesRenew.Long.fromBE(bufferLeast),
                BinariesRenew.Long.fromBE(bufferMost)
        );
    }

    @Override
    public byte[] toBE(@NotNull UUID value)
    {
        var bitMost = value.getMostSignificantBits();
        var bitLeast = value.getLeastSignificantBits();

        var bufferMost = BinariesRenew.Long.toBE(bitMost);
        var bufferLeast = BinariesRenew.Long.toBE(bitLeast);

        return new byte[] {
                bufferMost[0], bufferMost[1], bufferMost[2], bufferMost[3],
                bufferMost[4], bufferMost[5], bufferMost[6], bufferMost[7],
                bufferLeast[0], bufferLeast[1], bufferLeast[2], bufferLeast[3],
                bufferLeast[4], bufferLeast[5], bufferLeast[6], bufferLeast[7],
        };
    }

    @Override
    public byte[] toLE(@NotNull UUID value)
    {
        var bitMost = value.getMostSignificantBits();
        var bitLeast = value.getLeastSignificantBits();

        var bufferMost = BinariesRenew.Long.toBE(bitMost);
        var bufferLeast = BinariesRenew.Long.toBE(bitLeast);

        return new byte[] {
                bufferLeast[0], bufferLeast[1], bufferLeast[2], bufferLeast[3],
                bufferLeast[4], bufferLeast[5], bufferLeast[6], bufferLeast[7],

                bufferMost[0], bufferMost[1], bufferMost[2], bufferMost[3],
                bufferMost[4], bufferMost[5], bufferMost[6], bufferMost[7],
        };
    }
}
