package firok.topaz.general;

import firok.topaz.annotation.Overload;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * 对 {@link StringBuffer} 和 {@link StringBuilder} 的一点扩展
 * <p>gossip 上面这俩玩意竟然是 {@code sealed} 的</p>
 * @author Firok
 * @since 7.43.0
 * */
public class StringFactory implements Appendable, CharSequence
{
    private final Appendable app;
    private final CharSequence cs;
    private String lineSeparator;
    public StringFactory setLineSeparator(String lineSeparator)
    {
        this.lineSeparator = lineSeparator;
        return this;
    }

    public StringFactory(StringBuilder builder)
    {
        this.app = builder;
        this.cs = builder;
        this.lineSeparator = System.lineSeparator();
    }
    public StringFactory(StringBuffer buffer)
    {
        this.app = buffer;
        this.cs = buffer;
        this.lineSeparator = System.lineSeparator();
    }
    public static StringFactory viaBuffer()
    {
        return new StringFactory(new StringBuffer());
    }
    public static StringFactory viaBuilder()
    {
        return new StringFactory(new StringBuilder());
    }

    @Override
    public int length()
    {
        return this.cs.length();
    }

    @Override
    public char charAt(int index)
    {
        return this.cs.charAt(index);
    }

    /**
     * @apiNote 默认在内部使用 StringBuffer 实现
     * */
    @Override
    @Overload
    @NotNull
    public StringFactory subSequence(int start, int end)
    {
        return subSequence(start, end, true);
    }
    @NotNull StringFactory subSequence(int start, int end, boolean useBuffer)
    {
        var content = this.cs.subSequence(start, end);
        return useBuffer ?
                new StringFactory(new StringBuffer(content)) :
                new StringFactory(new StringBuilder(content));
    }

    @Override
    @NotNull
    public String toString()
    {
        return this.cs.toString();
    }

    @Override
    @SneakyThrows(IOException.class)
    public StringFactory append(CharSequence csq)
    {
        this.app.append(csq);
        return this;
    }

    @Override
    @SneakyThrows(IOException.class)
    public StringFactory append(CharSequence csq, int start, int end)
    {
        this.app.append(csq, start, end);
        return this;
    }

    @Override
    @SneakyThrows(IOException.class)
    public StringFactory append(char c)
    {
        this.app.append(c);
        return this;
    }

    @SneakyThrows(IOException.class)
    public StringFactory appendLine(CharSequence csq)
    {
        this.app.append(csq).append(this.lineSeparator);
        return this;
    }

    @SneakyThrows(IOException.class)
    public StringFactory appendLine(CharSequence csq, int start, int end)
    {
        this.app.append(csq, start, end).append(this.lineSeparator);
        return this;
    }

    @SneakyThrows(IOException.class)
    public StringFactory appendLine(char c)
    {
        this.app.append(c).append(this.lineSeparator);
        return this;
    }
}
