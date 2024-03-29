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
            'a', 'b', 'd', 'e', 'f', 'g', 'h', 'm', 'n', 'r', 't', 'y',
            'A', 'B', 'D', 'E', 'F', 'G', 'H', 'M', 'N', 'T', 'T', 'Y',
            '2', '3', '4', '5', '6', '7', '8', '9',
    };

    /**
     * 获取一组便于阅读的字符. 包含数字和字母
     * */
    public static char[] getEasyChars()
    {
        return EASY_CHARS.clone();
    }

    /**
     * 将驼峰标识符转换为下划线标识符
     * @since 7.8.0
     * */
    public static String camel2underline(String raw)
    {
        var sb = new StringBuilder();
        for (int step = 0; step < raw.length(); step++)
        {
            var ch = raw.charAt(step);
            if (ch >= 'A' && ch <= 'Z')
            {
                var chUpper = (char) (ch + 32);
                if (step > 0) sb.append('_');
                sb.append(chUpper);
            }
            else sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * 将下划线标识符转换为驼峰标识符
     * @since 7.8.0
     * */
    public static String underline2camel(String raw)
    {
        var sb = new StringBuilder();
        for (int step = 0; step < raw.length(); step++)
        {
            var ch = raw.charAt(step);
            if (ch == '_')
            {
                step++;
                if (step < raw.length())
                {
                    var chUpper = (char) (raw.charAt(step) - 32);
                    sb.append(chUpper);
                }
            }
            else sb.append(ch);
        }
        return sb.toString();
    }
}
