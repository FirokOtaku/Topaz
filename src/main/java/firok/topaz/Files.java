package firok.topaz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;

/**
 * 文件操作辅助类
 *
 * @since 3.19.0
 * @author Firok
 * */
public class Files
{
	/**
	 * 检查文件是否存在
	 * */
	public static boolean checkExist(String path, Boolean isFile)
	{
		var file = new File(path);
		return checkExist(file, isFile);
	}
	public static boolean checkExist(File file, Boolean isFile)
	{
		if(isFile == null)
		{
			return file.exists();
		}
		else
		{
			return file.exists() && (file.isFile() == isFile);
		}
	}

	/**
	 * 指定文件不应存在
	 * */
	public static void assertNoExist(String path, Boolean isDirectory, String msg) throws IOException
	{
		var file = new File(path);
		assertNoExist(file, msg);
	}
	public static void assertNoExist(File file, String msg) throws IOException
	{
		if(file.exists())
			throw new FileAlreadyExistsException(msg.formatted(file.getAbsolutePath()));
	}

	/**
	 * 指定文件应当存在
	 * */
	public static void assertExist(String path, Boolean isFile, String msg) throws IOException
	{
		var file = new File(path);
		assertExist(file, isFile, msg);
	}
	public static void assertExist(File file, Boolean isFile, String msg) throws IOException
	{
		if(!file.exists())
			throw new FileNotFoundException(msg.formatted(file.getAbsolutePath()));
		if(isFile != null && isFile != file.isFile())
			throw new NoSuchFileException(msg.formatted(file.getAbsoluteFile()));
	}

//	/**
//	 * 简易实现, 判断指定文件是否位于子目录下
//	 * */
//	public static boolean isSubFile(File fileParent, File fileChild)
//	{
//		if(!fileParent.isDirectory())
//			throw new IllegalArgumentException("not a directory");
//
//		var pathParent = fileParent.getAbsolutePath();
//		var pathChild = fileChild.getAbsolutePath();
//		return pathChild.startsWith(pathParent);
//	}
}
