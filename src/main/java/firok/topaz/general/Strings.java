package firok.topaz.general;

import firok.topaz.annotation.Indev;

/**
 * 字符串工具类
 *
 * @since 5.17.0
 * @author Firok
 * */
public final class Strings
{
    private Strings() { }

    /**
     * 移除前缀
     * */
    public static String cutPrefix(String value, String prefix)
    {
        return value.startsWith(prefix) ? value.substring(prefix.length()) : value;
    }

    /**
     * 移除后缀
     * */
    public static String cutSuffix(String value, String suffix)
    {
        return value.endsWith(suffix) ? value.substring(0, value.length() - suffix.length()) : value;
    }

    /**
     * 便于识别的字符列表
     * */
    private static final char[] EASY_CHARS = new char[] {
            'a', 'b', 'd', 'e', 'f', 'g', 'h', 'j', 'm', 'n', 'r', 't', 'y',
            'A', 'B', 'D', 'E', 'F', 'G', 'H', 'J', 'M', 'N', 'T', 'T', 'Y',
            '1', '2', '3', '4', '5', '6', '7', '8', '9',
    };

    /**
     * 获取一组便于阅读的字符. 包含数字和字母
     * */
    @Indev
    public static char[] getEasyChars()
    {
        return EASY_CHARS.clone();
    }
}
