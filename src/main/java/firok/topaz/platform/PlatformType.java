package firok.topaz.platform;

import static firok.topaz.platform.PlatformType.*;

/**
 * 操作系统类型
 *
 * @see PlatformTypes
 * @since 3.9.0
 * @author Firok
 */
public abstract sealed class PlatformType permits Unknown, Windows, Linux, MacOS
{
	final String OS_NAME = System.getProperty("os.name").toLowerCase();

	private final boolean isCurrent;
	protected PlatformType()
	{
		this.isCurrent = equalsCurrent();
	}

	/**
	 * 判断是否是当前系统
	 */
	public final boolean isCurrent()
	{
		return isCurrent;
	}

	/**
	 * 子类需要实现此方法 返回当前是否是此操作系统
	 */
	protected abstract boolean equalsCurrent();

	protected abstract String getName();

	/**
	 * 未知操作系统
	 */
	static final class Unknown extends PlatformType
	{
		@Override
		protected boolean equalsCurrent()
		{
			return false;
		}

		@Override
		protected String getName()
		{
			return "Unknown";
		}
	}

	/**
	 * Windows 操作系统
	 */
	static final class Windows extends PlatformType
	{
		@Override
		protected boolean equalsCurrent()
		{
			return OS_NAME.contains("windows");
		}

		@Override
		protected String getName()
		{
			return "Windows";
		}
	}

	static final class Linux extends PlatformType
	{
		@Override
		protected boolean equalsCurrent()
		{
			return OS_NAME.contains("linux"); // OS_NAME.contains("nix") || OS_NAME.contains("nux");
		}

		@Override
		protected String getName()
		{
			return "Linux";
		}
	}

	static final class MacOS extends PlatformType
	{
		@Override
		protected boolean equalsCurrent()
		{
			// gossip 没机会
			return false;
		}

		@Override
		protected String getName()
		{
			return "MacOS";
		}
	}
}
