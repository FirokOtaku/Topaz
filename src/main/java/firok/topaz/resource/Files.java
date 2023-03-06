package firok.topaz.resource;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
	public static void assertNoExist(String path, String msg) throws IOException
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

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static FileOutputStream writeTo(File file) throws IOException
	{
		var parent = file.getParentFile();
		parent.mkdirs();
		file.createNewFile();
		return new FileOutputStream(file);
	}

	public static void writeTo(File file, String content, Charset charset) throws IOException
	{
		try(var ofs = writeTo(file))
		{
			ofs.write(content.getBytes(charset));
		}
	}
	public static void writeTo(File file, String content) throws IOException
	{
		writeTo(file, content, StandardCharsets.UTF_8);
	}

//	public static void transferTo(File fileFrom, File fileTo) throws IOException
//	{
//		try(
//				var ifs = new FileInputStream(fileFrom);
//				var ofs = new FileOutputStream(fileTo)
//		){
//			ifs.transferTo(ofs);
//		}
//	}

	public static String unixPathOf(File file) throws IOException
	{
		return file.getCanonicalPath().replaceAll("\\\\", "/");
	}
}
