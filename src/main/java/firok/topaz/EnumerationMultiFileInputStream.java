package firok.topaz;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * 从多个文件中创建输入流
 *
 * @implNote 枚举器并不负责释放文件流, 调用者需要自己释放
 * @author Firok
 * @since 2.0.0
 */
public class EnumerationMultiFileInputStream implements Enumeration<FileInputStream>
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
	public FileInputStream nextElement() throws NoSuchElementException
	{
		if(!hasMoreElements())
			throw new NoSuchElementException();

		try
		{
			return new FileInputStream(files[index++]);
		}
		catch (Exception e) // 非常恶心
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取当前进度
	 *
	 * @since 2.2.0
	 */
	public int currentIndex()
	{
		return index;
	}
}
