package firok.topaz;

import firok.topaz.annotation.Indev;
import firok.topaz.function.TriConsumer;
import firok.topaz.reflection.ReflectionDirection;
import firok.topaz.reflection.Reflections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
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
		TriConsumer<Field, String, Class<?>> assertField = (field, name, type) -> {
			Assertions.assertEquals(name, field.getName());
			Assertions.assertEquals(type, field.getType());
		};
		Assertions.assertEquals(3, lf3.size());
		assertField.accept(lf3.get(0), "f2", Integer.class); // 先获取到子类字段
		assertField.accept(lf3.get(1), "f1", String.class);
		assertField.accept(lf3.get(2), "f2", int.class); // 被隐藏的字段也会被返回
	}

	@Test
	public void testFindPackageAnnotation()
	{
		var packageReflections = Reflections.class.getPackage();
		var clReflections = Reflections.class.getClassLoader();
		Reflections.findAnnotationsOf(packageReflections, clReflections, Indev.class, ReflectionDirection.ParentToChild);
		Reflections.findAnnotationsOf(packageReflections, clReflections, Indev.class, ReflectionDirection.ChildToParent);
	}
}
