package firok.topaz.general;

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
}
