package firok.topaz.resource;

import firok.topaz.TopazExceptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import static firok.topaz.general.Collections.isEmpty;

/**
 * 流操作相关工具方法
 * @since 5.16.0
 * @author Firok
 * */
public final class Streams
{
    private Streams() { }

    /**
     * @see InputStream.DEFAULT_BUFFER_SIZE
     * */
    @SuppressWarnings("JavadocReference")
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * 将一个输入流的数据导出到多个输出流
     * @throws IOException 任何读写操作出现问题都会抛出
     * @see InputStream#transferTo(OutputStream) 基本复制自此方法
     * */
    public long transferTo(InputStream ibs, OutputStream... arrOs) throws IOException
    {
        TopazExceptions.ParamValueNoneNull.maybe(isEmpty(arrOs));
        for(var os : arrOs) TopazExceptions.ParamValueNoneNull.maybe(os == null);

        long transferred = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int read;
        while ((read = ibs.read(buffer, 0, DEFAULT_BUFFER_SIZE)) >= 0)
        {
            for(var os : arrOs)
            {
                os.write(buffer, 0, read);
            }
            transferred += read;
        }
        return transferred;
    }
}
