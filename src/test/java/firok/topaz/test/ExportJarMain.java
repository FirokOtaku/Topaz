package firok.topaz.test;

import firok.topaz.Topaz;
import firok.topaz.general.Collections;
import firok.topaz.math.Maths;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 用来测试整体导出一个 jar 的测试主类
 * */
public class ExportJarMain
{
    public static void main(String[] args)
    {
        System.out.println("exported by Topaz: " + Topaz.META.version);
        System.out.println("main method of ExportJarMain");
        System.out.println(Arrays.toString(args));

        var list = new HashSet<Double>();
        if(args != null) for(var arg : args)
        {
            var num = Maths.parseDouble(arg, 0);
            list.add(num);
        }
        var min = Maths.min(list);
        var max = Maths.max(list);
        System.out.println("min = " + min);
        System.out.println("max = " + max);
        System.out.println("count = " + Collections.sizeOf(list));
    }
}
