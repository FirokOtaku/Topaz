package firok.topaz.hash;

/**
 * @since 3.10.0
 * @author Firok
 * */
public record UUIDSextupleHash(
		UUIDSextupleMapper mapper,
		String p1,
		String p2,
		String p3,
		String p4,
		String p5,
		String p6,
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
		return p1 + '/' +
				p2 + '/' +
				p3 + '/' +
				p4 + '/' +
				p5 + '/' +
				p6;
	}

	@Override
	public String getOriginalFull()
	{
		return full;
	}
}
