package firok.topaz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegexPipelineTests
{
	@Test
	public void testBasic()
	{
		final var raw = """
				line1
				line2
				line3
				line-end""";

		var pp = new RegexPipeline();
		Assertions.assertEquals("""
                line1
                line-test
                line3
                line-end""", pp.replaceAll(raw, "line2", "line-test"));
		Assertions.assertEquals("""
				line-test1
				line2
				line3
				line-end""", pp.replaceFirst(raw, "line", "line-test"));

		var str = "a.b.c.D";
		var index = str.lastIndexOf('.');

		System.out.println(str.substring(index + 1));
	}
}
