package firok.topaz.hash;

/**
 * 将字符串直接作为哈希值
 * <p>
 * 例:
 * 8d6b5f5a-dc3b-4513-b504-e4de2d594751
 * 提取后:
 * 8d6b5f5a-dc3b-4513-b504-e4de2d594751
 *
 * @since 3.10.0
 * @author Firok
 */
public record NoHash(
		NoHashMapper mapper,
		String full
) implements IMappedHash
{

	@Override
	public IHashMapper<? extends IMappedHash> getMapper()
	{
		return mapper;
	}

	@Override
	public String getHashValue()
	{
		return full;
	}

	@Override
	public String getOriginalFull()
	{
		return full;
	}
}
