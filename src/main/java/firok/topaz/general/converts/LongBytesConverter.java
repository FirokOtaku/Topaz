package firok.topaz.general.converts;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"PointlessBitwiseExpression", "PointlessArithmeticExpression"})
public class LongBytesConverter implements TypeBytesConverter<Long>
{
    @Override
    public int sizeOf()
    {
        return Long.BYTES;
    }

    @Override
    public @NotNull Long fromLE(byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        return ( (long) ( buffer[offset + 0] & 0xFF) ) << 0  |
               ( (long) ( buffer[offset + 1] & 0xFF) ) << 8  |
               ( (long) ( buffer[offset + 2] & 0xFF) ) << 16 |
               ( (long) ( buffer[offset + 3] & 0xFF) ) << 24 |
               ( (long) ( buffer[offset + 4] & 0xFF) ) << 32 |
               ( (long) ( buffer[offset + 5] & 0xFF) ) << 40 |
               ( (long) ( buffer[offset + 6] & 0xFF) ) << 48 |
               ( (long) ( buffer[offset + 7] & 0xFF) ) << 56 ;
    }

    @Override
    public @NotNull Long fromBE(byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        return ( (long) ( buffer[offset + 7] & 0xFF) ) << 0  |
               ( (long) ( buffer[offset + 6] & 0xFF) ) << 8  |
               ( (long) ( buffer[offset + 5] & 0xFF) ) << 16 |
               ( (long) ( buffer[offset + 4] & 0xFF) ) << 24 |
               ( (long) ( buffer[offset + 3] & 0xFF) ) << 32 |
               ( (long) ( buffer[offset + 2] & 0xFF) ) << 40 |
               ( (long) ( buffer[offset + 1] & 0xFF) ) << 48 |
               ( (long) ( buffer[offset + 0] & 0xFF) ) << 56 ;
    }

    @Override
    public byte[] toBE(@NotNull Long value, byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        buffer[offset + 0] = (byte) (value >> 56 & 0xFF);
        buffer[offset + 1] = (byte) (value >> 48 & 0xFF);
        buffer[offset + 2] = (byte) (value >> 40 & 0xFF);
        buffer[offset + 3] = (byte) (value >> 32 & 0xFF);
        buffer[offset + 4] = (byte) (value >> 24 & 0xFF);
        buffer[offset + 5] = (byte) (value >> 16 & 0xFF);
        buffer[offset + 6] = (byte) (value >> 8 & 0xFF);
        buffer[offset + 7] = (byte) (value >> 0 & 0xFF);
        return buffer;
    }

    @Override
    public byte[] toLE(@NotNull Long value, byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        buffer[offset + 0] = (byte) (value >> 0 & 0xFF);
        buffer[offset + 1] = (byte) (value >> 8 & 0xFF);
        buffer[offset + 2] = (byte) (value >> 16 & 0xFF);
        buffer[offset + 3] = (byte) (value >> 24 & 0xFF);
        buffer[offset + 4] = (byte) (value >> 32 & 0xFF);
        buffer[offset + 5] = (byte) (value >> 40 & 0xFF);
        buffer[offset + 6] = (byte) (value >> 48 & 0xFF);
        buffer[offset + 7] = (byte) (value >> 56 & 0xFF);
        return buffer;
    }
}
