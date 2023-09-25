package firok.topaz.reflection;

import firok.topaz.annotation.Indev;
import firok.topaz.annotation.SupportedMaximalVersion;
import firok.topaz.annotation.SupportedMinimalVersion;

import javax.lang.model.SourceVersion;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

	/**
	 * 获取一个类的某个方法, 不抛出异常
	 * @since 5.19.0
	 * */
	public static Method methodOf(Class<?> classTarget, String methodName, Class<?>... classParams)
	{
		try
		{
			return classTarget.getMethod(methodName, classParams);
		}
		catch (Exception any)
		{
			return null;
		}
	}

	/**
	 * 获取一个类的某个方法, 不抛出异常
	 * @since 5.19.0
	 * */
	public static Method declaredMethodOf(Class<?> classTarget, String methodName, Class<?>... classParams)
	{
		try
		{
			return classTarget.getDeclaredMethod(methodName, classParams);
		}
		catch (Exception any)
		{
			return null;
		}
	}

	/**
	 * 获取一个类的某个字段, 不抛出异常
	 * @since 5.19.0
	 * */
	public static Field fieldOf(Class<?> classTarget, String fieldName)
	{
		try
		{
			return classTarget.getField(fieldName);
		}
		catch (Exception any)
		{
			return null;
		}
	}

	/**
	 * 获取一个类的某个字段, 不抛出异常
	 * @since 5.19.0
	 * */
	public static Field declaredFieldOf(Class<?> classTarget, String fieldName)
	{
		try
		{
			return classTarget.getDeclaredField(fieldName);
		}
		catch (Exception any)
		{
			return null;
		}
	}

	/**
	 * 获取一个类的某个构造器, 不抛出异常
	 * @since 5.19.0
	 * */
	public static <T> Constructor<T> constructorOf(Class<T> classTarget, Class<?>... classParams)
	{
		try
		{
			return classTarget.getConstructor(classParams);
		}
		catch (Exception any)
		{
			return null;
		}
	}

	/**
	 * 获取一个类的某个构造器, 不抛出异常
	 * @since 5.19.0
	 * */
	public static <T> Constructor<T> declaredConstructorOf(Class<T> classTarget, Class<?>... classParams)
	{
		try
		{
			return classTarget.getDeclaredConstructor(classParams);
		}
		catch (Exception any)
		{
			return null;
		}
	}

	/**
	 * 构造一个新实例, 不跑出异常
	 * @since 5.19.0
	 * */
	public static <T> T newInstanceOf(Constructor<T> constructor, Object... params)
	{
		try
		{
			return constructor.newInstance(params);
		}
		catch (Exception any)
		{
			return null;
		}
	}

	/**
	 * 读取某个实体的注解值
	 * @since 5.20.0
	 * */
	@Indev
	@SuppressWarnings("unchecked")
	public static <TypeResult> TypeResult annotatedValueOf(Field fieldAny, Method annotationMethod)
	{
		try
		{
			var classAnnotation = annotationMethod.getDeclaringClass();
			if (!classAnnotation.isAnnotation())
				throw new IllegalArgumentException("指定方法不属于注解");
			var anno = fieldAny.getAnnotation((Class<? extends Annotation>) classAnnotation);
			return anno != null ? (TypeResult) annotationMethod.invoke(anno) : null;
		}
		catch (Exception any)
		{
			throw new RuntimeException(any);
		}
	}

	/**
	 * 读取某个实体的注解值
	 * @since 5.20.0
	 * */
	@Indev
	@SuppressWarnings("unchecked")
	public static <TypeResult> TypeResult annotatedValueOf(Method methodAny, Method annotationMethod)
	{
		try
		{
			var classAnnotation = annotationMethod.getDeclaringClass();
			if (!classAnnotation.isAnnotation())
				throw new IllegalArgumentException("指定方法不属于注解");
			var anno = methodAny.getAnnotation((Class<? extends Annotation>) classAnnotation);
			return anno != null ? (TypeResult) annotationMethod.invoke(anno) : null;
		}
		catch (Exception any)
		{
			throw new RuntimeException(any);
		}
	}

	/**
	 * 读取某个实体的注解值
	 * @since 5.20.0
	 * */
	@Indev
	@SuppressWarnings("unchecked")
	public static <TypeResult> TypeResult annotatedValueOf(Class<?> classAny, Method annotationMethod)
	{
		try
		{
			var classAnnotation = annotationMethod.getDeclaringClass();
			if (!classAnnotation.isAnnotation())
				throw new IllegalArgumentException("指定方法不属于注解");
			var anno = classAny.getAnnotation((Class<? extends Annotation>) classAnnotation);
			return anno != null ? (TypeResult) annotationMethod.invoke(anno) : null;
		}
		catch (Exception any)
		{
			throw new RuntimeException(any);
		}
	}

	/**
	 * @since 5.20.0
	 * */
	private static SupportStatus supportStatusOf(
			SupportedMinimalVersion minimal,
			SupportedMaximalVersion maximum
	)
	{
		var min =  minimal != null ? minimal.value().ordinal() : -1;
		var max = maximum != null ? maximum.value().ordinal() : -1;
		var cur = SourceVersion.latest().ordinal();

		if(min == -1 && max == -1) return SupportStatus.NotDeclared;
		else if(min != -1 && cur < min) return SupportStatus.Unsupported;
		else if(max != -1 && cur > max) return SupportStatus.Unsupported;
		else return SupportStatus.Supported;
	}

	/**
	 * 判断某个字段是否可用
	 * @since 5.20.0
	 * */
	public static SupportStatus supportStatusOf(Field fieldAny)
	{
		return supportStatusOf(fieldAny.getAnnotation(SupportedMinimalVersion.class), fieldAny.getAnnotation(SupportedMaximalVersion.class));
	}

	/**
	 * 判断某个方法是否可用
	 * @since 5.20.0
	 * */
	public static SupportStatus supportStatusOf(Method methodAny)
	{
		return supportStatusOf(methodAny.getAnnotation(SupportedMinimalVersion.class), methodAny.getAnnotation(SupportedMaximalVersion.class));
	}

	/**
	 * 判断某个类是否可用
	 * @since 5.20.0
	 * */
	public static SupportStatus supportStatusOf(Class<?> classAny)
	{
		return supportStatusOf(classAny.getAnnotation(SupportedMinimalVersion.class), classAny.getAnnotation(SupportedMaximalVersion.class));
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
		var ret = findAnnotationsOf(packageCur, cl, classAnnotation, direction);
		switch (direction)
		{
			case ParentToChild -> ret.add(classAny.getAnnotation(classAnnotation));
			case ChildToParent -> ret.add(0, classAny.getAnnotation(classAnnotation));
		}
		return ret;
	}
}
