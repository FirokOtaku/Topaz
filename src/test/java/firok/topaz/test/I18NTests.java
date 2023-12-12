package firok.topaz.test;

import firok.topaz.general.I18N;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class I18NTests
{
    @Test
    void testI18N()
    {
        var i18n = new I18N("/firok/topaz/test/test", Locale.US, I18NTests.class);
        Assertions.assertEquals("测试信息01", i18n.localize(Locale.CHINA, "test-msg-01"));
        Assertions.assertEquals("测试信息02", i18n.localize(Locale.CHINA, "test-msg-02"));
        Assertions.assertEquals("test msg 03", i18n.localize(Locale.CHINA, "test-msg-03"));
        Assertions.assertEquals("测试信息04", i18n.localize(Locale.CHINA, "test-msg-04"));

        Assertions.assertEquals("test msg 01", i18n.localize("test-msg-01"));
        Assertions.assertEquals("test msg 02", i18n.localize("test-msg-02"));
        Assertions.assertEquals("test msg 03", i18n.localize("test-msg-03"));
        Assertions.assertNull(i18n.localize("test-msg-04"));
    }
}
