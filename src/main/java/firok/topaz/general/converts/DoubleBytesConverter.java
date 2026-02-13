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
    public Double fromLE(byte[] buffer)
    {
        checkBuffer(buffer);
        long value = BinariesRenew.Long.fromLE(buffer);
        return Double.longBitsToDouble(value);
    }

    @Override
    @NotNull
    public Double fromBE(byte[] buffer)
    {
        checkBuffer(buffer);
        long value = BinariesRenew.Long.fromBE(buffer);
        return Double.longBitsToDouble(value);
    }

    @Override
    public byte[] toBE(@NotNull Double value)
    {
        var intValue = Double.doubleToLongBits(value);
        return BinariesRenew.Long.toBE(intValue);
    }

    @Override
    public byte[] toLE(@NotNull Double value)
    {
        var intValue = Double.doubleToLongBits(value);
        return BinariesRenew.Long.toLE(intValue);
    }
}
