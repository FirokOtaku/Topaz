package firok.topaz;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * 加密解密工具类
 * https://www.cnblogs.com/baiyp/p/7833610.html
 *
 * @author Firok
 * @since 2020/04/14 15:57
 */
public final class Encrypts
{
	private Encrypts() { }

	private static final byte[] DES_KEY = {55, 32, -15, 23, -24, 65, 47, -28};

	/**
	 * 使用默认密钥加密
	 * @deprecated 密钥固定 仅测试用途使用
	 * */
	@Deprecated(forRemoval = true)
	@SuppressWarnings("restriction")
	public static String encryptBasedDes(String data)
	{
		String encryptedData;
		try
		{
			// DES算法要求有一个可信任的随机数源
			SecureRandom sr = new SecureRandom();
			DESKeySpec deskey = new DESKeySpec(DES_KEY);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(deskey);
			// 加密对象
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key, sr);
			// 加密，并把字节数组编码成字符串
			encryptedData = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
		} catch (Exception e)
		{
			// log.error("加密错误，错误信息：", e);
			throw new RuntimeException("加密错误，错误信息：", e);
		}
		return encryptedData;
	}

	/**
	 * 使用默认密钥解密
	 * @deprecated 密钥固定 仅测试用途使用
	 * */
	@Deprecated(forRemoval = true)
	@SuppressWarnings("restriction")
	public static String decryptBasedDes(String cryptData)
	{
		String decryptedData;
		try
		{
			// DES算法要求有一个可信任的随机数源
			SecureRandom sr = new SecureRandom();
			DESKeySpec deskey = new DESKeySpec(DES_KEY);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(deskey);
			// 解密对象
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key, sr);
			// 把字符串进行解码，解码为为字节数组，并解密
			decryptedData = new String(cipher.doFinal(Base64.getDecoder().decode(cryptData)));
		} catch (Exception e)
		{
			throw new RuntimeException("解密错误，错误信息：", e);
		}
		return decryptedData;
	}

	/* ---- BASE64 加密 ---- */
	public static String encodeBase64_S2S(String raw)
	{
		return Base64.getEncoder().encodeToString(raw.getBytes(UTF_8));
	}

	public static byte[] encodeBase64_S2B(String raw)
	{
		return encodeBase64_B2B(raw.getBytes(UTF_8));
	}

	public static String encodeBase64_B2S(byte[] bytes)
	{
		return new String(encodeBase64_B2B(bytes));
	}

	public static byte[] encodeBase64_B2B(byte[] bytes)
	{
		return Base64.getEncoder().encode(bytes);
	}

	public static String decodeBase64_S2S(String raw)
	{
		return new String(Base64.getDecoder().decode(raw.getBytes(UTF_8)));
	}

	public static byte[] decodeBase64_S2B(String raw)
	{
		return Base64.getDecoder().decode(raw);
	}

	public static String decodeBase64_B2S(byte[] bytes)
	{
		return new String(Base64.getDecoder().decode(bytes));
	}

	public static byte[] decodeBase64_B2B(byte[] bytes)
	{
		return Base64.getDecoder().decode(bytes);
	}

	/* ---- RSA 加密 ---- */

	/**
	 * RSA公钥加密
	 *
	 * @param str       加密字符串
	 * @param publicKey 公钥
	 * @return 密文
	 * @throws Exception 加密过程中的异常信息
	 */
	public static String encodeRSA(String str, String publicKey) throws Exception
	{
		//base64编码的公钥
		byte[] decoded = decodeBase64_S2B(publicKey);
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(decoded));
		//RSA加密
		return encodeRSA(str, pubKey);
	}

	public static String encodeRSA(String str, RSAPublicKey publicKey) throws Exception
	{
		//RSA加密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		String outStr = encodeBase64_B2S(cipher.doFinal(str.getBytes(UTF_8)));
		return outStr;
	}

	public static String encodeRSA(String str, byte[] publicKey) throws Exception
	{
		return encodeRSA(str, new String(publicKey));
	}

	/**
	 * RSA私钥解密
	 *
	 * @param str        加密字符串
	 * @param privateKey 私钥
	 * @return 明文
	 * @throws Exception 解密过程中的异常信息
	 */
	public static String decodeRSA(String str, RSAPrivateKey privateKey) throws Exception
	{
		//64位解码加密后的字符串
		byte[] inputBytes = decodeBase64_S2B(str);
		//RSA解密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		String outStr = new String(cipher.doFinal(inputBytes));
		return outStr;
	}

	public static String decodeRSA(String str, String privateKey) throws Exception
	{
		//base64编码的私钥
		byte[] decoded = decodeBase64_S2B(privateKey);
		RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
				.generatePrivate(new PKCS8EncodedKeySpec(decoded));
		return decodeRSA(str, priKey);
	}

	public static String decodeRSA(String str, byte[] privateKey) throws Exception
	{
		return decodeRSA(str, new String(privateKey));
	}

	private static final RSAPublicKey keyPublicRSA;
	private static final RSAPrivateKey keyPrivateRSA;

	static
	{
		// 直接创建随机密钥
		try
		{
//			System.out.print("初始化系统密钥...");
			var kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048, new SecureRandom());
			KeyPair kp = kpg.generateKeyPair();
			keyPublicRSA = (RSAPublicKey) kp.getPublic();
			keyPrivateRSA = (RSAPrivateKey) kp.getPrivate();
//			System.out.print("完成\n");
		}
		catch (Exception e)
		{
			throw new RuntimeException("初始化加密模块失败", e);
		}
	}

	/**
	 * 用来触发类加载的空方法
	 */
	public static void init()
	{
//		System.out.println("init " + Encrypts.class);
	}

	/**
	 * 使用每次启动时生成的密钥加密
	 * */
	public static String encodeRSAtemp(String temp) throws Exception
	{
		return encodeRSA(temp, keyPublicRSA);
	}
	/**
	 * 使用每次启动时生成的密钥解密
	 * */
	public static String decodeRSAtemp(String temp) throws Exception
	{
		return decodeRSA(temp, keyPrivateRSA);
	}

	/* ---- HMAC 加密相关 ---- */

	/**
	 * MAC算法可选以下多种算法
	 * HmacMD5 / HmacSHA1 / HmacSHA256 / HmacSHA384 / HmacSHA512
	 * */
	private static final String HMAC_ALGORITHM = "HmacSHA256";
	/**
	 * 初始化一个 HMAC 密钥
	 * */
	@SneakyThrows
	public static Mac initHMACMac(String key)
	{
		var sk = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
		var mac = Mac.getInstance(HMAC_ALGORITHM);
		mac.init(sk);
		return mac;
	}
	/**
	 * @see #initHMACMac(String)
	 * @see #encodeHMAC(byte[], Mac)
	 * */
	@Deprecated
	public static byte[] encodeHMAC(byte[] content, String key)
	{
		var mac = initHMACMac(key);
		return encodeHMAC(content, mac);
	}
	/**
	 * @see #initHMACMac(String)
	 * */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	public static byte[] encodeHMAC(byte[] content, Mac mac)
	{
		synchronized (mac)
		{
			return mac.doFinal(content);
		}
	}
	/**
	 * @see #initHMACMac(String)
	 * @see #matchHMAC(byte[], byte[], String)
	 * */
	@Deprecated
	public static boolean matchHMAC(byte[] content, byte[] signature, String key)
	{
		var mac = initHMACMac(key);
		return matchHMAC(content, signature, mac);
	}
	/**
	 * @see #initHMACMac(String)
	 * */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	public static boolean matchHMAC(byte[] content, byte[] signature, Mac mac)
	{
		synchronized (mac)
		{
			return Arrays.equals(mac.doFinal(content), signature);
		}
	}

}
