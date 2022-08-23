package firok.topaz;

import java.lang.reflect.Array;

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

		if(strs != null)
			for(int step = 0; step < ret.length; step++)
			{
				ret[step] = valueOf(classEnum, strs[step]);
			}

		return ret;
	}
}
