package firok.topaz.test;

import firok.topaz.resource.Files;
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

}
