package firok.topaz.reflection;

import firok.topaz.annotation.Indev;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Collections;
import java.util.function.Predicate;

/**
 * 反射相关代码
 *
 * @since 3.28.0
 * @author Firok
 * */
public final class Reflections
{
	/**
	 * 获取某个类所有字段, 包含父类字段
	 * */
	public static List<Field> listAllFieldsOf(Class<?> objClass)
	{
		var listField = new ArrayList<Field>();
		var classNow = objClass;
		while(classNow != Object.class)
		{
			Collections.addAll(listField, classNow.getDeclaredFields());
			classNow = classNow.getSuperclass();
		}
		return listField;
	}

	/**
	 * 获取某个类符合条件的所有字段, 包含父类字段
	 * @param predicate 字段过滤条件. 只有推断为真的字段才会被添加
	 * */
	public static List<Field> listAllFieldsOf(Class<?> objClass, Predicate<Field> predicate)
	{
		var listField = new ArrayList<Field>();
		var classNow = objClass;
		while(classNow != Object.class)
		{
			for(var field : classNow.getDeclaredFields())
			{
				if(predicate.test(field)) listField.add(field);
			}
			classNow = classNow.getSuperclass();
		}
		return listField;
	}

	@Indev
	public static List<Package> getPackages(Package certainPackage, ClassLoader cl, ReflectionDirection direction)
	{
		var packageName = certainPackage.getName();
		var packageParts = packageName.split("\\.");
		var pp = packageParts.length;
		var ret = new ArrayList<Package>(pp);

		var cur = direction == ReflectionDirection.ParentToChild ? 1 : pp;
		do
		{
			var packageNameCur = String.join(".", firok.topaz.general.Collections.cut(cur, packageParts));
			var packageCur = cl.getDefinedPackage(packageNameCur);

			ret.add(packageCur);

			if(direction == ReflectionDirection.ParentToChild) cur++;
			else cur--;
		}
		while(direction == ReflectionDirection.ParentToChild ? cur <= pp : cur > 0);

		return ret;
	}

	@Indev
	public static <TypeAnnotation extends Annotation>
	List<TypeAnnotation> findAnnotationsOf(Package certainPackage, ClassLoader cl, Class<TypeAnnotation> classAnnotation, ReflectionDirection direction)
	{
		var packages = getPackages(certainPackage, cl, direction);
		var ret = new ArrayList<TypeAnnotation>(packages.size());
		for(var packageCur : packages)
		{
			var annotation = packageCur.getAnnotation(classAnnotation);
			ret.add(annotation);
		}
		return ret;
	}

	@Indev
	public static <TypeAnnotation extends Annotation>
	List<TypeAnnotation> findAnnotationsOfPackage(String certainPackage, ClassLoader cl, Class<TypeAnnotation> classAnnotation, ReflectionDirection direction)
	{
		var packageCur = cl.getDefinedPackage(certainPackage);
		return findAnnotationsOf(packageCur, cl, classAnnotation, direction);
	}

	@Indev
	public static <TypeAnnotation extends Annotation>
	List<TypeAnnotation> findAnnotationsOfPackage(Class<?> classAny, Class<TypeAnnotation> classAnnotation, ReflectionDirection direction)
	{
		var packageCur = classAny.getPackage();
		var cl = classAny.getClassLoader();
		return findAnnotationsOf(packageCur, cl, classAnnotation, direction);
	}
}
