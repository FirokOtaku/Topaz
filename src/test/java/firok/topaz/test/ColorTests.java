package firok.topaz.test;

import firok.topaz.design.ColorRGB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class ColorTests
{
    private static void testOneConvert(
            int r, int g, int b,
            double h,
            double s1, double l1,
            double s2, double l2
    )
    {
        var colorRGB = ColorRGB.fromAWT(new Color(r, g, b));
        var colorHSL = colorRGB.toHSL();
        var colorHSV = colorRGB.toHSV();
        var colorRGB2 = colorHSL.toRGB();
        var colorRGB3 = colorHSV.toRGB();
        System.out.println(colorRGB + " -> " + colorRGB2 + " | " + colorRGB3);
        Assertions.assertTrue(Math.abs(colorHSL.h() - h) < 1);
        Assertions.assertTrue(Math.abs(colorHSL.s() * 100 - s1) < 1);
        Assertions.assertTrue(Math.abs(colorHSL.l() * 100 - l1) < 1);
        Assertions.assertTrue(Math.abs(colorHSV.s() * 100 - s2) < 1);
        Assertions.assertTrue(Math.abs(colorHSV.v() * 100 - l2) < 1);
        Assertions.assertTrue(Math.abs(colorRGB.r() - colorRGB2.r()) < 0.0001);
        Assertions.assertTrue(Math.abs(colorRGB.g() - colorRGB2.g()) < 0.0001);
        Assertions.assertTrue(Math.abs(colorRGB.b() - colorRGB2.b()) < 0.0001);
        Assertions.assertTrue(Math.abs(colorRGB.r() - colorRGB3.r()) < 0.0001);
        Assertions.assertTrue(Math.abs(colorRGB.g() - colorRGB3.g()) < 0.0001);
        Assertions.assertTrue(Math.abs(colorRGB.b() - colorRGB3.b()) < 0.0001);
    }
    @Test
    public void testColorConvert()
    {
        testOneConvert(105, 193, 67, 101.9, 50.4, 50.98, 65.28, 75.69);
        testOneConvert(193, 47, 37, 4, 68.07, 45.03, 80.83, 75.69);
        testOneConvert(193, 121, 19, 35, 81.82, 41.63, 90.16, 75.69);
        testOneConvert(139, 193, 173, 158, 30.35, 65.09, 27.98, 75.69);
        testOneConvert(14, 106, 193, 209, 86.92, 40.49, 92.75, 75.69);
        testOneConvert(72, 12, 193, 260, 88.68, 40.11, 93.78, 75.69);
        testOneConvert(188, 129, 193, 295, 33.93, 63.2, 33.16, 75.69);
        testOneConvert(189, 214, 148, 83, 44.87, 70.98, 31, 84);
        testOneConvert(25, 69, 8, 103, 80.18, 14.99, 89, 27);
        testOneConvert(141, 127, 184, 255, 28.5, 60.84, 31, 72);
    }
}
