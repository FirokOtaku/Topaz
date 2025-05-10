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
			new Version(7, 46, 0),
			"personal Java lib",
			List.of("Firok"),
			List.of("https://github.com/FirokOtaku/Topaz"),
			List.of("https://github.com/FirokOtaku/Topaz"),
			"MulanPSL-2.0"
	);

	private Topaz() { }
}
