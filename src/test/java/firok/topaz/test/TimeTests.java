package firok.topaz.test;

import firok.topaz.general.Times;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class TimeTests
{
    static final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");

    static final TimeZone tzUtc = TimeZone.getTimeZone("UTC"); // +0
    static final TimeZone tzGmt1 = TimeZone.getTimeZone("GMT+1"); // +1
    static final TimeZone tzGmt2 = TimeZone.getTimeZone("GMT+2");
    static final TimeZone tzGmt8 = TimeZone.getTimeZone("Asia/Shanghai"); // +8
    static final TimeZone tzGmt9 = TimeZone.getTimeZone("Asia/Tokyo"); // +9

    @Test
    public void testCalcDayRange() throws ParseException
    {
        var day1mid = f.parse("2026-01-01 12:00:00.000+0800");
        var day1start = f.parse("2026-01-01 00:00:00.000+0800");
        var day1end = f.parse("2026-01-01 23:59:59.999+0800");
        var rangeDay1 = Times.getTimestampRangeOfDay(day1mid.getTime(), tzGmt8);
        Assertions.assertEquals(day1start.getTime(), rangeDay1[0]);
        Assertions.assertEquals(day1end.getTime(), rangeDay1[1]);

        var day2mid = f.parse("2025-01-01 00:00:01.000+0800");
        var day2start = f.parse("2025-01-01 00:00:00.000+0800");
        var day2end = f.parse("2025-01-01 23:59:59.999+0800");
        var rangeDay2 = Times.getTimestampRangeOfDay(day2mid.getTime(), tzGmt8);
        Assertions.assertEquals(day2start.getTime(), rangeDay2[0]);
        Assertions.assertEquals(day2end.getTime(), rangeDay2[1]);

        var day3mid = f.parse("2025-01-05 23:34:21.000+0800");
        var day3start = f.parse("2025-01-05 00:00:00.000+0800");
        var day3end = f.parse("2025-01-05 23:59:59.999+0800");
        var rangeDay3 = Times.getTimestampRangeOfDay(day3mid.getTime(), tzGmt8);
        Assertions.assertEquals(day3start.getTime(), rangeDay3[0]);
        Assertions.assertEquals(day3end.getTime(), rangeDay3[1]);

        var day4mid = f.parse("2025-01-05 23:34:21.000+0900");
        var day4start = f.parse("2025-01-05 00:00:00.000+0900");
        var day4end = f.parse("2025-01-05 23:59:59.999+0900");
        var rangeDay4 = Times.getTimestampRangeOfDay(day4mid.getTime(), tzGmt9);
        Assertions.assertEquals(day4start.getTime(), rangeDay4[0]);
        Assertions.assertEquals(day4end.getTime(), rangeDay4[1]);

        var day5mid = f.parse("2025-01-05 23:34:21.000+0000");
        var day5start = f.parse("2025-01-05 00:00:00.000+0000");
        var day5end = f.parse("2025-01-05 23:59:59.999+0000");
        var rangeDay5 = Times.getTimestampRangeOfDay(day5mid.getTime(), tzUtc);
        Assertions.assertEquals(day5start.getTime(), rangeDay5[0]);
        Assertions.assertEquals(day5end.getTime(), rangeDay5[1]);

        var day6mid = f.parse("2025-12-31 23:01:01.000+0100");
        var day6start = f.parse("2025-12-31 00:00:00.000+0100");
        var day6end = f.parse("2025-12-31 23:59:59.999+0100");
        var rangeDay6 = Times.getTimestampRangeOfDay(day6mid.getTime(), tzGmt1);
        Assertions.assertEquals(day6start.getTime(), rangeDay6[0]);
        Assertions.assertEquals(day6end.getTime(), rangeDay6[1]);

        var day7mid = f.parse("2027-02-28 14:01:01.000+0000");
        var day7start = f.parse("2027-02-28 00:00:00.000+0000");
        var day7end = f.parse("2027-02-28 23:59:59.999+0000");
        var rangeDay7 = Times.getTimestampRangeOfDay(day7mid.getTime(), tzUtc);
        Assertions.assertEquals(day7start.getTime(), rangeDay7[0]);
        Assertions.assertEquals(day7end.getTime(), rangeDay7[1]);
    }

    @Test
    public void testCalcMonthRange() throws ParseException
    {
        var m1mid = f.parse("2025-01-12 02:32:43.000+0800");
        var m1start = f.parse("2025-01-01 00:00:00.000+0800");
        var m1end = f.parse("2025-01-31 23:59:59.999+0800");
        var rangeM1 = Times.getTimestampRangeOfMonth(m1mid.getTime(), tzGmt8);
        Assertions.assertEquals(m1start.getTime(), rangeM1[0]);
        Assertions.assertEquals(m1end.getTime(), rangeM1[1]);

        var m2mid = f.parse("2025-02-12 02:32:43.000+0800");
        var m2start = f.parse("2025-02-01 00:00:00.000+0800");
        var m2end = f.parse("2025-02-28 23:59:59.999+0800");
        var rangeM2 = Times.getTimestampRangeOfMonth(m2mid.getTime(), tzGmt8);
        Assertions.assertEquals(m2start.getTime(), rangeM2[0]);
        Assertions.assertEquals(m2end.getTime(), rangeM2[1]);

        var m3mid = f.parse("2025-02-28 04:32:43.000+0900");
        var m3start = f.parse("2025-02-01 00:00:00.000+0900");
        var m3end = f.parse("2025-02-28 23:59:59.999+0900");
        var rangeM3 = Times.getTimestampRangeOfMonth(m3mid.getTime(), tzGmt9);
        Assertions.assertEquals(m3start.getTime(), rangeM3[0]);
        Assertions.assertEquals(m3end.getTime(), rangeM3[1]);

        var m4mid = f.parse("2025-02-28 04:32:43.000+0000");
        var m4start = f.parse("2025-02-01 00:00:00.000+0000");
        var m4end = f.parse("2025-02-28 23:59:59.999+0000");
        var rangeM4 = Times.getTimestampRangeOfMonth(m4mid.getTime(), tzUtc);
        Assertions.assertEquals(m4start.getTime(), rangeM4[0]);
        Assertions.assertEquals(m4end.getTime(), rangeM4[1]);

        var m5mid = f.parse("2025-02-28 04:32:43.000+0100");
        var m5start = f.parse("2025-02-01 00:00:00.000+0100");
        var m5end = f.parse("2025-02-28 23:59:59.999+0100");
        var rangeM5 = Times.getTimestampRangeOfMonth(m5mid.getTime(), tzGmt1);
        Assertions.assertEquals(m5start.getTime(), rangeM5[0]);
        Assertions.assertEquals(m5end.getTime(), rangeM5[1]);

        var m6mid = f.parse("2025-03-16 04:32:43.000+0200");
        var m6start = f.parse("2025-03-01 00:00:00.000+0200");
        var m6end = f.parse("2025-03-31 23:59:59.999+0200");
        var rangeM6 = Times.getTimestampRangeOfMonth(m6mid.getTime(), tzGmt2);
        Assertions.assertEquals(m6start.getTime(), rangeM6[0]);
        Assertions.assertEquals(m6end.getTime(), rangeM6[1]);

        var m7mid = f.parse("2026-12-31 21:23:22.000+0800");
        var m7start = f.parse("2026-12-01 00:00:00.000+0800");
        var m7end = f.parse("2026-12-31 23:59:59.999+0800");
        var rangeM7 = Times.getTimestampRangeOfMonth(m7mid.getTime(), tzGmt8);
        Assertions.assertEquals(m7start.getTime(), rangeM7[0]);
        Assertions.assertEquals(m7end.getTime(), rangeM7[1]);

        var m8mid = f.parse("2027-07-30 14:01:01.000+0000");
        var m8start = f.parse("2027-07-01 00:00:00.000+0000");
        var m8end = f.parse("2027-07-31 23:59:59.999+0000");
        var rangeM8 = Times.getTimestampRangeOfMonth(m8mid.getTime(), tzUtc);
        Assertions.assertEquals(m8start.getTime(), rangeM8[0]);
        Assertions.assertEquals(m8end.getTime(), rangeM8[1]);
    }

    @Test
    public void testCalcYearRange() throws ParseException
    {
        var y1mid = f.parse("2025-01-12 02:32:43.000+0800");
        var y1start = f.parse("2025-01-01 00:00:00.000+0800");
        var y1end = f.parse("2025-12-31 23:59:59.999+0800");
        var rangeY1 = Times.getTimestampRangeOfYear(y1mid.getTime(), tzGmt8);
        Assertions.assertEquals(y1start.getTime(), rangeY1[0]);
        Assertions.assertEquals(y1end.getTime(), rangeY1[1]);

        var y2mid = f.parse("2025-02-28 04:32:43.000+0900");
        var y2start = f.parse("2025-01-01 00:00:00.000+0900");
        var y2end = f.parse("2025-12-31 23:59:59.999+0900");
        var rangeY2 = Times.getTimestampRangeOfYear(y2mid.getTime(), tzGmt9);
        Assertions.assertEquals(y2start.getTime(), rangeY2[0]);
        Assertions.assertEquals(y2end.getTime(), rangeY2[1]);

        var y3mid = f.parse("2026-02-28 04:32:43.000+0000");
        var y3start = f.parse("2026-01-01 00:00:00.000+0000");
        var y3end = f.parse("2026-12-31 23:59:59.999+0000");
        var rangeY3 = Times.getTimestampRangeOfYear(y3mid.getTime(), tzUtc);
        Assertions.assertEquals(y3start.getTime(), rangeY3[0]);
        Assertions.assertEquals(y3end.getTime(), rangeY3[1]);

        var y4mid = f.parse("2027-12-31 04:32:43.000+0100");
        var y4start = f.parse("2027-01-01 00:00:00.000+0100");
        var y4end = f.parse("2027-12-31 23:59:59.999+0100");
        var rangeY4 = Times.getTimestampRangeOfYear(y4mid.getTime(), tzGmt1);
        Assertions.assertEquals(y4start.getTime(), rangeY4[0]);
        Assertions.assertEquals(y4end.getTime(), rangeY4[1]);
    }


}
