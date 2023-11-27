package firok.topaz.design;

import firok.topaz.math.Maths;

/**
 * 一个 HSL 色彩空间数据
 * @since 6.10.0
 * @author Firok
 * @implSpec <a href="https://zh.wikipedia.org/zh-cn/HSL%E5%92%8CHSV%E8%89%B2%E5%BD%A9%E7%A9%BA%E9%97%B4">关于 HSL 和 HSV 色彩空间</a>
 * */
public record ColorHSL(float h, float s, float l)
{
    public ColorHSL
    {
        var okay = Maths.isInRange(h, 0, 360)
                && Maths.isInRange(s, 0, 1)
                && Maths.isInRange(l, 0, 1);
        if(!okay) throw new IllegalArgumentException("HSL value out of range");
    }

    public ColorRGB toRGB()
    {
        float q, p;
        if(l < 0.5) q = l * (1 + s);
        else q = l + s - l * s;
        p = 2 * l - q;
        float hk = h / 360;

        float r = hk + 1f / 3f;
        float g = hk;
        float b = hk - 1f / 3f;

        if(r < 0) r += 1;
        if(r > 1) r -= 1;
        if(g < 0) g += 1;
        if(g > 1) g -= 1;
        if(b < 0) b += 1;
        if(b > 1) b -= 1;

        float red, green, blue;
        if(r < 1f / 6f) red = p + ((q - p) * 6 * r);
        else if(r < 0.5) red = q;
        else if(r < 2f / 3f) red = p + ((q - p) * 6 * (2f / 3f - r));
        else red = p;

        if(g < 1f / 6f) green = p + ((q - p) * 6 * g);
        else if(g < 0.5) green = q;
        else if(g < 2f / 3f) green = p + ((q - p) * 6 * (2f / 3f - g));
        else green = p;

        if(b < 1f / 6f) blue = p + ((q - p) * 6 * b);
        else if(b < 0.5) blue = q;
        else if(b < 2f / 3f) blue = p + ((q - p) * 6 * (2f / 3f - b));
        else blue = p;

        return new ColorRGB(red, green, blue);
    }
}
