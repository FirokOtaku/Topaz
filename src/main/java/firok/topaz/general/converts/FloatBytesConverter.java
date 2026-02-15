package firok.topaz.general.converts;

import firok.topaz.general.Binaries;
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
    public Float fromLE(byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        int value = Binaries.Int.fromLE(buffer);
        return Float.intBitsToFloat(value);
    }

    @Override
    @NotNull
    public Float fromBE(byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        int value = Binaries.Int.fromBE(buffer);
        return Float.intBitsToFloat(value);
    }

    @Override
    public byte[] toBE(@NotNull Float value, byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        var intValue = Float.floatToIntBits(value);
        return Binaries.Int.toBE(intValue, buffer, offset);
    }

    @Override
    public byte[] toLE(@NotNull Float value, byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        var intValue = Float.floatToIntBits(value);
        return Binaries.Int.toLE(intValue, buffer, offset);
    }
}
