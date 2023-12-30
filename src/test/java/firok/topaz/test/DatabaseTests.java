package firok.topaz.test;

import firok.topaz.database.Databases;
import firok.topaz.general.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;

import static firok.topaz.general.Collections.sizeOf;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection", "EmptyTryBlock"})
public class DatabaseTests
{
	public static class TestBean
	{
		String key, value;

		public static String convert(String columnName)
		{
			return Strings.underline2camel(columnName.substring(5)).toLowerCase();
		}
	}

	/**
	 * 用内存型 H2 数据库测试 {@link Databases} 的工具方法
	 * */
	@Test
	public void test()
	{
		try(var conn = DriverManager.getConnection("jdbc:h2:mem:test"))
		{
			// 无参数查询

			try(var ignored = Databases.executeUpdate(conn, """
create table test(
`test_key` varchar(64),
`test_value` varchar(64)
)
""")) { }

			try(var ignored = Databases.executeUpdate(conn, """
insert into test (test_key, test_value) values ('k1', 'v1'), ('k2', 'v2'), ('k3', 'v3')
""")) { }

			try(var context = Databases.executeQuery(conn, "select * from test order by test_key"))
			{
				var list = Databases.collect(
						context.rs(),
						TestBean.class,
						TestBean::convert
				);
				Assertions.assertEquals(3, sizeOf(list));
				Assertions.assertEquals("k1", list.get(0).key);
				Assertions.assertEquals("v1", list.get(0).value);
				Assertions.assertEquals("k2", list.get(1).key);
				Assertions.assertEquals("v2", list.get(1).value);
				Assertions.assertEquals("k3", list.get(2).key);
				Assertions.assertEquals("v3", list.get(2).value);
			}

			try(var context = Databases.executeQuery(conn, "select * from test"))
			{
				var list = Databases.collectField(context.rs(), "TEST_KEY", String.class);
				Assertions.assertEquals(3, sizeOf(list));
				Assertions.assertEquals("k1", list.get(0));
				Assertions.assertEquals("k2", list.get(1));
				Assertions.assertEquals("k3", list.get(2));
			}

			try(var context = Databases.executeUpdate(conn, "delete from test where test_key = 'k1'"))
			{
				Assertions.assertEquals(1, context.count());
			}

			try(var context = Databases.executeQuery(conn, "select * from test order by test_key"))
			{
				var list = Databases.collect(
						context.rs(),
						TestBean.class,
						TestBean::convert
				);
				Assertions.assertEquals(2, sizeOf(list));
				Assertions.assertEquals("k2", list.get(0).key);
				Assertions.assertEquals("v2", list.get(0).value);
				Assertions.assertEquals("k3", list.get(1).key);
				Assertions.assertEquals("v3", list.get(1).value);
			}

			try(var context = Databases.executeQuery(conn, "select * from test order by test_key"))
			{
				var list = Databases.collectField(context.rs(), "TEST_KEY", String.class);
				Assertions.assertEquals(2, sizeOf(list));
				Assertions.assertEquals("k2", list.get(0));
				Assertions.assertEquals("k3", list.get(1));
			}

			try(var context = Databases.executeQuery(conn, "select test_key, test_value from test order by test_key"))
			{
				var list = Databases.collectField(context.rs(), 2, String.class);
				Assertions.assertEquals(2, sizeOf(list));
				Assertions.assertEquals("v2", list.get(0));
				Assertions.assertEquals("v3", list.get(1));
			}


			// 有参数查询

			try(var ignored = Databases.executeUpdate(conn, """
create table test2 (
field_str varchar(64),
field_int int,
field_bool bool
)
""")) { }
			try(var ignored = Databases.executeUpdate(conn, """
insert into test2 (field_str, field_int, field_bool) values ('str1', 1, true), ('str2', 2, false), ('str3', 3, true)
""")) { }
			try(var context = Databases.executeQuery(conn, "select * from test2 where field_int >= ? order by field_int", 2))
			{
				var list = Databases.collectField(
						context.rs(),
						1,
						String.class
				);
				Assertions.assertEquals(2, sizeOf(list));
				Assertions.assertEquals("str2", list.get(0));
				Assertions.assertEquals("str3", list.get(1));

			}

			try(var context = Databases.executeQuery(conn, "select * from test2 where field_str = ? order by field_int", "str2"))
			{
				var list = Databases.collectField(context.rs(), "FIELD_STR", String.class);
				Assertions.assertEquals(1, sizeOf(list));
				Assertions.assertEquals("str2", list.get(0));
			}
		}
		catch (Exception any)
		{
			any.printStackTrace(System.err);
		}
	}
}
