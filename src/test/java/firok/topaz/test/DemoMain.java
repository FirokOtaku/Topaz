package firok.topaz.test;

import java.util.Arrays;

/**
 * @see ReflectionTests#testExportMainClass
 * */
public class DemoMain
{
    public static void main(String[] args)
    {
        System.out.println("hello world from demo main class");
        System.out.println(Arrays.toString(args));
    }
}
