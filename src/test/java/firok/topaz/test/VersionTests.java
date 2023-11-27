package firok.topaz.test;

import firok.topaz.general.Version;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VersionTests
{
	@Test
	void test()
	{
		var v100 = new Version(1, 0, 0);
		var v100str = Version.parse("1.0.0");
		Assertions.assertEquals(v100, v100str);
		Assertions.assertEquals("1.0.0", v100.toString());

		var v123 = new Version(1, 2, 3);
		var v123str = Version.parse("1.2.3");
		Assertions.assertEquals(v123, v123str);
		Assertions.assertEquals("1.2.3", v123.toString());

		var v345alpha = new Version(3, 4, 5, "alpha");
		var v345alphaStr = Version.parse("3.4.5-alpha");
		Assertions.assertEquals(v345alpha, v345alphaStr);
		Assertions.assertEquals("3.4.5-alpha", v345alpha.toString());

		var v1 = new Version(1, Integer.MIN_VALUE, Integer.MIN_VALUE);
		var v1str = Version.parse("1");
		Assertions.assertEquals(v1, v1str);
		Assertions.assertEquals("1", v1.toString());

		var v12build123 = new Version(1, 2, Integer.MIN_VALUE, "build123");
		var v12build123str = Version.parse("1.2-build123");
		Assertions.assertEquals(v12build123, v12build123str);
		Assertions.assertEquals("1.2-build123", v12build123.toString());

		Assertions.assertEquals(-1, v100.compareTo(v123));
		Assertions.assertEquals(0, v100.compareTo(v100));
		Assertions.assertEquals(1, v123.compareTo(v100));

		Assertions.assertEquals(1, v100.compareTo(v1));
		Assertions.assertEquals(-1, v100.compareTo(v12build123));
	}
}
