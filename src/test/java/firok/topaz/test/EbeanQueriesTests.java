package firok.topaz.test;

import firok.topaz.database.Databases;
import firok.topaz.integration.ebean.Queries;
import firok.topaz.math.Maths;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import lombok.Data;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

/**
 * 测试 ebean 相关内容
 * */
public class EbeanQueriesTests
{
    @Data
    @Entity
    @Table(name = "test")
    public static class TestBean implements Serializable
    {
        @Serial
        private static final long serialVersionUID = 1;

        @Column(name = "test_key")
        String testKey;

        @Column(name = "test_value")
        String testValue;
    }

    /**
     * 测试分页查询相关接口
     * */
//    @Test
    public void testPageMethod() throws Exception
    {
        // 用 H2 数据库作为测试数据库

        var ds = JdbcConnectionPool.create("jdbc:h2:mem:test", "", "");

        var configDb = new DatabaseConfig();
        configDb.setDataSource(ds);
        // 手动创建 ebean 配置的话, 需要自己注册然后加载实体类
        configDb.addPackage("firok.topaz.test");
        configDb.addClass(TestBean.class);
        configDb.setDefaultServer(true);
        configDb.setRegister(true);
        configDb.setDisableLazyLoading(true);

        configDb.loadFromProperties();

        var database = DatabaseFactory.create(configDb);

        try(var conn = ds.getConnection())
        {
            // 准备测试数据

            try(var ignored = Databases.executeUpdate(conn, """
create table test(
    `test_key` varchar(64),
    `test_value` varchar(64)
);
""")) { }
            try(var ignored = Databases.executeUpdate(conn, """
insert into test (test_key, test_value)
values ('k1', 'v1'), ('k2', 'v2'), ('k3', 'v3'), ('k4', 'v4'), ('k5', 'v5'), ('k6', 'v6'), ('k7', 'v7')
""")) { }

            // 调用 ebean 接口

            var page1 = database.find(TestBean.class)
                    .orderBy("test_key")
                    .setFirstRow(Maths.firstRowOf0(1, 2))
                    .setMaxRows(2)
                    .findPagedList();
            var page1static = Queries.pageOf(page1);

            Assertions.assertEquals(7, page1static.getCountRecord());
            Assertions.assertEquals(1, page1static.getPageIndex());
            Assertions.assertEquals(2, page1static.getPageSize());
            Assertions.assertEquals(4, page1static.getCountPage());
            var list1 = page1static.getRecords();
            Assertions.assertEquals("k3", list1.get(0).testKey);
            Assertions.assertEquals("v3", list1.get(0).testValue);
            Assertions.assertEquals("k4", list1.get(1).testKey);
            Assertions.assertEquals("v4", list1.get(1).testValue);
        }
    }
}
