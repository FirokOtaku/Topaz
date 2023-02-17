package firok.topaz.platform;

/**
 * @since 3.9.0
 * @author Firok
 * */
public class PlatformTypes
{
	public static final PlatformType Unknown = new PlatformType.Unknown();

	public static final PlatformType Windows = new PlatformType.Windows();

	@Deprecated
	public static final PlatformType Linux = new PlatformType.Linux();

	@Deprecated
	public static final PlatformType MacOS = new PlatformType.MacOS();

	public static final PlatformType Current;
	static
	{
		var current = Unknown;
		for(var type : new PlatformType[]{ Windows, Linux, MacOS })
		{
			if(type.isCurrent())
			{
				current = type;
			}
		}
		Current = current;
	}
}
