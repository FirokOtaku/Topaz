package firok.topaz.hash;

/**
 * @since 3.10.0
 * @author Firok
 * */
public class NoHashMapper implements IHashMapper<NoHash>
{
	@Override
	public String getMapperName()
	{
		return "no-hash";
	}

	@Override
	public NoHash hashOf(String str)
	{
		return new NoHash(this, str);
	}
}
