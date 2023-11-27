package firok.topaz.test.indev;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.io.File;

/**
 * 直方图运算测试类
 * */
public class HistogramEqualizationTests
{
    public static final File file = new File("C:/Users/a3517/Pictures/105369331_p0.jpg");
    @Test
    public void test() throws Exception
    {
        var img = ImageIO.read(file);

    }
}
