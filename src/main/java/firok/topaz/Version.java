package firok.topaz;

import java.util.*;

/**
 * 语义化版本号的简易实现
 *
 * https://semver.org/lang/zh-CN/
 * */
public class Version implements Comparable<Version>
{
	public final int major, minor, patch;
	public final String meta;
	private final int hash;
	/**
	 * 直接创建版本号
	 * */
	public Version(int major, int minor, int patch, String meta)
	{
		this.major = major;
		this.minor = minor;
		this.patch = patch;
		this.meta = meta;
		this.hash = Objects.hash(major, minor, patch, meta);
	}
	public Version(int major, int minor, int patch)
	{
		this(major, minor, patch, null);
	}

	/**
	 * 生成一个不带 meta 信息的版本号
	 * @since 3.18.0
	 * */
	public Version withoutMeta()
	{
		return new Version(major, minor, patch);
	}

	/**
	 * 把一个字符串转换为版本号
	 * @param raw 接受格式: "1", "1.2", "1.2.3", "1-xxx", "1.2-xxx", "1.2.3-xxx"
	 * @throws IllegalArgumentException 格式错误
	 * */
	public static Version parse(String raw) throws IllegalArgumentException
	{
		int major, minor = Integer.MIN_VALUE, patch = Integer.MIN_VALUE;
		try
		{
			var indexLine = raw.indexOf('-');
			String[] wordsNum;
			String meta = null;
			if(indexLine < 0)
			{
				wordsNum = raw.split("\\.");
			}
			else
			{
				wordsNum = raw.substring(0, indexLine).split("\\.");
				meta = raw.substring(indexLine + 1);
			}

			if(wordsNum.length < 1) throw new RuntimeException("Unknown format");
			major = Integer.parseInt(wordsNum[0]);
			if(wordsNum.length > 1)
				minor = Integer.parseInt(wordsNum[1]);
			if(wordsNum.length > 2)
				patch = Integer.parseInt(wordsNum[2]);

			return new Version(major, minor, patch, meta);
		}
		catch (Exception any)
		{
			throw new IllegalArgumentException("Failed to parse Version: " + raw, any);
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Version version = (Version) o;
		if(this.hash != version.hash) return false;
		return major == version.major && minor == version.minor && patch == version.patch && Objects.equals(meta, version.meta);
	}

	@Override
	public int hashCode()
	{
		return this.hash;
	}

	@Override
	public String toString()
	{
		var ret = new StringBuilder();
		ret.append(major);
		if(minor != Integer.MIN_VALUE)
		{
			ret.append(".").append(minor);

			if(patch != Integer.MIN_VALUE)
			{
				ret.append(".").append(patch);
			}
		}
		if(meta != null)
		{
			ret.append("-").append(meta);
		}
		return ret.toString();
	}

	@Override
	public int compareTo(Version o)
	{
		var cpMajor = Integer.compare(this.major, o.major);
		if(cpMajor != 0) return cpMajor;

		if(this.minor == Integer.MIN_VALUE && o.minor != Integer.MIN_VALUE)
		{
			return -1;
		}
		else if(this.minor != Integer.MIN_VALUE && o.minor == Integer.MIN_VALUE)
		{
			return 1;
		}
		else if(this.minor != Integer.MIN_VALUE) // o.minor != Integer.MIN_VALUE
		{
			var cpMinor = Integer.compare(this.minor, o.minor);
			if(cpMinor != 0)
				return cpMinor;
		}

		if(this.patch == Integer.MIN_VALUE && o.patch != Integer.MIN_VALUE)
		{
			return -1;
		}
		else if(this.patch != Integer.MIN_VALUE && o.patch == Integer.MIN_VALUE)
		{
			return 1;
		}
		else if(this.patch != Integer.MIN_VALUE) // o.patch != Integer.MIN_VALUE
		{
			var cpPatch = Integer.compare(this.patch, o.patch);
			if(cpPatch != 0)
				return cpPatch;
		}

		if(this.meta != null && o.meta == null)
		{
			return -1;
		}
		else if(this.meta == null && o.meta != null)
		{
			return 1;
		}
		else if(this.meta != null) // o.meta != null
		{
			return this.meta.compareTo(o.meta);
		}

		return 0;
	}
}
