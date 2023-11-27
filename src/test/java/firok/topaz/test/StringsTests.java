package firok.topaz.test;

import firok.topaz.general.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringsTests
{
    @Test
    void testCutFix()
    {
        Assertions.assertEquals("abc", Strings.cutPrefix("123abc", "123"));
        Assertions.assertEquals("123", Strings.cutSuffix("123abc", "abc"));

        Assertions.assertEquals("123abc123", Strings.cutSuffix("123abc123", "a"));
        Assertions.assertEquals("123abc123", Strings.cutSuffix("123abc123", "b"));
        Assertions.assertEquals("123abc123", Strings.cutSuffix("123abc123", "321"));
        Assertions.assertEquals("123abc123", Strings.cutPrefix("123abc123", "a"));
        Assertions.assertEquals("123abc123", Strings.cutPrefix("123abc123", "b"));
        Assertions.assertEquals("123abc123", Strings.cutPrefix("123abc123", "321"));

        Assertions.assertEquals("111", Strings.cutPrefix("1111", "1"));
        Assertions.assertEquals("222", Strings.cutPrefix("2222", "2"));
        Assertions.assertEquals("111", Strings.cutSuffix("1111", "1"));
        Assertions.assertEquals("222", Strings.cutSuffix("2222", "2"));

        Assertions.assertEquals("3abc", Strings.cutPrefix("123abc", "12"));
        Assertions.assertEquals("123a", Strings.cutSuffix("123abc", "bc"));

        Assertions.assertEquals("", Strings.cutSuffix("", "1"));
        Assertions.assertEquals("", Strings.cutSuffix("", "2"));
        Assertions.assertEquals("", Strings.cutSuffix("", "asd"));
        Assertions.assertEquals("", Strings.cutSuffix("", "abcd"));
        Assertions.assertEquals("", Strings.cutPrefix("", "1"));
        Assertions.assertEquals("", Strings.cutPrefix("", "2"));
        Assertions.assertEquals("", Strings.cutPrefix("", "asd"));
        Assertions.assertEquals("", Strings.cutPrefix("", "abcd"));

        Assertions.assertEquals("123", Strings.cutSuffix("123", "abcd"));
        Assertions.assertEquals("123", Strings.cutSuffix("123", "456"));
        Assertions.assertEquals("123", Strings.cutSuffix("123", "1a"));
        Assertions.assertEquals("123", Strings.cutPrefix("123", "abcd"));
        Assertions.assertEquals("123", Strings.cutPrefix("123", "456"));
        Assertions.assertEquals("123", Strings.cutPrefix("123", "1a"));

        Assertions.assertEquals("", Strings.cutPrefix("123", "123"));
        Assertions.assertEquals("", Strings.cutPrefix("abc", "abc"));
        Assertions.assertEquals("", Strings.cutPrefix("123abc", "123abc"));
    }
}
