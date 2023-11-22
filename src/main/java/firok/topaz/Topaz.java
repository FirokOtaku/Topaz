package firok.topaz;

import firok.topaz.general.ProgramMeta;
import firok.topaz.general.Version;

import java.util.List;

@SuppressWarnings("all")
public final class Topaz
{
	/**
	 * @since 6.8.0
	 * */
	public static final ProgramMeta META = new ProgramMeta(
			"firok.topaz",
			"Topaz",
			new Version(6, 8, 0),
			"personal Java lib",
			List.of("Firok"),
			List.of("https://github.com/FirokOtaku/Topaz"),
			List.of("https://github.com/FirokOtaku/Topaz"),
			"MulanPSL-2.0"
	);

	@Deprecated(forRemoval = true)
	public static final String NAME = META.name;
	/**
	 * @apiNote 3.17.0 - 替换为 Version 类型
	 * */
	@Deprecated(forRemoval = true)
	public static final Version VERSION = META.version;

	@Deprecated(forRemoval = true)
	public static final String AUTHOR = META.authors.get(0);

	@Deprecated(forRemoval = true)
	public static final String URL = META.linkRepos.get(0);

	private Topaz() { }
}
