package firok.topaz.resource;

import firok.topaz.annotation.Overload;
import firok.topaz.function.MustCloseable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

/**
 * 一个可关闭的 Graphics, 用来更方便的使用 Graphics
 * @since 7.34.0
 * @author Firok
 * @see sun.print.ProxyGraphics 跟这个基本一样, 只不过多了个 close 方法
 * */
@SuppressWarnings("JavadocReference")
public class ClosableGraphics extends Graphics implements MustCloseable
{
    private final Graphics ref;
    public ClosableGraphics(Graphics ref)
    {
        this.ref = ref;
    }
    public ClosableGraphics(Image image)
    {
        this.ref = image.getGraphics();
    }

    @Override
    public Graphics create()
    {
        return ref.create();
    }

    @Override
    public void translate(int x, int y)
    {
        ref.translate(x, y);
    }

    @Override
    public Color getColor()
    {
        return ref.getColor();
    }

    @Override
    public void setColor(Color c)
    {
        ref.setColor(c);
    }

    /**
     * @since 7.39.0
     * */
    public void setColor(int rgb)
    {
        var color = new Color(rgb);
        setColor(color);
    }

    @Override
    public void setPaintMode()
    {
        ref.setPaintMode();
    }

    @Override
    public void setXORMode(Color c1)
    {
        ref.setXORMode(c1);
    }

    /**
     * @since 7.39.0
     * */
    public void setXORMode(int rgb)
    {
        var color = new Color(rgb);
        setXORMode(color);
    }

    @Override
    public Font getFont()
    {
        return ref.getFont();
    }

    @Override
    public void setFont(Font font)
    {
        ref.setFont(font);
    }

    @Override
    public FontMetrics getFontMetrics(Font f)
    {
        return ref.getFontMetrics(f);
    }

    @Override
    public Rectangle getClipBounds()
    {
        return ref.getClipBounds();
    }

    @Override
    public void clipRect(int x, int y, int width, int height)
    {
        ref.clipRect(x, y, width, height);
    }

    @Override
    public void setClip(int x, int y, int width, int height)
    {
        ref.setClip(x, y, width, height);
    }

    @Override
    public Shape getClip()
    {
        return ref.getClip();
    }

    @Override
    public void setClip(Shape clip)
    {
        ref.setClip(clip);
    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy)
    {
        ref.copyArea(x, y, width, height, dx, dy);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2)
    {
        ref.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void fillRect(int x, int y, int width, int height)
    {
        ref.fillRect(x, y, width, height);
    }

    @Override
    public void clearRect(int x, int y, int width, int height)
    {
        ref.clearRect(x, y, width, height);
    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
    {
        ref.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
    {
        ref.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public void drawOval(int x, int y, int width, int height)
    {
        ref.drawOval(x, y, width, height);
    }

    @Override
    public void fillOval(int x, int y, int width, int height)
    {
        ref.fillOval(x, y, width, height);
    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle)
    {
        ref.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle)
    {
        ref.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints)
    {
        ref.drawPolyline(xPoints, yPoints, nPoints);
    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints)
    {
        ref.drawPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints)
    {
        ref.fillPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public void drawString(@NotNull String str, int x, int y)
    {
        ref.drawString(str, x, y);
    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, int x, int y)
    {
        ref.drawString(iterator, x, y);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer)
    {
        return ref.drawImage(img, x, y, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer)
    {
        return ref.drawImage(img, x, y, width, height, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer)
    {
        return ref.drawImage(img, x, y, bgcolor, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer)
    {
        return ref.drawImage(img, x, y, width, height, bgcolor, observer);
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer)
    {
        return ref.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer)
    {
        return ref.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
    }

    @Override
    public void dispose()
    {
        ref.dispose();
    }

    // finally, we got this
    @Override
    public void close()
    {
        this.dispose();
    }
}
