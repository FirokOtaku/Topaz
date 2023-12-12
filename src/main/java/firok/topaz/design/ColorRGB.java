package firok.topaz.design;

import firok.topaz.TopazExceptions;
import firok.topaz.math.Maths;

import java.awt.*;

/**
 * 一个 RGB 色彩空间数据
 * @since 6.10.0
 * @author Firok
 * */
@SuppressWarnings("DuplicatedCode")
public record ColorRGB(float r, float g, float b)
{
    public ColorRGB
    {
        var okay = Maths.isInRange(r, 0F, 1F)
                && Maths.isInRange(g, 0F, 1F)
                && Maths.isInRange(b, 0F, 1F);
        if(!okay) TopazExceptions.ColorValueOutOfRange.occur();
    }

    public static ColorRGB fromAWT(Color color)
    {
        var r = color.getRed() / 255F;
        var g = color.getGreen() / 255F;
        var b = color.getBlue() / 255F;
        return new ColorRGB(r, g, b);
    }

    public Color toAWT()
    {
        return new Color(r, g, b);
    }

    public ColorHSL toHSL()
    {
        float max = Maths.max(r, g, b);
        float min = Maths.min(r, g, b);
        float deltaMaxMin = max - min;
        float h, s, l;
        l = (max + min) / 2f;

        if(max == min) h = 0;
        else if(max == r && g >= b) h = 60 * (g - b) / deltaMaxMin;
        else if(max == r /* && g < b */) h = 60 * (g - b) / deltaMaxMin + 360;
        else if(max == g) h = 60 * (b - r) / deltaMaxMin + 120;
        else h = 60 * (r - g) / deltaMaxMin + 240;

        if(l == 0 || max == min) s = 0;
        else if(l > 0 && l <= 0.5) s = deltaMaxMin / (2 * l);
        else s = deltaMaxMin / (2 - 2 * l);

        return new ColorHSL(h % 360, s, l);
    }

    public ColorHSV toHSV()
    {
        float max = Maths.max(r, g, b);
        float min = Maths.min(r, g, b);
        float deltaMaxMin = max - min;
        float h, s, v;
        v = max;

        if(max == min) h = 0;
        else if(max == r && g >= b) h = 60 * (g - b) / deltaMaxMin;
        else if(max == r /* && g < b */) h = 60 * (g - b) / deltaMaxMin + 360;
        else if(max == g) h = 60 * (b - r) / deltaMaxMin + 120;
        else h = 60 * (r - g) / deltaMaxMin + 240;

        if(max == 0) s = 0;
        else s = deltaMaxMin / max;

        return new ColorHSV(h % 360, s, v);
    }
}
