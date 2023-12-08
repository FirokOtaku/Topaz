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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

public class MathTests
{
    @Test
    public void testMaxMinMid()
    {
        // max
        Assertions.assertEquals(Integer.MAX_VALUE, Maths.max(Integer.MAX_VALUE));
        Assertions.assertEquals(100, Maths.max(1, 2, 3, 4, 100, 5));
        Assertions.assertEquals(3, Maths.max(-1, 1, 3, 2, -3));

        Assertions.assertEquals(100, Maths.max(new byte[] { 0, 1, 100, 2, 3}));

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

    @Test
    public void testRange()
    {
        Assertions.assertEquals(0, Maths.range(0, 0, 1));
        Assertions.assertEquals(123, Maths.range(200, 0, 123));
        Assertions.assertEquals(50, Maths.range(50, 0, 123));
        Assertions.assertEquals(new Date(0), Maths.range(new Date(0), new Date(0), new Date(100)));
        Assertions.assertEquals(BigDecimal.TWO, Maths.range(BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TWO));
        Assertions.assertEquals(100, Maths.range(100L, 0L, 200L));
    }

    @Test
    public void testIsInRange()
    {
        Assertions.assertTrue(Maths.isInRange(1, 0, 2));
        Assertions.assertFalse(Maths.isInRange(0, 1, 2));
        Assertions.assertFalse(Maths.isInRange(0, 100, 200));
        Assertions.assertFalse(Maths.isInRange(0, 100, 0));
        Assertions.assertTrue(Maths.isInRange(100, 40, 200));
        Assertions.assertTrue(Maths.isInRange(100L, 40L, 200L));
        Assertions.assertTrue(Maths.isInRange(BigDecimal.TWO, BigDecimal.ZERO, BigDecimal.TEN));
        Assertions.assertTrue(Maths.isInRange(new Date(System.currentTimeMillis() - 1000), new Date(0), new Date()));
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
