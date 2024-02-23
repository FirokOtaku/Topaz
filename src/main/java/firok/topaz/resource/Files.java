package firok.topaz.resource;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static firok.topaz.general.Collections.isEmpty;

/**
 * 文件操作辅助类
 *
 * @since 3.19.0
 * @author Firok
 * */
public final class Files
{
	private Files() { }

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
	 * 检查指定文件的父目录是否存在. 如果不存在则创建
	 * @return 本次操作是否创建了一个目录
	 * @since 7.17.0
	 * */
	public static boolean mkParent(File file)
	{
		var folder = file.getParentFile();
		if(!checkExist(folder, false))
			return folder.mkdirs();
		return false;
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
		mkParent(file);
		file.createNewFile();
		return new FileOutputStream(file);
	}

	public static void writeTo(File file, String content, Charset charset) throws IOException
	{
		try(var ofs = writeTo(file))
		{
			ofs.write(content.getBytes(charset));
			ofs.flush();
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

	private static String createOutputName(final File file) {
		String name = file.getName();
		int dotIndex = name.lastIndexOf('.');

		String baseName = name.substring(0, dotIndex);
		String extension = name.substring(dotIndex);

		return baseName + "_copy" + extension;
	}

	/**
	 * 为 PNG 文件添加元数据
	 * @since 5.21.0
	 * */
	private static void addTextEntry(final IIOMetadata metadata, final String key, final String value) throws IIOInvalidTreeException
	{
		IIOMetadataNode textEntry = new IIOMetadataNode("TextEntry");
		textEntry.setAttribute("keyword", key);
		textEntry.setAttribute("value", value);

		IIOMetadataNode text = new IIOMetadataNode("Text");
		text.appendChild(textEntry);

		IIOMetadataNode root = new IIOMetadataNode(IIOMetadataFormatImpl.standardMetadataFormatName);
		root.appendChild(text);

		metadata.mergeTree(IIOMetadataFormatImpl.standardMetadataFormatName, root);
	}

	private static String getTextEntry(final IIOMetadata metadata, final String key)
	{
		IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);
		NodeList entries = root.getElementsByTagName("TextEntry");

		for (int i = 0; i < entries.getLength(); i++) {
			IIOMetadataNode node = (IIOMetadataNode) entries.item(i);
			if (node.getAttribute("keyword").equals(key)) {
				return node.getAttribute("value");
			}
		}

		return null;
	}

	/**
	 * 获取图片文件读取器
	 * @since 5.21.0
	 * */
	public static ImageReader getImageReader(ImageInputStream iis)
	{
		var iterReader = ImageIO.getImageReaders(iis);
		if(!iterReader.hasNext())
			throw new IllegalArgumentException("无法确定文件读取器");
		var reader = iterReader.next();
		if(reader == null)
			throw new IllegalArgumentException("文件读取器加载错误");
		reader.setInput(iis);
		return reader;
	}

	/**
	 * 获取图片文件写入器
	 * @since 5.21.0
	 * */
	public static ImageWriter getImageWriter(ImageReader reader, ImageOutputStream ois)
	{
		var writer = ImageIO.getImageWriter(reader);
		writer.setOutput(ois);
		return writer;
	}

	/**
	 * 从 PNG 格式文件获取文本格式的元数据
	 * @since 5.21.0
	 * */
	public static Map<String, String> getPngTextMetadata(IIOMetadata metadata)
	{
		var ret = new HashMap<String, String>();
		var root = metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);
		var rootNodes = root.getChildNodes();
		var countNodes = rootNodes.getLength();
		for(var step = 0; step < countNodes; step++)
		{
			var node = rootNodes.item(step);
			if(!"Text".equals(node.getNodeName())) continue;

			var teNodes = node.getChildNodes();
			var countTeNode = teNodes.getLength();
			for(var stepTe = 0; stepTe < countTeNode; stepTe++)
			{
				var teNode = teNodes.item(stepTe);
				if(!"TextEntry".equals(teNode.getNodeName())) continue;

				var key = teNode.getAttributes().getNamedItem("keyword").getNodeValue();
				var value = teNode.getAttributes().getNamedItem("value").getNodeValue();
				ret.put(key, value);
			}
		}
		return ret;
	}

	/**
	 * 为 PNG 格式文件设定文字格式的元数据
	 * @param mapTextMetadata 如果为空, 则会删除所有 Text 节点而不添加数据
	 * @since 5.21.0
	 * */
	public static void setPngTextMetadata(IIOMetadata metadata, Map<String, String> mapTextMetadata)
	{
		var root = metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);

		// 先移除掉所有先前的节点
		var rootNodes = root.getChildNodes();
		var listNode2remove = new ArrayList<Node>();
		var countNode = rootNodes.getLength();
		for(var step = 0; step < countNode; step++)
		{
			var rootNode = rootNodes.item(step);
			if(!"Text".equals(rootNode.getNodeName())) continue;
			listNode2remove.add(rootNode);
		}
		for(var node2remove : listNode2remove)
		{
			root.removeChild(node2remove);
		}

		// 写入新的节点
		if(!isEmpty(mapTextMetadata))
		{
			var nodeText = new IIOMetadataNode("Text");
			for(var entry : mapTextMetadata.entrySet())
			{
				var nodeTextEntry = new IIOMetadataNode("TextEntry");
				nodeTextEntry.setAttribute("keyword", entry.getKey());
				nodeTextEntry.setAttribute("value", entry.getValue());
				nodeText.appendChild(nodeTextEntry);
			}
			root.appendChild(nodeText);
		}

		try
		{
			metadata.setFromTree(IIOMetadataFormatImpl.standardMetadataFormatName, root);
		}
		catch (Exception any)
		{
			throw new RuntimeException(any);
		}
	}

	/**
	 * 更新 PNG 图片元数据
	 * @since 5.21.0
	 * */
	public static void updatePngMetadata(File file, Function<Map<String, String>, Map<String, String>> updater)
		throws Exception
	{
		IIOMetadata metadata;
		ImageReader reader;
		IIOImage image;
		try(var iis = ImageIO.createImageInputStream(file))
		{
			reader = getImageReader(iis);
			image = reader.readAll(0, null);
			metadata = image.getMetadata();
		}
		var mapTextMetadata = getPngTextMetadata(metadata);
		var mapTextMetadataNew = updater.apply(mapTextMetadata);
		setPngTextMetadata(metadata, mapTextMetadataNew);
		try(var ois = ImageIO.createImageOutputStream(file))
		{
			var writer = getImageWriter(reader, ois);
			image.setMetadata(metadata);
			writer.write(image);
			ois.flush();
		}
	}

	/**
	 * 创建一个带有元数据的 PNG 文件
	 * @since 5.21.0
	 * */
	public static void writePngWithMetadata(File file, BufferedImage image, Map<String, String> mapMetadata)
		throws IOException
	{
		try(var ois = ImageIO.createImageOutputStream(file))
		{
			var writer = ImageIO.getImageWritersByFormatName("png").next();
			var root = writer.getDefaultImageMetadata(ImageTypeSpecifier.createFromRenderedImage(image), null);
			setPngTextMetadata(root, mapMetadata);
			var imageWithMetadata = new IIOImage(image, null, root);
			writer.setOutput(ois);
			writer.write(imageWithMetadata);
		}
	}

	/**
	 * 列出目录下的文件名, 不会返回 null
	 * @since 6.14.0
	 * */
	public static String[] listFilenames(File folder)
	{
		var ret = folder.list();
		return ret == null ? new String[0] : ret;
	}

	/**
	 * 列出目录下的文件名, 不会返回 null
	 * @since 6.14.0
	 * */
	public static String[] listFilenames(File folder, FilenameFilter filter)
	{
		var ret = folder.list(filter);
		return ret == null ? new String[0] : ret;
	}

	/**
	 * 列出目录下的文件, 不会返回 null
	 * @since 6.14.0
	 * */
	public static File[] listFiles(File folder)
	{
		var ret = folder.listFiles();
		return ret == null ? new File[0] : ret;
	}

	/**
	 * 列出目录下的文件, 不会返回 null
	 * @since 6.14.0
	 * */
	public static File[] listFiles(File folder, FileFilter filter)
	{
		var ret = folder.listFiles(filter);
		return ret == null ? new File[0] : ret;
	}

	/**
	 * 列出目录下的文件, 不会返回 null
	 * @since 6.14.0
	 * */
	public static File[] listFiles(File folder, FilenameFilter filter)
	{
		var ret = folder.listFiles(filter);
		return ret == null ? new File[0] : ret;
	}
}
