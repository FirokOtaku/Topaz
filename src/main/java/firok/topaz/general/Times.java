package firok.topaz.general;

/**
 * 计算时间相关工具方法
 * @since 7.4.0
 * @author Firok
 * */
public class Times
{
    public static double seconds(long ms)
    {
        return ms / 1000.D;
    }

    public static double minutes(long ms)
    {
        return ms / 1000.D / 60.D;
    }

    public static double hours(long ms)
    {
        return ms / 1000.D / 60.D / 60.D;
    }

    public static double days(long ms)
    {
        return ms / 1000.D / 60.D / 60.D / 24.D;
    }

    public static double weeks(long ms)
    {
        return ms / 1000.D / 60.D / 60.D / 24.D / 7.D;
    }

    public static double months(long ms)
    {
        return ms / 1000.D / 60.D / 60.D / 24.D / 30.D;
    }

    public static double years(long ms)
    {
        return ms / 1000.D / 60.D / 60.D / 24.D / 365.D;
    }

    public static double centuries(long ms)
    {
        return ms / 1000.D / 60.D / 60.D / 24.D / 365.D / 100.D;
    }

    public static double millenniums(long ms)
    {
        return ms / 1000.D / 60.D / 60.D / 24.D / 365.D / 1000.D;
    }
}
