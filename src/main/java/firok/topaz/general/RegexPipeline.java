package firok.topaz.general;

import org.intellij.lang.annotations.RegExp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @since 3.8.0
 * @author Firok
 * */
public class RegexPipeline
{
	public String replaceFirst(String content, @RegExp String regex, String replacement)
	{
		return getPattern(regex)
				.matcher(content)
				.replaceFirst(replacement);
	}
	public String replaceFirst(String content, Map<String, String> mapRegexReplacement)
	{
		for(var entryRegexReplacement : mapRegexReplacement.entrySet())
		{
			var regex = entryRegexReplacement.getKey();
			var replacement = entryRegexReplacement.getValue();
			content = replaceFirst(content, regex, replacement);
		}
		return content;
	}

	public String replaceAll(String content, @RegExp String regex, String replacement)
	{
		return getPattern(regex)
				.matcher(content)
				.replaceAll(replacement);
	}
	public String replaceAll(String content, Map<String, String> mapRegexReplacement)
	{
		for(var entryRegexReplacement : mapRegexReplacement.entrySet())
		{
			@RegExp
			var regex = entryRegexReplacement.getKey();
			var replacement = entryRegexReplacement.getValue();
			content = replaceAll(content, regex, replacement);
		}
		return content;
	}

	// 正则缓存
	private final Map<String, Pattern> mapCachedPattern = new ConcurrentHashMap<>();
	public Pattern getPattern(@RegExp String regex)
	{
		return mapCachedPattern.computeIfAbsent(regex, Pattern::compile);
	}
	public void cleanPatterns()
	{
		mapCachedPattern.clear();
	}
}
