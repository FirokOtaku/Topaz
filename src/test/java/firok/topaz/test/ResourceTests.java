package firok.topaz.test;

import firok.topaz.function.MustCloseable;
import firok.topaz.reflection.Reflections;
import firok.topaz.resource.Resources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static firok.topaz.general.Collections.sizeOf;

public class ResourceTests
{
    public static final class TestClass implements MustCloseable
    {
        @Override
        public void close()
        {
            System.out.println("test class # close");
        }
    }
    @Test
    public void testShutdownHook() throws Exception
    {
        var fieldContexts = Reflections.declaredFieldOf(Resources.class, "contexts");
        fieldContexts.setAccessible(true);
        var valueContexts = (List<?>) fieldContexts.get(null);

        var testObj1 = new TestClass();
        Assertions.assertEquals(0, sizeOf(valueContexts));
        var proxyTestObj1 = (AutoCloseable) Resources.hookOnShutdown(testObj1, new Class<?>[]{MustCloseable.class});
        Assertions.assertEquals(1, sizeOf(valueContexts));
        proxyTestObj1.close();
        Assertions.assertEquals(0, sizeOf(valueContexts));

        // 检查重复注册情况
        var testObj2 = new TestClass();
        Assertions.assertEquals(0, sizeOf(valueContexts));
        var proxyTestObj2 = (AutoCloseable) Resources.hookOnShutdown(testObj2, new Class<?>[]{MustCloseable.class});
        var proxyTestObj2_repeat = (AutoCloseable) Resources.hookOnShutdown(testObj2, new Class<?>[]{MustCloseable.class});
        Assertions.assertTrue(proxyTestObj2 == proxyTestObj2_repeat);
        Assertions.assertEquals(1, sizeOf(valueContexts));
        proxyTestObj2.close();
        proxyTestObj2.close();
        Assertions.assertEquals(0, sizeOf(valueContexts));

    }
}
