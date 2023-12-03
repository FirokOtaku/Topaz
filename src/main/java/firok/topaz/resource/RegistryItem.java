package firok.topaz.resource;

/**
 * 可注册实体
 * @see Registry
 * @since 6.12.0
 * @author Firok
 * */
public interface RegistryItem<TypeKey>
{
	/**
	 * 获取注册表键
	 * @implNote 对于一个实例, 这个值应保持不变
	 * */
	TypeKey getRegistryKey();
}
