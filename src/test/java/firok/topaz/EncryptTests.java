package firok.topaz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class EncryptTests
{
	@Test
	void testHMAC()
	{
		var listKeyTime = new ArrayList<Long>();
		var listContentTime = new ArrayList<Long>();
		IntStream.range(0, 100).parallel().forEach(stepKey-> {
			var key = "key-hmac-" + stepKey;
			var mac = Encrypts.initMac(key);

			var startkey = System.currentTimeMillis();
			IntStream.range(0, 100).parallel().forEach(stepContent -> {
				try
				{
					var startContent = System.currentTimeMillis();

					var content = "content-hmac-" + stepContent;
					var bytes = content.getBytes(StandardCharsets.UTF_8);
					var signature = Encrypts.encodeHMAC(bytes, mac);
					var match = Encrypts.matchHMAC(bytes, signature, key);

					var stopContent = System.currentTimeMillis();
					synchronized (listContentTime)
					{
						listContentTime.add(stopContent - startContent);
					}

					Assertions.assertTrue(match);
				}
				catch (Exception any)
				{
					Assertions.fail(any);
				}
			});
			var stopKey = System.currentTimeMillis();

			synchronized (listKeyTime)
			{
				listKeyTime.add(stopKey - startkey);
			}
		});

		long maxTimeKey = Long.MIN_VALUE, minTimeKey = Long.MAX_VALUE;
		long totalTimeKey = 0;
		for (var timeKey : listKeyTime)
		{
			maxTimeKey = Math.max(maxTimeKey, timeKey);
			minTimeKey = Math.min(minTimeKey, timeKey);
			totalTimeKey += timeKey;
		}

		long maxTimeContent = Long.MIN_VALUE, minTimeContent = Long.MAX_VALUE;
		long totalTimeContent = 0;
		for (var timeContent : listContentTime)
		{
			maxTimeContent = Math.max(maxTimeContent, timeContent);
			minTimeContent = Math.min(minTimeContent, timeContent);
			totalTimeContent += timeContent;
		}

		System.out.println("max-time-key: " + maxTimeKey);
		System.out.println("min-time-key: " + minTimeKey);
		System.out.println("total-time-key: " + totalTimeKey);
		System.out.println("aver-time-key: " + totalTimeKey / listKeyTime.size());
		System.out.println("max-time-content: " + maxTimeContent);
		System.out.println("min-time-content: " + minTimeContent);
		System.out.println("total-time-content: " + totalTimeContent);
		System.out.println("aver-time-content: " + totalTimeContent / listContentTime.size());
	}
}