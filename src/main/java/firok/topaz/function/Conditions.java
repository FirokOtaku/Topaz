package firok.topaz.function;

import firok.topaz.annotation.Indev;

import java.io.File;
import java.util.Date;
import java.util.Objects;

import static firok.topaz.general.Collections.isNotEmpty;

/**
 * 用来限定方法运行环境的工具类
 * @since 6.10.0
 * @author Firok
 */
@Indev(experimental = true, description = "暂时还不确定要不要加这么个玩意")
public class Conditions
{
    public static void onlyRun(MayRunnable task, Condition... conditions) throws Exception
    {
        if(isNotEmpty(conditions))
        {
            for(var condition : conditions)
                if(!condition.get())
                    return;
        }
        task.run();
    }
    public static void onlyRun(Runnable task, Condition... conditions)
    {
        if(isNotEmpty(conditions))
        {
            for(var condition : conditions)
                if(!condition.get())
                    return;
        }
        task.run();
    }

    public static Condition whenBefore(Date datetime)
    {
        return () -> new Date().compareTo(datetime) < 0;
    }
    public static Condition whenAfter(Date datetime)
    {
        return () -> new Date().compareTo(datetime) > 0;
    }
    public static Condition whenBetween(Date datetime)
    {
        return () -> {
            var now = new Date();
            return now.compareTo(datetime) > 0 && now.compareTo(datetime) < 0;
        };
    }

    /**
     * 存在指定系统属性时
     * */
    public static Condition whenSystemProperty(String key, String value)
    {
        return () -> Objects.equals(System.getProperty(key), value);
    }

    /**
     * 当上下文中存在指定文件时
     * */
    public static Condition whenFileExist(String file)
    {
        return whenFileExist(new File(file));
    }
    public static Condition whenFileExist(File file)
    {
        return file::exists;
    }

    /**
     * 当上下文中存在指定类时
     * */
    public static Condition whenClassExist(String className)
    {
        return () -> {
            try
            {
                Class.forName(className);
                return true;
            }
            catch (Exception any)
            {
                return false;
            }
        };
    }

    public static final Condition AlwaysTrue = () -> true;
    public static final Condition AlwaysFalse = () -> false;
}
