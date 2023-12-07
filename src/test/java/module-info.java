/**
 * Topaz 测试模块
 * */
module firok.topaz.test {
    requires firok.topaz;
    requires java.desktop;
    requires org.junit.jupiter.api;
    requires org.jetbrains.annotations;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    opens firok.topaz.test;
    opens firok.topaz.test.indev;
}
