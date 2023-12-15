package firok.topaz.reflection;

import firok.topaz.TopazExceptions;
import firok.topaz.annotation.Indev;
import firok.topaz.annotation.SupportedMaximalVersion;
import firok.topaz.annotation.SupportedMinimalVersion;
import firok.topaz.platform.Processes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.SourceVersion;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;
import java.util.Collections;
import java.util.function.Predicate;

import static firok.topaz.general.Collections.isNotEmpty;

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
	@Nullable
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
	@Nullable
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
	 * 根据名称获取类的某个方法, 不抛出异常.
	 * 请注意, 由于仅提供方法名而没有更多参数信息, 这个工具方法无法区分同名的重载方法.
	 * @since 6.8.0
	 * @see #findNamedMethodsFrom(Class, String)
	 * */
	@Deprecated(since = "6.9.0")
	@Nullable
	public static Method namedMethodOf(Class<?> classTarget, String methodName)
	{
		try
		{
			for(var method : classTarget.getMethods())
			{
				if(Objects.equals(methodName, method.getName()))
					return method;
			}
			return null;
		}
		catch (Exception any)
		{
			return null;
		}
	}

	/**
	 * 根据名称获取类的某个方法, 不抛出异常.
	 * 请注意, 由于仅提供方法名而没有更多参数信息, 这个工具方法无法区分同名的重载方法.
	 * @since 6.8.0
	 * @see #findNamedMethodsFrom(Class, String)
	 * */
	@Deprecated(since = "6.9.0")
	@Nullable
	public static Method namedDeclaredMethodOf(Class<?> classTarget, String methodName)
	{
		try
		{
			for(var method : classTarget.getDeclaredMethods())
			{
				if(Objects.equals(methodName, method.getName()))
					return method;
			}
			return null;
		}
		catch (Exception any)
		{
			return null;
		}
	}

	private static void findNamedMethodFromInternal(Class<?> classTarget, String methodName, List<Method> list, boolean enableObject)
	{
		if(classTarget == null) return;
		if(classTarget == Object.class && !enableObject) return;

		for(var method : classTarget.getDeclaredMethods())
		{
			if(Objects.equals(methodName, method.getName()))
				list.add(method);
		}

		// 先遍历接口
		for(var interfaceCur : classTarget.getInterfaces())
		{
			findNamedMethodFromInternal(interfaceCur, methodName, list, enableObject);
		}
		// 再遍历父类
		findNamedMethodFromInternal(classTarget.getSuperclass(), methodName, list, enableObject);
	}

	/**
	 * 在给定的类上搜索指定名称的方法, 这会遍历类和其所有父类和接口类.
	 * @return 这个方法会返回所有找到的方法的列表, 但是对顺序不做保证.
	 *         如果没找到, 这个方法会返回空列表; 如果出现错误, 这个方法会返回 null
	 * @since 6.9.0
	 * */
	@Nullable
	public static List<Method> findNamedMethodsFrom(@NotNull Class<?> classTarget, @NotNull String methodName)
	{
		try
		{
			var ret = new ArrayList<Method>(8);

			findNamedMethodFromInternal(classTarget, methodName, ret, false);
			findNamedMethodFromInternal(Object.class, methodName, ret, true);

			return ret;
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
	@Nullable
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
	@Nullable
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

	private static void findNamedFieldFromInternal(Class<?> classTarget, String fieldName, List<Field> list, boolean enableObject)
	{
		if(classTarget == null) return;
		if(classTarget == Object.class && !enableObject) return;

		for(var field : classTarget.getDeclaredFields())
		{
			if(Objects.equals(fieldName, field.getName()))
				list.add(field);
		}
		// 不用遍历接口类, 接口类不包含字段
		// 再遍历父类
		findNamedFieldFromInternal(classTarget.getSuperclass(), fieldName, list, enableObject);
	}

	/**
	 * 在给定的类上搜索指定名称的字段, 这会遍历类和其所有父类和接口类.
	 * @return 这个方法会返回所有找到的字段的列表, 但是对顺序不做保证.
	 *         如果没找到, 这个方法会返回空列表; 如果出现错误, 这个方法会返回 null
	 * @since 6.9.0
	 * */
	public static List<Field> findNamedFieldsFrom(@NotNull Class<?> classTarget, @NotNull String fieldName)
	{
		var ret = new ArrayList<Field>(8);

		findNamedFieldFromInternal(classTarget, fieldName, ret, false);
		findNamedFieldFromInternal(Object.class, fieldName, ret, true);

		return ret;
	}

	/**
	 * 获取一个类的某个构造器, 不抛出异常
	 * @since 5.19.0
	 * */
	@Nullable
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
	@Nullable
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
	@Nullable
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
	@Nullable
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
	@Nullable
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
	@Nullable
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

	/**
	 * 获取调用者类
	 * @since 6.8.0
	 * */
	@Indev(experimental = true)
	public static Class<?> getCallerClass() throws ClassNotFoundException
	{
		var traces = Thread.currentThread().getStackTrace();
		var trace = traces[3];
		return Class.forName(trace.getClassName());
	}

	/**
	 * 获取调用者方法
	 * @since 6.8.0
	 * */
	@Indev(experimental = true)
	public static Method getCallerMethod() throws ClassNotFoundException, NoSuchMethodException
	{
		var traces = Thread.currentThread().getStackTrace();
		var trace = traces[3];
		var callerClass = Class.forName(trace.getClassName());
		return Reflections.namedMethodOf(callerClass, trace.getMethodName());
	}

	/**
	 * 一个导出之后的可执行类的信息
	 * @since 7.2.0
	 * */
	public record FileInfoOfExecutableClass(File folderBase, File folderClass, File fileClass, Class<?> classAny)
	{
		/**
		 * 创建文件夹和文件, 如果失败则抛出异常
		 * */
		public void create()
		{
			try
			{
				if(!folderClass.exists() && !folderClass.mkdirs())
					throw new RuntimeException("创建目录失败");
				if(!fileClass.exists() && !fileClass.createNewFile())
					throw new RuntimeException("创建文件失败");
			}
			catch (Exception any)
			{
				TopazExceptions.FileSystemCreationFailed.occur(any);
			}
		}

		/**
		 * 生成执行这个字节码文件所需的命令行指令
		 * */
		public String getExecutionCommand(String[] jvmArgs, String[] programArgs)
		{
			var jvmExe = Processes.getCurrentJvmExecutable();
			TopazExceptions.JvmNotFound.maybe(jvmExe == null);
			assert jvmExe != null;

			var classNameWithoutClassSuffix = classAny.getCanonicalName();
			var list = new ArrayList<String>();
			list.add('"' + jvmExe.getAbsolutePath() + '"');
			if(isNotEmpty(jvmArgs))
				list.addAll(Arrays.asList(jvmArgs));

			list.add("-cp");
			list.add('"' + folderBase.getAbsolutePath() + '"');
			list.add(classNameWithoutClassSuffix);
			if(isNotEmpty(programArgs))
				list.addAll(Arrays.asList(programArgs));

			return String.join(" ", list);
		}
	}
	/**
	 * 根据一个类的限定名, 计算出这个类如果相对于一个目录, 应该在什么位置
	 * @since 7.2.0
	 * */
	public static FileInfoOfExecutableClass getFileOfExecutableClass(File folderBase, Class<?> classAny)
	{
		var packageNames = classAny.getPackage().getName().split("\\.");
		var className = classAny.getSimpleName();
		var folderClass = Path.of(folderBase.getAbsolutePath(), packageNames).toFile();
		var fileClass = new File(folderClass, className + ".class");
		return new FileInfoOfExecutableClass(folderBase, folderClass, fileClass, classAny);
	}

	/**
	 * 将一个可执行类的字节码文件导出到文件系统
	 * @param classAny 可执行类. 这个类需要有一个合法的 main 方法
	 * @param folderBase 目标导出目录
	 * @throws firok.topaz.general.CodeException 失败则抛出
	 * @since 7.2.0
	 * */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static FileInfoOfExecutableClass exportExecutableClassFile(File folderBase, Class<?> classAny)
	{
		// 检查是不是合法的主类
		// todo low 支持新版本的无参 main 方法和无类 main 方法
		try { classAny.getMethod("main", String[].class); }
		catch (NoSuchMethodException any) { TopazExceptions.NoMainMethod.occur(any); }

		var info = getFileOfExecutableClass(folderBase, classAny);
		var className = classAny.getSimpleName();
		var resourceClass = classAny.getResource(className + ".class");
		TopazExceptions.NoClassResource.maybe(resourceClass == null);
		assert resourceClass != null;

		if(info.fileClass.exists())
			info.fileClass.delete();
		info.create();

		try(var ifs = resourceClass.openStream(); var ofs = new FileOutputStream(info.fileClass))
		{
			ifs.transferTo(ofs);
		}
		catch (Exception any)
		{
			TopazExceptions.FileSystemCreationFailed.occur(any);
		}

		return info;
	}
}
