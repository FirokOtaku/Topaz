package firok.topaz.resource;

import firok.topaz.TopazExceptions;
import firok.topaz.annotation.Overload;
import firok.topaz.function.MustCloseable;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/// 跟资源有关的工具
/// @since 7.15.0
/// @version 8.0.0
/// @author Firok
public final class Resources
{
    private Resources() { }

    /// 关闭所有实例
    /// @return 是否全部成功关闭. 如果没有可供关闭的实例, 则返回true
    @SuppressWarnings("DuplicatedCode")
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

    /// 关闭所有实例
    /// @return 是否全部成功关闭. 如果没有可供关闭的实例, 则返回true
    /// @see #close(AutoCloseable...)
    /// @since 7.53.0
    @SuppressWarnings("DuplicatedCode")
    @Overload
    public static boolean close(Collection<? extends java.lang.AutoCloseable> closeables)
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

    /// 输出所有缓冲区并关闭所有实例
    /// @return 是否全部成功输出并关闭. 如果没有可供关闭的实例, 则返回true
    /// @since 8.0.0
    @SafeVarargs
    @SuppressWarnings("DuplicatedCode")
    public static <TypeEntity extends java.io.Flushable & java.lang.AutoCloseable>
    boolean flushAndClose(TypeEntity... closeables)
    {
        if(closeables == null) return true;

        var hasException = false;
        for(var closable : closeables)
        {
            if(closable == null) continue;
            try { closable.flush(); }
            catch (Exception ignored) { hasException = true; }
            try { closable.close(); }
            catch (Exception ignored) { hasException = true; }
        }
        return !hasException;
    }

    /// 输出所有缓冲区并关闭所有实例
    /// @return 是否全部成功输出并关闭. 如果没有可供关闭的实例, 则返回true
    /// @see #flushAndClose
    /// @since 8.0.0
    @SuppressWarnings("DuplicatedCode")
    @Overload
    public static <TypeEntity extends java.io.Flushable & java.lang.AutoCloseable>
    boolean flushAndClose(Collection<? extends TypeEntity> closeables)
    {
        if(closeables == null) return true;

        var hasException = false;
        for(var closable : closeables)
        {
            if(closable == null) continue;
            try { closable.flush(); }
            catch (Exception ignored) { hasException = true; }
            try { closable.close(); }
            catch (Exception ignored) { hasException = true; }
        }
        return !hasException;
    }

    /// @since 7.31.0
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
    /// @since 7.31.0
    private record ShutdownHookContext(ShutdownHook hook, Object proxy) { }

    /// @since 7.31.0
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

    /// 创建并返回指定对象的代理.
    /// 调用代理对象的 [AutoCloseable#close] 方法会正常执行原对象的 [AutoCloseable#close] 方法.
    /// 如果程序运行时从未调用该代理对象的 [AutoCloseable#close] 方法, 则将会在 JVM 关闭时自动调用一次.
    /// 请注意, 无论 [AutoCloseable#close] 方法是否执行成功,
    /// 该对象都会被移除自等待关闭列表, JVM 关闭时将不会再次调用.
    /// @return 代理对象. 如果对象已经被以相同的 interfaces 模式代理过, 则返回原代理对象
    /// @see #unhookOnShutdown(Object)
    /// @since 7.31.0
    @SuppressWarnings("ConstantValue")
    public static <TypeEntity extends AutoCloseable>
    Object hookOnShutdown(
            @NotNull TypeEntity entity,
            @NotNull Class<?>[] interfaces
    )
    {
        TopazExceptions.ParamValueNoneNull.ifNull(entity);
        TopazExceptions.ParamValueNoneNull.ifNull(interfaces);

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

    /// 解除对象的关闭钩子
    /// @param proxy 要解除钩子的 **代理对象**
    /// @return 是否成功解除指定对象的关闭钩子
    /// @see #hookOnShutdown(AutoCloseable, Class[])
    /// @since 7.31.0
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
