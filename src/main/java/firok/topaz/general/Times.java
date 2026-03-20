package firok.topaz.general;

import firok.topaz.annotation.Overload;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.TimeZone;

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

    /// 获取时区对应的时区时间偏移量
    ///
    /// @since 8.1.0
    private static ZoneOffset toZoneOffset(TimeZone tz)
    {
        return ZoneOffset.ofHours((int) (tz.getRawOffset() / OneHour));
    }

    /// 获取指定时间戳所在 **天** 起始和结束的时间戳
    ///
    /// @return 返回一个长度为 2 的数组.
    ///         第一个元素为起始时间戳, 也就是所在时区的 `00:00:00.000`;
    ///         第二个元素为结束时间戳, 也就是所在时区的 `23:59:59.999`
    /// @since 8.1.0
    public static long[] getTimestampRangeOfDay(long timestamp, TimeZone tz) // todo 重命名方法
    {
        var offset = tz.getRawOffset();
        long dayOfTimestamp = (timestamp + offset) / OneDay;
        long timestampOfDayStart = dayOfTimestamp * OneDay;
        long timestampOfDayEnd = timestampOfDayStart + OneDay - 1;
        return new long[] { timestampOfDayStart - offset, timestampOfDayEnd - offset };
    }
    /// 获取指定时间戳所在 **天** 起始和结束的时间戳, 基于系统默认时区
    ///
    /// @since 8.1.0
    @Overload
    public static long[] getTimestampRangeOfDay(long timestamp)
    {
        return getTimestampRangeOfDay(timestamp, TimeZone.getDefault());
    }
    /// @since 8.1.0
    @Overload
    public static long[] getTimestampRangeOfDay(@NotNull Date timestamp, TimeZone tz)
    {
        return getTimestampRangeOfDay(timestamp.getTime(), tz);
    }
    /// 获取指定时间戳所在 **天** 起始和结束的时间戳, 基于系统默认时区
    ///
    /// @since 8.1.0
    public static long[] getTimestampRangeOfDay(@NotNull Date timestamp)
    {
        return getTimestampRangeOfDay(timestamp.getTime(), TimeZone.getDefault());
    }

    /// 获取指定时间戳所在 **月** 起始和结束的时间戳
    ///
    /// @return 返回一个长度为 2 的数组.
    ///         第一个元素为起始时间戳, 也就是当月第一天的 `00:00:00.000`;
    ///         第二个元素为结束时间戳, 也就是当月最后一天的 `23:59:59.999`
    /// @since 8.1.0
    public static long[] getTimestampRangeOfMonth(long timestamp, TimeZone tz)
    {
        var offset = toZoneOffset(tz);
        var instant = Instant.ofEpochMilli(timestamp);
        var date = LocalDate.ofInstant(instant, tz.toZoneId());
        var dateFirstDay = date.with(TemporalAdjusters.firstDayOfMonth());
        var dateLastDay = date.with(TemporalAdjusters.lastDayOfMonth());
        var timestampOfMonthStart = dateFirstDay.atTime(0, 0, 0).toInstant(offset).toEpochMilli();
        var timestampOfMonthEnd = dateLastDay.atTime(23, 59, 59).toInstant(offset).toEpochMilli() + 999;
        return new long[] { timestampOfMonthStart, timestampOfMonthEnd };
    }
    /// 获取指定时间戳所在 **月** 起始和结束的时间戳, 基于系统默认时区
    ///
    /// @since 8.1.0
    @Overload
    public static long[] getTimestampRangeOfMonth(long timestamp)
    {
        return getTimestampRangeOfMonth(timestamp, TimeZone.getDefault());
    }
    /// 获取指定时间戳所在 **月** 起始和结束的时间戳
    ///
    /// @since 8.1.0
    @Overload
    public static long[] getTimestampRangeOfMonth(@NotNull Date timestamp, TimeZone tz)
    {
        return getTimestampRangeOfMonth(timestamp.getTime(), tz);
    }
    /// 获取指定时间戳所在 **月** 起始和结束的时间戳, 基于系统默认时区
    ///
    /// @since 8.1.0
    @Overload
    public static long[] getTimestampRangeOfMonth(@NotNull Date timestamp)
    {
        return getTimestampRangeOfMonth(timestamp.getTime(), TimeZone.getDefault());
    }

    /// 获取指定时间戳所在 **年** 起始和结束的时间戳
    ///
    /// @return 返回一个长度为 2 的数组.
    ///         第个元素为起始时间戳, 也就是当年第一天的 `00:00:00.000`;
    ///         第二个元素为结束时间戳, 也就是当年最后一天的 `23:59:59.999`
    /// @since 8.1.0
    public static long[] getTimestampRangeOfYear(long timestamp, TimeZone tz)
    {
        var offset = toZoneOffset(tz);
        var instant = Instant.ofEpochMilli(timestamp);
        var date = LocalDate.ofInstant(instant, tz.toZoneId());
        var dateFirstDay = date.with(TemporalAdjusters.firstDayOfYear());
        var dateLastDay = date.with(TemporalAdjusters.lastDayOfYear());
        var timestampOfYearStart = dateFirstDay.atTime(0, 0, 0).toInstant(offset).toEpochMilli();
        var timestampOfYearEnd = dateLastDay.atTime(23, 59, 59).toInstant(offset).toEpochMilli() + 999;
        return new long[] { timestampOfYearStart, timestampOfYearEnd };
    }
    /// 获取指定时间戳所在 **年** 起始和结束的时间戳, 基于系统默认时区
    ///
    /// @since 8.1.0
    @Overload
    public static long[] getTimestampRangeOfYear(long timestamp)
    {
        return getTimestampRangeOfYear(timestamp, TimeZone.getDefault());
    }
    /// 获取指定时间戳所在 **年** 起始和结束的时间戳, 基于系统默认时区
    ///
    /// @since 8.1.0
    @Overload
    public static long[] getTimestampRangeOfYear(@NotNull Date timestamp)
    {
        return getTimestampRangeOfYear(timestamp.getTime(), TimeZone.getDefault());
    }
    /// 获取指定时间戳所在 **年** 起始和结束的时间戳
    ///
    /// @since 8.1.0
    @Overload
    public static long[] getTimestampRangeOfYear(@NotNull Date timestamp, TimeZone tz)
    {
        return getTimestampRangeOfYear(timestamp.getTime(), tz);
    }
}
