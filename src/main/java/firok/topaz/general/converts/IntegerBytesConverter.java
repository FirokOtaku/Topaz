package firok.topaz.general.converts;

import org.jetbrains.annotations.NotNull;

public class IntegerBytesConverter implements TypeBytesConverter<Integer>
{
    @Override
    public int sizeOf()
    {
        return Integer.BYTES;
    }

    @Override
    public @NotNull Integer fromLE(byte[] buffer)
    {
        checkBuffer(buffer);
        return ( (int) ( buffer[0] & 0xFF) ) << 0  |
               ( (int) ( buffer[1] & 0xFF) ) << 8  |
               ( (int) ( buffer[2] & 0xFF) ) << 16 |
               ( (int) ( buffer[3] & 0xFF) ) << 24 ;
    }

    @Override
    public @NotNull Integer fromBE(byte[] buffer)
    {
        checkBuffer(buffer);
        return ( (int) ( buffer[3] & 0xFF) ) << 0  |
               ( (int) ( buffer[2] & 0xFF) ) << 8  |
               ( (int) ( buffer[1] & 0xFF) ) << 16 |
               ( (int) ( buffer[0] & 0xFF) ) << 24 ;
    }

    @Override
    public byte[] toBE(@NotNull Integer value)
    {
        return new byte[] {
                (byte) (value >> 24 & 0xFF),
                (byte) (value >> 16 & 0xFF),
                (byte) (value >> 8 & 0xFF),
                (byte) (value & 0xFF),
        };
    }

    @Override
    public byte[] toLE(@NotNull Integer value)
    {
        return new byte[] {
                (byte) (value & 0xFF),
                (byte) (value >> 8 & 0xFF),
                (byte) (value >> 16 & 0xFF),
                (byte) (value >> 24 & 0xFF),
        };
    }
}
