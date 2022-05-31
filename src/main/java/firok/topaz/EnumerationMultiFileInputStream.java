package firok.topaz;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * 从多个文件中创建输入流
 * @implNote 枚举器并不负责释放文件流, 调用者需要自己释放
 * @since 2.0.0
 */
public class EnumerationMultiFileInputStream implements Enumeration<InputStream>
{
	File[] files;
	int index;
	public EnumerationMultiFileInputStream(File[] files)
	{
		this.files=files;
		this.index=0;
	}

	@Override
	public boolean hasMoreElements()
	{
		return files != null && index < files.length;
	}

	@Override
	public InputStream nextElement()
	{
		try
		{
			return new FileInputStream(files[index++]);
		}
		catch (Exception e) // 非常恶心
		{
			throw new RuntimeException(e);
		}
	}
}
