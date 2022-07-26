package firok.topaz;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MathHelperTest
{
	@Test
	void test()
	{
		testOne(65535);
		testOne(9999);
		testOne(4096);
		testOne(1024);
	}
	void testOne(int len)
	{
		var mh = new MathHelper(len);

		double accu = 0;
		final int maxStep = 65535 * 4;
		var factor = Math.PI * 2 / maxStep;
		for(int i = 0; i < maxStep; i++)
		{
			double current = i * factor;
			double resultHelper = mh.cos(current);
			double resultMath = Math.cos(current);
			accu += Math.abs(resultHelper - resultMath);
		}
		System.out.println("total: " + len);
		System.out.println("step: " + maxStep);
		System.out.println("accu: " + accu);
		System.out.println("aver: " + accu / maxStep);
		System.out.println("====");
		assertTrue(accu / maxStep < 0.01);
	}
}
