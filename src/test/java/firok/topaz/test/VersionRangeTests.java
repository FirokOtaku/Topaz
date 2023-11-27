package firok.topaz.test;

import firok.topaz.general.Version;
import firok.topaz.general.VersionRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VersionRangeTests
{
	@Test
	void test()
	{
		var vr1 = VersionRange.parse("[1.2.3, 4.5.6)");
		System.out.println(vr1);

		Assertions.assertEquals(Version.parse("1.2.3"), vr1.versionFrom);
		Assertions.assertEquals(Version.parse("4.5.6"), vr1.versionTo);
		Assertions.assertTrue(vr1.includeFrom);
		Assertions.assertFalse(vr1.includeTo);
		Assertions.assertTrue(vr1.includes(Version.parse("1.2.3")));
		Assertions.assertTrue(vr1.includes(Version.parse("1.4.0")));
		Assertions.assertTrue(vr1.includes(Version.parse("2.1.2")));
		Assertions.assertTrue(vr1.includes(Version.parse("4.1.0")));
		Assertions.assertTrue(vr1.includes(Version.parse("4.5.5")));
		Assertions.assertFalse(vr1.includes(Version.parse("1.2.2")));
		Assertions.assertFalse(vr1.includes(Version.parse("1.0.0")));
		Assertions.assertFalse(vr1.includes(Version.parse("4.5.6")));
		Assertions.assertFalse(vr1.includes(Version.parse("5.0.0")));

		var vr2 = VersionRange.parse("[1.0.0,)");
		Assertions.assertEquals(new Version(1, 0, 0), vr2.versionFrom);
		Assertions.assertNull(vr2.versionTo);
		Assertions.assertTrue(vr2.includeFrom);
		Assertions.assertFalse(vr2.includeTo);
		Assertions.assertTrue(vr2.includes(Version.parse("1.0.0")));
		Assertions.assertTrue(vr2.includes(Version.parse("1.1.0")));
		Assertions.assertTrue(vr2.includes(Version.parse("2.1.0")));
		Assertions.assertTrue(vr2.includes(Version.parse("3.1.0")));
		Assertions.assertTrue(vr2.includes(Version.parse("111.1.0")));
		Assertions.assertFalse(vr2.includes(Version.parse("0.1.0")));
		Assertions.assertFalse(vr2.includes(Version.parse("0.0.0")));
		Assertions.assertFalse(vr2.includes(Version.parse("0.9.0")));

		var vr3 = VersionRange.parse("(,1.0.0]");
		Assertions.assertFalse(vr3.includes(new Version(2, 0, 0)));
		Assertions.assertFalse(vr3.includes(new Version(3, 0, 0)));
		Assertions.assertTrue(vr3.includes(new Version(1, 0, 0)));
		Assertions.assertTrue(vr3.includes(new Version(0, 9, 0)));
		Assertions.assertTrue(vr3.includes(new Version(0, 0, 0)));

		Assertions.assertEquals(VersionRange.parse("[0.1.0, )"), VersionRange.parse("[ 0.1.0  ,  )"));
	}
}
