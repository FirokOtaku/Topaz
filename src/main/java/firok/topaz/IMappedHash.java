package firok.topaz;

/**
 * 计算映射后的哈希值
 *
 * @since 3.10.0
 * @author Firok
 */
public interface IMappedHash
{
	/**
	 * 获取映射器
	 */
	IHashMapper<? extends IMappedHash> getMapper();

	/**
	 * 获取计算后的哈希值
	 * */
	String getHashValue();

	/**
	 * 获取原始字符串
	 * */
	String getOriginalFull();
}
