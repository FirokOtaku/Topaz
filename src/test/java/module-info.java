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
    requires java.sql;
    requires com.h2database;
    requires redis.clients.jedis;

    opens firok.topaz.test;
}
