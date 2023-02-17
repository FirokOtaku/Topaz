package firok.topaz;

import firok.topaz.hash.IHashMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HashMapperTests
{
	@Test
	void test()
	{
		var mapperNoHash = IHashMapper.of("no-hash");
		var mapperUUID4 = IHashMapper.of("uuid-quadruple");
		var mapperUUID6 = IHashMapper.of("uuid-sextuple");

		Assertions.assertNotNull(mapperNoHash);
		Assertions.assertNotNull(mapperUUID4);
		Assertions.assertNotNull(mapperUUID6);

		var uuid = "86bcd80a-0d5c-43da-8938-15f9be563109";

		var hashNo = mapperNoHash.hashOf(uuid);
		Assertions.assertEquals(uuid, hashNo.getHashValue());
		Assertions.assertEquals(uuid, hashNo.getOriginalFull());

		var hashUUID4 = mapperUUID4.hashOf(uuid);
		Assertions.assertEquals("86/bc/d8/0a", hashUUID4.getHashValue());
		Assertions.assertEquals(uuid, hashUUID4.getOriginalFull());

		var hashUUID6 = mapperUUID6.hashOf(uuid);
		Assertions.assertEquals("86/bc/d8/0a/0d/5c", hashUUID6.getHashValue());
		Assertions.assertEquals(uuid, hashUUID6.getOriginalFull());
	}
}
