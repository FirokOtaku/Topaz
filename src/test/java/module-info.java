/**
 * Topaz 测试模块
 * */
module firok.topaz.test {
    requires firok.topaz;
    requires org.junit.jupiter.api;
    requires org.jetbrains.annotations;
    requires com.h2database;
    requires org.apache.commons.pool2;
    requires redis.clients.jedis;
    requires io.ebean.api;
    requires lombok;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires org.slf4j;
    requires io.ebean.types;

    opens firok.topaz.test;
}
