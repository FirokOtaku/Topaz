package firok.topaz.design;

/**
 * 一个 HSV 色彩空间数据
 *  @since 6.10.0
 *  @author Firok
 *  @implSpec <a href="https://zh.wikipedia.org/zh-cn/HSL%E5%92%8CHSV%E8%89%B2%E5%BD%A9%E7%A9%BA%E9%97%B4">关于 HSL 和 HSV 色彩空间</a>
 * */
public record ColorHSV(float h, float s, float v)
{
    public ColorRGB toRGB()
    {
        int hi = ((int) h) / 60 % 6;
        float f = h / 60 - hi;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);
        return switch (hi)
        {
            case 0 -> new ColorRGB(v, t, p);
            case 1 -> new ColorRGB(q, v, p);
            case 2 -> new ColorRGB(p, v, t);
            case 3 -> new ColorRGB(p, q, v);
            case 4 -> new ColorRGB(t, p, v);
            case 5 -> new ColorRGB(v, p, q);
            default -> throw new IllegalArgumentException("just for compiler");
        };
    }
}
