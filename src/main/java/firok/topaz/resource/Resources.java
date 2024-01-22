package firok.topaz.resource;

/**
 * 跟资源有关的工具
 * @since 7.15.0
 * @author Firok
 * */
public final class Resources
{
    private Resources() { }

    /**
     * 关闭所有实例
     * @return 是否全部成功关闭
     * */
    public static boolean close(java.lang.AutoCloseable... closeables)
    {
        var hasException = false;
        for(var closable : closeables)
        {
            if(closable == null) continue;
            try { closable.close(); }
            catch (Exception ignored) { hasException = true; }
        }
        return !hasException;
    }
}
