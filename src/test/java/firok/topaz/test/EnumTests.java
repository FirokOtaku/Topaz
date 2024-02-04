package firok.topaz.test;

import firok.topaz.general.Enums;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.EnumSet;

public class EnumTests
{
    public enum EnumTest
    {
        Test1,
        Test2,
        Test3,
        ;

    }

    @Test
    void testEnumMappingValue()
    {
        var setShouldBe = EnumSet.of(EnumTest.Test1, EnumTest.Test2);
        var nameShouldBe = new String[] { "Test1", "Test2" };
        var setReal = Enums.setOf(EnumTest.class, nameShouldBe);
        Assertions.assertEquals(setShouldBe, setReal);

        var setReal2 = Enums.setOf(EnumTest.class, "Test1", "Test2");
        Assertions.assertEquals(setShouldBe, setReal2);

        var setReal3 = Enums.setOf(EnumTest.class, Arrays.asList("Test1", "Test2"));
        Assertions.assertEquals(setShouldBe, setReal3);

        var setReal4 = Enums.setOf(EnumTest.class, Arrays.asList("Test1", "Test2", "", "Test1232141"));
        Assertions.assertEquals(setShouldBe, setReal4);

        var setReal5 = Enums.setOf(EnumTest.class, "Test2", "Test1");
        Assertions.assertEquals(setShouldBe, setReal5);

        var setReal6 = Enums.setOf(EnumTest.class, "Test1", "", "Test2");
        Assertions.assertEquals(setShouldBe, setReal6);
    }

    @Test
    void testMappingName()
    {
        // todo high 有空再写, 反正现在没问题
//        var setShouldBe = Arrays.asList("Test1", "Test2");
//
//        Enums.namesOf();
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
