package firok.topaz.general;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

import static firok.topaz.general.Collections.*;

/**
 * @since 3.7.0
 * @author Firok
 * */
@SuppressWarnings("GrazieInspection")
public class Enums
{
	public static <TypeEnum extends Enum<TypeEnum>> TypeEnum valueOf(Class<TypeEnum> classEnum, String str)
	{
		try
		{
			return Enum.valueOf(classEnum, str);
		}
		catch (Exception any)
		{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <TypeEnum extends Enum<TypeEnum>> TypeEnum[] valuesOf(Class<TypeEnum> classEnum, String... strs)
	{
		var count = strs != null ? strs.length : 0;
		var ret = (TypeEnum[]) Array.newInstance(classEnum, count);

		if(count > 0)
			for(int step = 0; step < ret.length; step++)
				ret[step] = valueOf(classEnum, strs[step]);

		return ret;
	}

	/**
	 * @since 7.16.0
	 * */
	@SuppressWarnings("unchecked")
	public static <TypeEnum extends Enum<TypeEnum>> TypeEnum[] valuesOf(Class<TypeEnum> classEnum, Iterable<String> strs)
	{
		var list = collect(strs);
		var count = sizeOf(list);
		var ret = (TypeEnum[]) Array.newInstance(classEnum, count);

		if(count > 0)
			for(int step = 0; step < ret.length; step++)
				ret[step] = valueOf(classEnum, list.get(step));

		return ret;
	}

	/**
	 * 把若干字符串转换成相应的 {@code EnumSet}
	 * */
	public static <TypeEnum extends Enum<TypeEnum>> EnumSet<TypeEnum> setOf(Class<TypeEnum> classEnum, String... strs)
	{
		var ret = EnumSet.noneOf(classEnum);
		if(isNotEmpty(strs)) for(var str : strs)
		{
			var tag = valueOf(classEnum, str);
			if(tag == null) continue;
			ret.add(tag);
		}
		return ret;
	}

	/**
	 * @since 7.16.0
	 * */
	public static <TypeEnum extends Enum<TypeEnum>> EnumSet<TypeEnum> setOf(Class<TypeEnum> classEnum, Iterable<String> strs)
	{
		var ret = EnumSet.noneOf(classEnum);
		if(isNotEmpty(strs)) for(var str : strs)
		{
			var tag = valueOf(classEnum, str);
			if(tag == null) continue;
			ret.add(tag);
		}
		return ret;
	}

	/**
	 * 把若干枚举转换成字符串列表
	 * */
	public static <TypeEnum extends Enum<TypeEnum>> Collection<String> namesOf(Collection<TypeEnum> enumValues)
	{
		var size = enumValues != null ? enumValues.size() : 0;
		var ret = new ArrayList<String>(size);
		if(size > 0) for(var enumValue : enumValues)
			if(enumValue != null)
				ret.add(enumValue.name());
		return ret;
	}

	/**
	 * 把若干枚举转换成字符串列表
	 * */
	public static <TypeEnum extends Enum<TypeEnum>> Collection<String> namesOf(TypeEnum... enumValues)
	{
		var size = enumValues != null ? enumValues.length : 0;
		var ret = new ArrayList<String>(size);
		if(size > 0) for(var enumValue : enumValues)
			if(enumValue != null)
				ret.add(enumValue.name());
		return ret;
	}
}
