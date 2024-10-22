package firok.topaz.resource;

import firok.topaz.reflection.Reflections;

/**
 * @author Firok
 * @since 7.32.0
 * */
public class AbstractAutoClosing implements AutoCloseable
{
    private final Object proxy;
    AbstractAutoClosing()
    {
        this.proxy = Resources.hookOnShutdown(this, new Class[] {
                AutoCloseable.class
        });
    }

    @Override
    public void close() throws Exception
    {
        Resources.unhookOnShutdown(proxy);
    }
}
