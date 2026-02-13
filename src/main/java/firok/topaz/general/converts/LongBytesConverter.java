package firok.topaz.general.converts;

import org.jetbrains.annotations.NotNull;

public class LongBytesConverter implements TypeBytesConverter<Long>
{
    @Override
    public int sizeOf()
    {
        return Long.BYTES;
    }

    @Override
    public @NotNull Long fromLE(byte[] buffer)
    {
        checkBuffer(buffer);
        return ( (long) ( buffer[0] & 0xFF) ) << 0  |
               ( (long) ( buffer[1] & 0xFF) ) << 8  |
               ( (long) ( buffer[2] & 0xFF) ) << 16 |
               ( (long) ( buffer[3] & 0xFF) ) << 24 |
               ( (long) ( buffer[4] & 0xFF) ) << 32 |
               ( (long) ( buffer[5] & 0xFF) ) << 40 |
               ( (long) ( buffer[6] & 0xFF) ) << 48 |
               ( (long) ( buffer[7] & 0xFF) ) << 56 ;
    }

    @Override
    public @NotNull Long fromBE(byte[] buffer)
    {
        checkBuffer(buffer);
        return ( (long) ( buffer[7] & 0xFF) ) << 0  |
               ( (long) ( buffer[6] & 0xFF) ) << 8  |
               ( (long) ( buffer[5] & 0xFF) ) << 16 |
               ( (long) ( buffer[4] & 0xFF) ) << 24 |
               ( (long) ( buffer[3] & 0xFF) ) << 32 |
               ( (long) ( buffer[2] & 0xFF) ) << 40 |
               ( (long) ( buffer[1] & 0xFF) ) << 48 |
               ( (long) ( buffer[0] & 0xFF) ) << 56 ;
    }

    @Override
    public byte[] toBE(@NotNull Long value)
    {
        return new byte[] {
                (byte) (value >> 56 & 0xFF),
                (byte) (value >> 48 & 0xFF),
                (byte) (value >> 40 & 0xFF),
                (byte) (value >> 32 & 0xFF),
                (byte) (value >> 24 & 0xFF),
                (byte) (value >> 16 & 0xFF),
                (byte) (value >> 8 & 0xFF),
                (byte) (value & 0xFF)
        };
    }

    @Override
    public byte[] toLE(@NotNull Long value)
    {
        return new byte[] {
                (byte) (value & 0xFF),
                (byte) (value >> 8 & 0xFF),
                (byte) (value >> 16 & 0xFF),
                (byte) (value >> 24 & 0xFF),
                (byte) (value >> 32 & 0xFF),
                (byte) (value >> 40 & 0xFF),
                (byte) (value >> 48 & 0xFF),
                (byte) (value >> 56 & 0xFF)
        };
    }
}
