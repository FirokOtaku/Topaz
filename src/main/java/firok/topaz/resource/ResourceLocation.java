package firok.topaz.resource;

import lombok.Getter;

import java.util.Objects;

/**
 * 一个资源坐标实现
 * @since 6.13.0
 * @author Firok
 * */
@SuppressWarnings("LombokGetterMayBeUsed")
public final class ResourceLocation
{
	/**
	 * 坐标丢失
	 * */
	public static final ResourceLocation Missing = new ResourceLocation();

	@Getter
	private String namespace;
	@Getter
	private String key;
	private ResourceLocation() { }
	public ResourceLocation(String namespace, String key)
	{
		this.namespace = Objects.requireNonNull(namespace);
		this.key = Objects.requireNonNull(key);
	}

	public static ResourceLocation resolveFull(String path)
	{
		var ret = new ResourceLocation();
		var index = path.indexOf(':');
		if(index > 0)
		{
			ret.namespace = path.substring(0, index);
			ret.key = path.substring(index + 1);
		}
		else
		{
			ret.namespace = null;
			ret.key = path;
		}
		return ret;
	}

	public static ResourceLocation resolveDefault(String path, String namespaceDefault)
	{
		var ret = resolveFull(path);
		if(ret.namespace == null)
			ret.namespace = namespaceDefault;
		return ret;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(namespace, key);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ResourceLocation that = (ResourceLocation) o;
		return Objects.equals(namespace, that.namespace) && Objects.equals(key, that.key);
	}
}
