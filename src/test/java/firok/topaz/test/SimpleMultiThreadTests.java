package firok.topaz.test;

import firok.topaz.function.MayRunnable;
import firok.topaz.general.CodeException;
import firok.topaz.thread.SimpleMultiThread;
import firok.topaz.thread.Threads;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @since 3.14.0
 * @author Firok
 * */
public class SimpleMultiThreadTests
{
	@Test
	void test()
	{
		// 直接创建会报错
		Assertions.assertThrowsExactly(CodeException.class, SimpleMultiThread::new);

		var results1 = new int[] {
				0,
				0,
				0,
		};
		var ths1 = new MayRunnable[] {
				() -> {
					results1[0] = 1;
				},
				() -> {
					results1[1] = 2;
				},
				() -> {
					results1[2] = 3;
				},
		};
		var pool1 = new SimpleMultiThread(ths1);
		pool1.start();
		pool1.waitEnd();
		System.out.println("pool1 done");
		Assertions.assertEquals(1, results1[0]);
		Assertions.assertEquals(2, results1[1]);
		Assertions.assertEquals(3, results1[2]);

		var results2 = new int[3];
		var ths2 = new MayRunnable[] {
				() -> {
					Threads.sleep(5000);
					results2[0] = 1;
					System.out.println("1 done");
				},
				() -> {
					Threads.sleep(300);
					results2[1] = 2;
					System.out.println("2 done");
				},
				() -> {
					Threads.sleep(1200);
					results2[2] = 3;
					System.out.println("3 done");
				},
		};
		var pool2 = new SimpleMultiThread(ths2);
		pool2.start();
		pool2.waitEnd();
		Assertions.assertEquals(1, results2[0]);
		Assertions.assertEquals(2, results2[1]);
		Assertions.assertEquals(3, results2[2]);
		System.out.println("pool2 done");

		var ths3 = new MayRunnable[] {
				() -> {
					Threads.sleep(200);
					throw new RuntimeException("exception 1");
				},
				() -> {
					Threads.sleep(1000);
					throw new RuntimeException("exception 2");
				},
				() -> {
					Threads.sleep(4000);
					throw new RuntimeException("exception 3");
				},
		};
		var pool3 = new SimpleMultiThread(false, ths3);
		pool3.start();
		pool3.waitEnd();
		for(int step = 0; step < 3; step++)
		{
			var exception = pool3.exceptionOf(step);
			System.out.println("错误: " + step + " = " + (exception != null));
			Assertions.assertNotNull(exception);
		}

		var results4 = new int[] {
				0,
				0,
				0,
		};
		var ths4 = new MayRunnable[] {
				() -> {
					Threads.sleep(2000);
					System.out.println("exception 1");
					if(true) throw new IllegalArgumentException("exception 1");
					results4[0] = 1;
				},
				() -> {
					Threads.sleep(100);
					results4[1] = 2;
					System.out.println("done 2");
				},
				() -> {
					Threads.sleep(2100);
					results4[2] = 3;
					System.out.println("done 3");
				},
		};
		var pool4 = new SimpleMultiThread(true, ths4);
		pool4.start();
		pool4.waitEnd();
		System.out.println("pool4 done");
		Assertions.assertArrayEquals(new int[] { 0, 2, 3 }, results4);
	}
}
