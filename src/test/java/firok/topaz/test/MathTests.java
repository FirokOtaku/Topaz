package firok.topaz.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import firok.topaz.design.ChineseSolarTermColors;
import firok.topaz.design.Colors;
import firok.topaz.math.Maths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MathTests
{
    @Test
    public void testMaxMinMid()
    {
        // max
        Assertions.assertEquals(Integer.MAX_VALUE, Maths.max(Integer.MAX_VALUE));
        Assertions.assertEquals(100, Maths.max(1, 2, 3, 4, 100, 5));
        Assertions.assertEquals(3, Maths.max(-1, 1, 3, 2, -3));

        // min
        Assertions.assertEquals(-1, Maths.min(-1, 2, 3, 4));
        Assertions.assertEquals(-100, Maths.min(100, 20, -10, -100, 30));

        // mid
        Assertions.assertEquals(2, Maths.mid(1, 2, 3));
        Assertions.assertEquals(2, Maths.mid(1, 3, 2));
        Assertions.assertEquals(999, Maths.mid(-1, 9999, 999));
        Assertions.assertEquals(999L, Maths.mid(-999L, 9999L, 999L));
        Assertions.assertEquals((short) 0, Maths.mid((short) 0, (short) -1, (short) 1));
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record PolyfitRaw(
            double[] time,
            double[] data
    ) { }

    @Test
    public void testPolyfit() throws IOException
    {
        var height = 200;
        var om = new ObjectMapper();
        var raw = om.readValue(MathTests.class.getResource("/firok/topaz/test/math-fit-test.json"), PolyfitRaw.class);
        var meanResult = Maths.meanFit(raw.data, 3);
        double meanMax = Double.MIN_VALUE, meanMin = Double.MAX_VALUE;
        double rawMax = Double.MIN_VALUE, rawMin = Double.MAX_VALUE;
        for (double v : meanResult)
        {
            meanMax = Math.max(meanMax, v);
            meanMin = Math.min(meanMin, v);
        }
        for(double v : raw.data)
        {
            rawMax = Math.max(rawMax, v);
            rawMin = Math.min(rawMin, v);
        }
        final double meanInterval = meanMax - meanMin;
        final double rawInterval = rawMax - rawMin;

        var image = new BufferedImage(raw.time.length, height, BufferedImage.TYPE_INT_RGB);
        var gra = image.createGraphics();
        gra.setColor(Color.WHITE);
        gra.fillRect(0, 0, image.getWidth(), image.getHeight());
        gra.dispose();
        for (int x = 0; x < raw.time.length; x++)
        {
            double meanFactor = (meanResult[x] - meanMin) / meanInterval;
            double rawFactor = (raw.data[x] - rawMin) / rawInterval;

            var colorMean = Colors.getColorRGBBetween(ChineseSolarTermColors.翠缥, ChineseSolarTermColors.木兰, meanFactor);
            image.setRGB(x, (int) (meanFactor * (height - 1)), colorMean);
            image.setRGB(x, (int) (rawFactor * (height - 1)), ChineseSolarTermColors.蓝采和);
        }
        new File("./test-cache").mkdirs();
        ImageIO.write(image, "png", new File("./test-cache/fit-test-01.png"));
    }
}
