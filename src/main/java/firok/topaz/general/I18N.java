package firok.topaz.general;

import firok.topaz.TopazExceptions;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.*;

/**
 * i18n 工具.
 * 在资源目录下提供类似 "bundle-zh-CN.lang" 格式的语言文件即可使用此类进行本地化.
 *
 * @author Firok
 * @since 1.0.0
 * @version 7.0.0
 */
public final class I18N
{
	private final String bundle;
	private final Locale localeDefault;
	private final Map<String, Map<String, String>> mapCache;
	private final Class<?> classRelated;
	public I18N(String bundle, Class<?> classRelated)
	{
		this(bundle, Locale.getDefault(), classRelated);
	}
	/**
	 * @param bundle 语言文件的统一名称前缀
	 * @param localeDefault 默认语言
	 * @param classRelated 指定一个类, 相关资源将会从这个类的相对路径加载
	 * */
	public I18N(String bundle, Locale localeDefault, Class<?> classRelated)
	{
		this.bundle = bundle;
		this.localeDefault = localeDefault;
		this.mapCache = new Hashtable<>();
		this.classRelated = classRelated;
	}

    /// 获取语言文件名
    /// @since 8.0.0
    public String getRelatedFilename(Locale locale)
    {
        return bundle + '-' + locale.toLanguageTag() + ".lang";
    }

    /// 清空对应语言的缓存
    /// @since 8.0.0
    public void clearCache(Locale locale)
    {
        mapCache.remove(getRelatedFilename(locale));
    }

    /// 清空所有语言的缓存
    /// @since 8.0.0
    public void clearAllCache()
    {
        mapCache.clear();
    }

	@Nullable
	private String localizeInternal(Locale[] locales, String key, Object... params)
	{
		for(var locale : locales)
		{
			var filename = getRelatedFilename(locale);

			var properties = mapCache.computeIfAbsent(filename, fn -> {
				try(var from = classRelated.getResourceAsStream(filename);
					var in = new Scanner(from))
				{
					var map = new HashMap<String, String>();
					while(in.hasNextLine())
					{
						var line = in.nextLine().trim();
						if(line.isEmpty()) continue;

						var index = line.indexOf('=');
						var k = line.substring(0, index);
						var v = line.substring(index + 1);
						map.put(k, v);
					}
					return map;
				}
				catch (Exception any)
				{
					// 如果加载的不是默认语言, 那没事
					// 如果默认语言都加载失败了, 那爆炸
					if(Objects.equals(locale, localeDefault))
					{
						System.out.println("filename: " + filename);
						return TopazExceptions.I18NDefaultResourceNotFound.occur();
					}
					else
					{
						return null;
					}
				}
			});
			if(properties != null)
			{
				var pattern = properties.get(key);
				if(pattern != null) return pattern.formatted(params);
			}
		}
		return null;
	}

	public String localize(String key, Object... params)
	{
		return localize(localeDefault, key, params);
	}

	public String localize(Locale locale, String key, Object... params)
	{
		if(Objects.equals(locale, localeDefault))
			return localizeInternal(new Locale[] { locale }, key, params);
		else
			return localizeInternal(new Locale[] { locale, localeDefault }, key, params);
	}
}
