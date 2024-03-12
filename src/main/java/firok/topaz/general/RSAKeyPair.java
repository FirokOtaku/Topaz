package firok.topaz.general;

import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;

/**
 * 储存 RSA 密钥对用的工具实体
 * @since 7.21.0
 * @see Encrypts#generateRSAKeyPair(int, SecureRandom)
 * */
public record RSAKeyPair(
		RSAPublicKey publicKey,
		RSAPrivateKey privateKey
) { }
