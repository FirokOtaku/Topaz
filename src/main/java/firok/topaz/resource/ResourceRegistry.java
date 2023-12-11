package firok.topaz.resource;

/**
 * 直接用 {@link ResourceLocation} 作为键的注册表, 提供一些工具方法
 * @since 6.17.0
 * @author Firok
 * */
public class ResourceRegistry<TypeValue extends RegistryItem<ResourceLocation>>
        extends Registry<ResourceLocation, TypeValue>
{
    public TypeValue resolve(String namespace, String key)
    {
        var lr = new ResourceLocation(namespace, key);
        return get(lr);
    }

    public TypeValue resolveDefault(String path, String defaultNamespace)
    {
        var lr = ResourceLocation.resolveDefault(path, defaultNamespace);
        return get(lr);
    }

    public TypeValue resolveFull(String full)
    {
        var lr = ResourceLocation.resolveFull(full);
        return get(lr);
    }
}
