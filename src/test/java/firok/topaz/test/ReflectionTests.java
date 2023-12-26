package firok.topaz.test;

import firok.topaz.annotation.Indev;
import firok.topaz.annotation.SupportedMinimalVersion;
import firok.topaz.function.TriConsumer;
import firok.topaz.reflection.ReflectionDirection;
import firok.topaz.reflection.Reflections;
import firok.topaz.thread.Threads;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ReflectionTests
{
	public static class Class1
	{
		String f1;
		int f2;
	}
	public static class Class2 extends Class1
	{
		Integer f3;
	}
	public static class Class3 extends Class1
	{
		Integer f2;
	}

	static Map<String, Class<?>> map(List<Field> list)
	{
		var map = new HashMap<String, Class<?>>();
		for(var field : list)
		{
			map.put(field.getName(), field.getType());
		}
		return map;
	}

	@Test
	void test()
	{
		var lf1 = Reflections.listAllFieldsOf(Class1.class);
		var mf1 = map(lf1);
		var mf1_should = new HashMap<>();
		mf1_should.put("f1", String.class);
		mf1_should.put("f2", int.class);
		Assertions.assertEquals(mf1_should, mf1);

		var lf2 = Reflections.listAllFieldsOf(Class2.class);
		var mf2 = map(lf2);
		var mf2_should = new HashMap<>();
		mf2_should.put("f1", String.class);
		mf2_should.put("f2", int.class);
		mf2_should.put("f3", Integer.class);
		Assertions.assertEquals(mf2_should, mf2);

		var lf3 = Reflections.listAllFieldsOf(Class3.class);
		var assertField = (TriConsumer<Field, String, Class<?>>) (field, name, type) -> {
			Assertions.assertEquals(name, field.getName());
			Assertions.assertEquals(type, field.getType());
		};
		Assertions.assertEquals(3, lf3.size());
		assertField.accept(lf3.get(0), "f2", Integer.class); // 先获取到子类字段
		assertField.accept(lf3.get(1), "f1", String.class);
		assertField.accept(lf3.get(2), "f2", int.class); // 被隐藏的字段也会被返回
	}

//	@Test
	public void testFindPackageAnnotation()
	{
		var packageReflections = Reflections.class.getPackage();
		var clReflections = Reflections.class.getClassLoader();
		Reflections.findAnnotationsOf(packageReflections, clReflections, Indev.class, ReflectionDirection.ParentToChild);
		Reflections.findAnnotationsOf(packageReflections, clReflections, Indev.class, ReflectionDirection.ChildToParent);
	}

	@Test
	public void testMethodClass() throws Exception
	{
		Method m = SupportedMinimalVersion.class.getMethod("value");
		System.out.println(m);
		System.out.println(m.getDeclaringClass());
	}

	@Test
	public void testCallerClass() throws Exception
	{
		System.out.println(Reflections.getCallerClass());
		System.out.println(Reflections.getCallerMethod());
	}

	/**
	 * 测试类结构反射信息
	 * */
	@Test
	public void testClassStructure() throws Exception
	{
		interface InterfaceTest { void test(); }
		interface InterfaceTest2 { default void test2() { } }
		class ClassWith12 implements InterfaceTest, InterfaceTest2
		{
			public void test() { }
		}

		System.out.println("find test()");
		var listTest = Reflections.findNamedMethodsFrom(ClassWith12.class, "test");
		System.out.println(listTest);

	}

	/**
	 * 测试导出并运行一个主类
	 * */
	@Test
	public void testExportMainClass() throws IOException
	{
		System.out.println("导出可执行类");
		var folderBase = new File("./test-cache").getCanonicalFile();
		var info = Reflections.exportExecutableClassFile(folderBase, DemoMain.class);
		var cmd = info.getExecutionCommand(null, null);
		Process process = null;
		try
		{
			System.out.println("创建子进程, 使用当前 jvm 调用可执行类");
			System.out.println("需要执行的指令为: [" + cmd + "]");
			process = Runtime.getRuntime().exec(cmd);
			var ips = process.getInputStream();
			Threads.start(true, () -> ips.transferTo(System.out));
			int ret = process.waitFor();
			Assertions.assertEquals(0, ret);

			System.out.println("成功完成调用可执行类");
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(process != null && process.isAlive())
				process.destroyForcibly();
		}

		// 导出没有 main 方法的类会抛出异常
		Assertions.assertThrows(
				Exception.class,
				() -> Reflections.exportExecutableClassFile(folderBase, ReflectionTests.class)
		);
	}
}
