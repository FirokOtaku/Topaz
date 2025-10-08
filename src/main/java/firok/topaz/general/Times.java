package firok.topaz.general;

/// 时间运算相关工具方法
/// @since 7.4.0
/// @version 8.0.0
/// @author Firok
public class Times
{
    /// 一秒的毫秒数
    /// @since 8.0.0
    public static final long OneSecond = 1000L;

    /// 一分钟的毫秒数
    /// @since 8.0.0
    public static final long OneMinute = OneSecond * 60;

    /// 一小时的毫秒数
    /// @since 8.0.0
    public static final long OneHour = OneMinute * 60;

    /// 一天的毫秒数
    /// @since 8.0.0
    public static final long OneDay = OneHour * 24;

    /// 一周的毫秒数
    /// @since 8.0.0
    public static final long OneWeek = OneDay * 7;

    /// 一月的毫秒数 (以 30 天计)
    /// @since 8.0.0
    public static final long OneMonth = OneDay * 30;

    /// 一年的毫秒数 (以 365 天计)
    /// @since 8.0.0
    public static final long OneYear = OneDay * 365;

    /// 一世纪的毫秒数 (以每年 365 天计)
    /// @since 8.0.0
    public static final long OneCentury = OneYear * 100;

    /// 一千年的毫秒数 (以每年 365 天计)
    /// @since 8.0.0
    public static final long OneMillennium = OneYear * 1000;

    /// 将 **毫秒** 转化为 **秒**
    public static double seconds(long ms)
    {
        return 1D * ms / OneSecond;
    }

    /// 将 **毫秒** 转化为 **分钟**
    public static double minutes(long ms)
    {
        return 1D * ms / OneMinute;
    }

    /// 将 **毫秒** 转化为 **小时**
    public static double hours(long ms)
    {
        return 1D * ms / OneHour;
    }

    /// 将 **毫秒** 转化为 **天**
    public static double days(long ms)
    {
        return 1D * ms / OneDay;
    }

    /// 将 **毫秒** 转化为 **周**
    public static double weeks(long ms)
    {
        return 1D * ms / OneWeek;
    }

    /// 将 **毫秒** 转化为 **月**. (以 30 天计)
    public static double months(long ms)
    {
        return 1D * ms / OneMonth;
    }

    /// 将 **毫秒** 转化为 **年**. (以每年 365 天计)
    public static double years(long ms)
    {
        return 1D * ms / OneYear;
    }

    /// 将 **毫秒** 转化为 **世纪**. (以每年 365 天计)
    public static double centuries(long ms)
    {
        return 1D * ms / OneCentury;
    }

    /// 将 **毫秒** 转化为 **千年**. (以每年 365 天计)
    public static double millenniums(long ms)
    {
        return 1D * ms / OneMillennium;
    }
}
