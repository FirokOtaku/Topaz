package firok.topaz.test;

import firok.topaz.general.Encrypts;
import firok.topaz.general.RSAKeyPair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class EncryptTests
{
	@SuppressWarnings("deprecation")
	@Test
	void testHMAC()
	{
		var listKeyTime = new ArrayList<Long>();
		var listContentTime = new ArrayList<Long>();
		IntStream.range(0, 100).parallel().forEach(stepKey-> {
			var key = "key-hmac-" + stepKey;
			var mac = Encrypts.initHMACMac(key);

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

	@Test
	void testHMACLength()
	{
		var mac = Encrypts.initHMACMac("pass-mac-000");

		for(int i = 0; i < 10; i++)
		{
			var sb = new StringBuilder("pswd-");
			for(int step = -5; step < i; step++)
			{
				sb.append(step);
			}
			var password = sb.toString();
			var bytes = password.getBytes(StandardCharsets.UTF_8);
			var signature = Encrypts.encodeHMAC(bytes, mac);

			System.out.println("加密前长度: " + bytes.length);
			System.out.println("加密后长度: " + signature.length);
		}

	}

	@Test
	void testHMACKeyLength()
	{
		var key1 = Encrypts.initHMACMac("12314123");
		Assertions.assertEquals(32, key1.getMacLength());

		var key2 = Encrypts.initHMACMac("123141absd2131");
		Assertions.assertEquals(32, key2.getMacLength());

		var key3 = Encrypts.initHMACMac("dwahnodi21323123121");
		Assertions.assertEquals(32, key3.getMacLength());
	}

	private void testSha(String passwordRaw,  RSAKeyPair kp) throws Exception
	{
		var kpPublic = kp.publicKey();
		var kpPrivate = kp.privateKey();

		var passwordShaEnc = Encrypts.encodeRSA(passwordRaw, kpPublic);
		var passwordShaDec = Encrypts.decodeRSA(passwordShaEnc, kpPrivate);

		System.out.println(
				"%s | %s | %s | %s".formatted(
						passwordRaw,
						passwordShaEnc,
						passwordShaDec,
						passwordRaw.equals(passwordShaDec)
				)
		);
		Assertions.assertEquals(passwordRaw, passwordShaDec);
	}

	@Test
	public void testSha() throws Exception
	{
		var kp = Encrypts.generateRSAKeyPair(2048, new SecureRandom());
		testSha("123456", kp);
		testSha("test-password", kp);
		testSha("000", kp);
		testSha("测试密码", kp);
		testSha("测试密码 123 ABC abc", kp);
		testSha("密码00123 AAA", kp);
	}
}
