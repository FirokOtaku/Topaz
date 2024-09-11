package firok.topaz.resource;

import firok.topaz.TopazExceptions;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
     * @return 是否全部成功关闭. 如果没有可供关闭的实例, 则返回true
     * */
    public static boolean close(java.lang.AutoCloseable... closeables)
    {
        if(closeables == null) return true;

        var hasException = false;
        for(var closable : closeables)
        {
            if(closable == null) continue;
            try { closable.close(); }
            catch (Exception ignored) { hasException = true; }
        }
        return !hasException;
    }

    /**
     * @since 7.31.0
     * */
    private record ShutdownHook(AutoCloseable entity, Class<?>[] interfaces)
            implements InvocationHandler
    {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            try
            {
                return method.invoke(entity, args);
            }
            finally
            {
                var isMethodClose = method.getName().equals("close") &&
                        method.getParameterCount() == 0; // todo 这里以后可能换成其它实现方式

                if(isMethodClose)
                {
                    synchronized (contexts)
                    {
                        contexts.removeIf(context -> Objects.equals(context.proxy, proxy));
                    }
                }
            }
        }
    }
    /**
     * @since 7.31.0
     * */
    private record ShutdownHookContext(ShutdownHook hook, Object proxy) { }

    /**
     * @since 7.31.0
     * */
    private static final List<ShutdownHookContext> contexts = new LinkedList<>();
    static
    {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            synchronized (contexts)
            {
                for(var context : contexts)
                {
                    try { context.hook().entity().close(); }
                    catch (Exception ignored) { }
                }
                contexts.clear();
            }
        }));
    }

    /**
     * 创建并返回指定对象的代理.
     * 调用代理对象的 {@link AutoCloseable#close} 方法会正常执行原对象的 {@link AutoCloseable#close} 方法.
     * 如果程序运行时从未调用该代理对象的 {@link AutoCloseable#close} 方法, 则将会在 JVM 关闭时自动调用一次.
     * 请注意, 无论 {@link AutoCloseable#close} 方法是否执行成功,
     * 该对象都会被移除自等待关闭列表, JVM 关闭时将不会再次调用.
     *
     * @return 代理对象. 如果对象已经被以相同的 interfaces 模式代理过, 则返回原代理对象
     * @see #unhookOnShutdown(Object)
     * @since 7.31.0
     * */
    @SuppressWarnings("ConstantValue")
    public static <TypeEntity extends AutoCloseable>
    Object hookOnShutdown(
            @NotNull TypeEntity entity,
            @NotNull Class<?>[] interfaces
    )
    {
        TopazExceptions.ParamValueNoneNull.maybe(entity == null);
        TopazExceptions.ParamValueNoneNull.maybe(interfaces == null);

        assert entity != null;
        assert interfaces != null;

        var cl = entity.getClass().getClassLoader();
        var hook = new ShutdownHook(entity, interfaces.clone());
        var proxy = Proxy.newProxyInstance(cl, interfaces, hook);
        synchronized (contexts)
        {
            for(var context : contexts)
            {
                var hookOld = context.hook();
                if(hookOld.entity() == entity && Arrays.equals(hookOld.interfaces(), interfaces)) return context.proxy();
            }
            var context = new ShutdownHookContext(hook, proxy);
            contexts.add(context);
        }
        return proxy;
    }

    /**
     * 解除对象的关闭钩子
     * @param proxy 要解除钩子的 <b>代理对象</b>
     * @return 是否成功解除指定对象的关闭钩子
     * @see #hookOnShutdown(AutoCloseable, Class[])
     * @since 7.31.0
     * */
    @SuppressWarnings("ConstantValue")
    public static boolean unhookOnShutdown(
            @NotNull Object proxy
    )
    {
        TopazExceptions.ParamValueNoneNull.maybe(proxy == null);

        assert proxy != null;

        synchronized (contexts)
        {
            return contexts.removeIf(context -> Objects.equals(context.proxy, proxy));
        }
    }
}
