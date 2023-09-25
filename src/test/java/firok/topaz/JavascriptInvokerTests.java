package firok.topaz;

import firok.topaz.indev.JavascriptInvoker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class JavascriptInvokerTests
{
	public static class Counter
	{
		int count = 0;
		public void add()
		{
			count++;
		}
	}
	static final String SCRIPT = """
            c1.add();
            c1.add();
            for(let step = 0; step < 10; step++) c2.add();
""";
//	@Test
	public void testInvoker()
	{
		var map = new HashMap<String, Object>();
		var c1 = new Counter();
		var c2 = new Counter();
		map.put("c1", c1);
		map.put("c2", c2);

		var invoker = new JavascriptInvoker(map);
		invoker.eval(SCRIPT);
		Assertions.assertEquals(2, c1.count);
		Assertions.assertEquals(10, c2.count);

		var c1_2 = invoker.<Counter>getValue("c1");
		var c2_2 = invoker.<Counter>getValue("c2");
		Assertions.assertEquals(2, c1_2.count);
		Assertions.assertEquals(10, c2_2.count);
	}
}
