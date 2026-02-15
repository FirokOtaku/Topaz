package firok.topaz.general.converts;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"PointlessArithmeticExpression", "PointlessBitwiseExpression"})
public class IntegerBytesConverter implements TypeBytesConverter<Integer>
{
    @Override
    public int sizeOf()
    {
        return Integer.BYTES;
    }

    @Override
    public @NotNull Integer fromLE(byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        return ( (int) ( buffer[0] & 0xFF) ) << 0  |
               ( (int) ( buffer[1] & 0xFF) ) << 8  |
               ( (int) ( buffer[2] & 0xFF) ) << 16 |
               ( (int) ( buffer[3] & 0xFF) ) << 24 ;
    }

    @Override
    public @NotNull Integer fromBE(byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        return ( (int) ( buffer[3] & 0xFF) ) << 0  |
               ( (int) ( buffer[2] & 0xFF) ) << 8  |
               ( (int) ( buffer[1] & 0xFF) ) << 16 |
               ( (int) ( buffer[0] & 0xFF) ) << 24 ;
    }

    @Override
    public byte[] toBE(@NotNull Integer value, byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        buffer[offset + 0] = (byte) (value >> 24 & 0xFF);
        buffer[offset + 1] = (byte) (value >> 16 & 0xFF);
        buffer[offset + 2] = (byte) (value >> 8 & 0xFF);
        buffer[offset + 3] = (byte) (value >> 0 & 0xFF);
        return buffer;
    }

    @Override
    public byte[] toLE(@NotNull Integer value, byte[] buffer, int offset)
    {
        checkBuffer(buffer, offset);
        buffer[offset + 0] = (byte) (value >> 0 & 0xFF);
        buffer[offset + 1] = (byte) (value >> 8 & 0xFF);
        buffer[offset + 2] = (byte) (value >> 16 & 0xFF);
        buffer[offset + 3] = (byte) (value >> 24 & 0xFF);
        return buffer;
    }
}
