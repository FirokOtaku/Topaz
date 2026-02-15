package firok.topaz.general.converts;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("PointlessArithmeticExpression")
public class ShortBytesConverter implements TypeBytesConverter<Short>
{
    @Override
    public int sizeOf()
    {
        return Short.BYTES;
    }

    @Override
    public @NotNull Short fromLE(byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        return (short) (buffer[0 + offset] | buffer[1 + offset] << 8);
    }

    @Override
    public @NotNull Short fromBE(byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        return (short) (buffer[1] | buffer[0] << 8);
    }

    @Override
    public byte[] toBE(@NotNull Short value, byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        buffer[offset + 0] = (byte) (value >> 8 & 0xFF);
        buffer[offset + 1] = (byte) (value & 0xFF);
        return buffer;
    }

    @Override
    public byte[] toLE(@NotNull Short value, byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        buffer[offset + 0] = (byte) (value & 0xFF);
        buffer[offset + 1] = (byte) (value >> 8 & 0xFF);
        return buffer;
    }
}
