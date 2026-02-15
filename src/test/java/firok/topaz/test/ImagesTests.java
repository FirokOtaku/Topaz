package firok.topaz.test;

import firok.topaz.design.Images;
import firok.topaz.function.Condition;
import firok.topaz.function.Conditions;
import firok.topaz.function.MayRunnable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

import static firok.topaz.function.Conditions.*;

public class ImagesTests
{
    private static final Condition[] when = new Condition[] {
            whenBefore(new Date(2023, Calendar.DECEMBER, 27, 18, 0, 0)),
            whenFileExist("redstone-core.eff"),
    };
//    @Test
    public void testImageConvertOp() throws Exception
    {
        onlyRun((MayRunnable) () -> {
            var inputImage = ImageIO.read(new File("C:/Users/a3517/Pictures/88578976_p0.jpg"));
            var outputPath = new File("./image-test-01.png");
            var outputImage = Images.processingWithGrayscale(inputImage);
            ImageIO.write(outputImage, "png", outputPath);
        }, when);
    }

//    @Test
    public void testImageProcess() throws Exception
    {
        onlyRun((MayRunnable) () -> {
            var inputImage = ImageIO.read(new File("C:/Users/a3517/Pictures/88578976_p0.jpg"));
            var outputImage255 = Images.processingWithHistogramEqualization(inputImage, 255);
            var outputImage65535 = Images.processingWithHistogramEqualization(inputImage, 65535);
            ImageIO.write(outputImage255, "png", new File("./image-test-HE-255.png"));
            ImageIO.write(outputImage65535, "png", new File("./image-test-HE-65535.png"));
        }, when);

    }

    private void testGetImageSize(
            String path,
            int width,
            int height
    )
    {
        System.out.println("path|" + path);
        var res = ImagesTests.class.getResource("/firok/topaz/test/" + path);
        var pathAbsolute = res.getFile();
        var file = new File(pathAbsolute);
        var size = Images.getImageSize(file);
        Assertions.assertEquals(width, size.getWidth());
        Assertions.assertEquals(height, size.getHeight());
    }

//    @Test // fixme high 暂时忽略此测试
    public void testGetImageSizes()
    {
        testGetImageSize("35_137.bmp", 35, 137);
        testGetImageSize("70_155.jpg", 70, 155);
        testGetImageSize("87_67.png", 87, 67);
        testGetImageSize("130_81.jpg", 130, 81);
        testGetImageSize("162_200.png", 162, 200);
        testGetImageSize("166_90.bmp", 166, 90);
        testGetImageSize("233_103.png", 233, 103);
    }
}
