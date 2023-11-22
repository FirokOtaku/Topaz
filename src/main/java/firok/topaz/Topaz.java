package firok.topaz;

import firok.topaz.general.Version;

@SuppressWarnings("all")
public final class Topaz
{
	@Deprecated(forRemoval = true)
	public static final String NAME = "Topaz";
	/**
	 * @apiNote 3.17.0 - 替换为 Version 类型
	 * */

	@Deprecated(forRemoval = true)
	public static final Version VERSION = new Version(6, 7, 0);

	@Deprecated(forRemoval = true)
	public static final String AUTHOR = "Firok";

	@Deprecated(forRemoval = true)
	public static final String URL = "https://github.com/FirokOtaku/Topaz";

	private Topaz() { }
}
