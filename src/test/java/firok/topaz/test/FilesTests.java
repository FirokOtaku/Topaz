package firok.topaz.test;

import firok.topaz.resource.Files;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class FilesTests
{
//	@Test
	void testPngMetadataIO()
	{
		var file = new File("./test.png");

		try
		{
			if(!file.exists())
			{
				var imageRaw = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
				ImageIO.write(imageRaw, "png", file);
			}

			Files.updatePngMetadata(file, map -> {
				System.out.println(map);

				var mapNew = new HashMap<String, String>();
				mapNew.put("test", "test");
				mapNew.put("test_key", "this is a test value");
				mapNew.put("TEST_KEY_2", "this is a test value 2");
				mapNew.put("time", String.valueOf(System.currentTimeMillis()));

				System.out.println(mapNew);
				return mapNew;
			});
		}
		catch (Exception any)
		{
			any.printStackTrace(System.err);
		}
		finally
		{
			if(file.exists())
				file.delete();
		}
	}

	@Test
	void testAppendMetadata() throws IOException
	{
		var fileOrigin = new File("./docs/topaz.jpg");
		var fileOutput = new File("./docs/topaz.output.png");
		var bi = ImageIO.read(fileOrigin);
		var map = new HashMap<String, String>();
		map.put("test_key", "test_value");
		map.put("time", String.valueOf(System.currentTimeMillis()));
		Files.writePngWithMetadata(fileOutput, bi, map);
	}

//	@Test
	void testParent()
	{
		var file = new File("a/b/c/d/e/f/g/test.txt").getAbsoluteFile();
		for(var step = 0; step < 30; step++)
		{
			var parent = file.getParentFile();
			if(parent == null)
			{
				System.out.println("parent null");
				break;
			}

			System.out.println("step: " + step + ", now: " + file.getAbsolutePath() + ", parent: " + parent.getAbsolutePath());
			file = parent;
		}
	}

	@Test
	void testCleanDelete() throws Exception
	{
		var structureAll = new File("test-cache/f1/f2/f3");
		structureAll.mkdirs();

		var file31 = new File("test-cache/f1/f2/f3/f31.txt");
		file31.createNewFile();
		var file11 = new File("test-cache/f1/f11.txt");
		file11.createNewFile();

		var folder1 = new File("test-cache/f1");
		var folder2 = new File("test-cache/f1/f2");
		var folder3 = new File("test-cache/f1/f2/f3");
		Assertions.assertTrue(folder1.exists());
		Assertions.assertTrue(folder2.exists());
		Assertions.assertTrue(folder3.exists());
		Assertions.assertTrue(file31.exists());
		Assertions.assertTrue(file11.exists());

		Files.cleanDeleteFile(file31, 5);
		Assertions.assertTrue(folder1.exists());
		Assertions.assertTrue(file11.exists());
		Assertions.assertFalse(folder2.exists());
		Assertions.assertFalse(folder3.exists());
		Assertions.assertFalse(file31.exists());

		structureAll.mkdirs();
		file11.createNewFile();
		file31.createNewFile();
		Assertions.assertTrue(folder1.exists());
		Assertions.assertTrue(folder2.exists());
		Assertions.assertTrue(folder3.exists());

		Files.recursivelyDelete(folder1);
		Assertions.assertFalse(folder1.exists());
		Assertions.assertFalse(folder2.exists());
		Assertions.assertFalse(folder3.exists());
		Assertions.assertFalse(file11.exists());
		Assertions.assertFalse(file31.exists());
	}

}
