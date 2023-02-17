package firok.topaz.hash;

import java.util.Objects;

/**
 * 简单计算某个字符串中的哈希值,
 * 用于文件储存路径散列等
 *
 * @since 3.10.0
 * @author Firok
 */
public interface IHashMapper<TypeHash extends IMappedHash>
{
	/**
	 * 获取映射器名称
	 */
	String getMapperName();

	/**
	 * 将某个字符串映射为哈希值
	 */
	TypeHash hashOf(String str);

	/**
	 * 获取一个映射器 <br>
	 *
	 * 默认支持:
	 * <ul>
	 *     <li>"no-hash"</li>
	 *     <li>"uuid-quadruple"</li>
	 *     <li>"uuid-sextuple"</li>
	 * </ul>
	 *
	 * @see firok.topaz.hash.NoHashMapper
	 * @see firok.topaz.hash.UUIDQuadrupleMapper
	 * @see firok.topaz.hash.UUIDSextupleMapper
	 * */
	static IHashMapper<?> of(String name)
	{
		var loader = java.util.ServiceLoader.load(IHashMapper.class);
		for (IHashMapper<?> mapper : loader)
		{
			if(Objects.equals(mapper.getMapperName(), name))
				return mapper;
		}
		return null;
	}
}
