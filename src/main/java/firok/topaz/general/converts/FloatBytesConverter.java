package firok.topaz.general.converts;

import firok.topaz.general.BinariesRenew;
import org.jetbrains.annotations.NotNull;

public class FloatBytesConverter implements TypeBytesConverter<Float>
{
    @Override
    public int sizeOf()
    {
        return Float.BYTES;
    }

    @Override
    @NotNull
    public Float fromLE(byte[] buffer)
    {
        checkBuffer(buffer);
        int value = BinariesRenew.Int.fromLE(buffer);
        return Float.intBitsToFloat(value);
    }

    @Override
    @NotNull
    public Float fromBE(byte[] buffer)
    {
        checkBuffer(buffer);
        int value = BinariesRenew.Int.fromBE(buffer);
        return Float.intBitsToFloat(value);
    }

    @Override
    public byte[] toBE(@NotNull Float value)
    {
        var intValue = Float.floatToIntBits(value);
        return BinariesRenew.Int.toBE(intValue);
    }

    @Override
    public byte[] toLE(@NotNull Float value)
    {
        var intValue = Float.floatToIntBits(value);
        return BinariesRenew.Int.toLE(intValue);
    }
}
