package firok.topaz.general.converts;

import firok.topaz.general.BinariesRenew;
import org.jetbrains.annotations.NotNull;

public class DoubleBytesConverter implements TypeBytesConverter<Double>
{
    @Override
    public int sizeOf()
    {
        return Double.BYTES;
    }

    @Override
    @NotNull
    public Double fromLE(byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        long value = BinariesRenew.Long.fromLE(buffer, offset);
        return Double.longBitsToDouble(value);
    }

    @Override
    @NotNull
    public Double fromBE(byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        long value = BinariesRenew.Long.fromBE(buffer, offset);
        return Double.longBitsToDouble(value);
    }

    @Override
    public byte[] toBE(@NotNull Double value, byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        var longValue = Double.doubleToLongBits(value);
        return BinariesRenew.Long.toBE(longValue, buffer, offset);
    }

    @Override
    public byte[] toLE(@NotNull Double value, byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        var longValue = Double.doubleToLongBits(value);
        return BinariesRenew.Long.toLE(longValue, buffer, offset);
    }
}
