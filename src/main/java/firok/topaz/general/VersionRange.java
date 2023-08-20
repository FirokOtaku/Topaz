package firok.topaz.general;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 语义化版本号范围
 * @since 5.12.0
 * @author Firok
 * */
public final class VersionRange
{
	public final Version versionFrom, versionTo;
	public final boolean includeFrom, includeTo;
	public VersionRange(Version versionFrom, Version versionTo, boolean includeFrom, boolean includeTo)
	{
		if(includeFrom && versionFrom == null) throw new IllegalArgumentException("缺失范围下限");
		if(includeTo && versionTo == null) throw new IllegalArgumentException("缺失范围上限");
		if(versionFrom != null && versionTo != null && versionFrom.compareTo(versionTo) >= 0) throw new IllegalArgumentException("范围下限不小于等于范围上限");

		this.versionFrom = versionFrom;
		this.versionTo = versionTo;
		this.includeFrom = includeFrom;
		this.includeTo = includeTo;
	}

	public boolean includes(@NotNull Version version)
	{
		if(versionFrom != null) // 需要对比下限
		{
			var cp = versionFrom.compareTo(version);
			if(includeFrom && cp <= 0) ;
			else if(!includeFrom && cp < 0) ;
			else return false;
		}
		if(versionTo != null) // 需要对比上限
		{
			var cp = versionTo.compareTo(version);
			if(includeTo && cp >= 0) ;
			else if(!includeTo && cp > 0) ;
			else return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof VersionRange vr) return
				Objects.equals(this.includeFrom, vr.includeFrom) &&
				Objects.equals(this.includeTo, vr.includeTo) &&
				Objects.equals(this.versionFrom, vr.versionFrom) &&
				Objects.equals(this.versionTo, vr.versionTo);
		else return false;
	}

	/**
	 * 把一个字符串转换为语义化版本号范围
	 * @param raw 接受以 '[' 或 '(' 开头, 以 ']' 或 ')' 结尾, 以逗号分割的两个语义化版本号范围, 语义化版本号本身不能包含逗号, 例如: "[1.2.3, 4.5.6]", "(1.2.3, 4.5.6)", "[1.2.3, 4.5.6)", "[1.2.3,)"
	 * @throws IllegalArgumentException 格式错误
	 */
	public static VersionRange parse(@NotNull String raw) throws IllegalArgumentException
	{
		var cs = raw.toCharArray();
		final char startCh = cs[0], endCh = cs[cs.length - 1];
		if(startCh != '[' && startCh != '(') throw new IllegalArgumentException("起始字符错误");
		if(endCh != ']' && endCh != ')') throw new IllegalArgumentException("终止字符错误");
		var split = -1;
		for(var step = 1; step < cs.length - 1; step++)
		{
			if(cs[step] == ',')
			{
				if(split > 0) throw new IllegalArgumentException("语义化版本号内容不能包含逗号");
				split = step;
			}
		}
		if(split < 0) throw new IllegalArgumentException("提供的范围不完整");

		final boolean includeFrom = startCh == '[', includeTo = endCh == ']';
		final String versionFromRaw = raw.substring(1, split).trim(), versionToRaw = raw.substring(split + 1, raw.length() - 1).trim();
		final Version versionFrom = versionFromRaw.isEmpty() ? null : Version.parse(versionFromRaw);
		final Version versionTo = versionToRaw.isEmpty() ? null : Version.parse(versionToRaw);

		return new VersionRange(versionFrom, versionTo, includeFrom, includeTo);
	}

	@Override
	public String toString()
	{
		var ret = new StringBuilder();
		ret.append(includeFrom ? '[' : '(');
		ret.append(versionFrom).append(',').append(versionTo);
		ret.append(includeTo ? ']' : ')');
		return ret.toString();
	}
}
