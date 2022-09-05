package firok.topaz.hash;

import firok.topaz.IHashMapper;

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
