package firok.topaz.general.converts;

import org.jetbrains.annotations.NotNull;

public class ShortBytesConverter implements TypeBytesConverter<Short>
{
    @Override
    public int sizeOf()
    {
        return Short.BYTES;
    }

    @Override
    public @NotNull Short fromLE(byte[] buffer)
    {
        checkBuffer(buffer);
        return (short) (buffer[0] | buffer[1] << 8);
    }

    @Override
    public @NotNull Short fromBE(byte[] buffer)
    {
        checkBuffer(buffer);
        return (short) (buffer[1] | buffer[0] << 8);
    }

    @Override
    public byte[] toBE(@NotNull Short value)
    {
        return new byte[] {
                (byte) (value >> 8 & 0xFF),
                (byte) (value & 0xFF)
        };
    }

    @Override
    public byte[] toLE(@NotNull Short value)
    {
        return new byte[] {
                (byte) (value & 0xFF),
                (byte) (value >> 8 & 0xFF)
        };
    }
}
