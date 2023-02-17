package firok.topaz.general;

/**
 * 计算储存容量相关工具方法
 *
 * @author Firok
 * @since 2.1.0
 */
public final class Capacities
{
	private Capacities() { }

	public static double kb(long bytes)
	{
		return bytes / 1024.0D;
	}

	public static double mb(long bytes)
	{
		return bytes / 1024.0D / 1024.0D;
	}

	public static double gb(long bytes)
	{
		return bytes / 1024.0D / 1024.0D / 1024.0D;
	}

	public static double tb(long bytes)
	{
		return bytes / 1024.0D / 1024.0D / 1024.0D / 1024.0D;
	}

	public static double pb(long bytes)
	{
		return bytes / 1024.0D / 1024.0D / 1024.0D / 1024.0D / 1024.0D;
	}
}
