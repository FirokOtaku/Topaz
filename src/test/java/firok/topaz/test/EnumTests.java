package firok.topaz.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class EnumTests
{
    public enum EnumTest
    {
        Test1,
        Test2,
        Test3,
        ;

    }
    /**
     * 高版本 JDK 在模块化状态下访问这个 Unsafe 比较困难, 还是不这么玩了
     * */
    @Test
    void testEnumReflection() throws Exception
    {
        Assertions.assertThrows(InaccessibleObjectException.class, () -> {
            System.out.println("fields");
            for(var field : EnumTest.class.getDeclaredFields())
            {
                System.out.println(field);
            }
            System.out.println("methods");
            for(var method : EnumTest.class.getDeclaredMethods())
            {
                System.out.println(method);
            }

            System.out.println("values before");
            System.out.println(Arrays.asList(EnumTest.values()));

            var fieldToModify = EnumTest.class.getDeclaredField("$VALUES");
            var classUnsafe = Class.forName("jdk.internal.misc.Unsafe");
            var fieldUnsafe = classUnsafe.getDeclaredField("theUnsafe");
            fieldUnsafe.setAccessible(true);
            var unsafe = fieldUnsafe.get(null);

            var methodFieldBase = classUnsafe.getDeclaredMethod("staticFieldBase", Field.class);
            var methodFieldOffset = classUnsafe.getDeclaredMethod("staticFieldOffset", Field.class);
            var methodPutObject = classUnsafe.getDeclaredMethod("putObject", Object.class, long.class, Object.class);
            Object fieldBase = methodFieldBase.invoke(unsafe, fieldToModify);
            long fieldOffset = (long) methodFieldOffset.invoke(unsafe, fieldToModify);
            methodPutObject.invoke(unsafe, fieldBase, fieldOffset, new EnumTest[]{EnumTest.Test1, EnumTest.Test3});

            System.out.println("values after");
            System.out.println(Arrays.asList(EnumTest.values()));
        });
    }
}
