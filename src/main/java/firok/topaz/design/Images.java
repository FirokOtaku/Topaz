package firok.topaz.design;

import firok.topaz.TopazExceptions;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

/**
 * 图像处理相关
 * */
public class Images
{
    private static boolean loopPixel(BufferedImage image, PixelHandler handler)
    {
        var width = image.getWidth();
        var height = image.getHeight();
        try(var pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()))
        {
            if(width >= height)
            {
                for(int y = 0; y < height; y++)
                {
                    final int fy = y;
                    pool.submit(() -> {
                        for(int x = 0; x < width; x++)
                        {
                            handler.accept(x, fy);
                        }
                    });
                }
            }
            else
            {
                for(int x = 0; x < width; x++)
                {
                    final int fx = x;
                    pool.submit(() -> {
                        for(int y = 0; y < height; y++)
                        {
                            handler.accept(fx, y);
                        }
                    });
                }
            }

            pool.shutdown();
            return pool.awaitTermination(10L * width * height, java.util.concurrent.TimeUnit.MILLISECONDS);
        }
        catch (Exception any)
        {
            return false;
        }
    }
    private interface PixelHandler extends BiConsumer<Integer, Integer>
    {
    }

    /**
     * 将图片灰度化处理
     * */
    public static BufferedImage processingWithGrayscale(BufferedImage input)
    {
        var op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        var output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        op.filter(input, output);
        return output;
    }

    /**
     * 直方图均衡化的纯 CPU 实现
     * */
    public static BufferedImage processingWithHistogramEqualization(BufferedImage input, int grayLevel)
    {
        final int width = input.getWidth(), height = input.getHeight();
        final int total = width * height;
        final int[] grayCount = new int[grayLevel];

        loopPixel(input, (Integer x, Integer y) -> {
            var rgb = input.getRGB(x, y);
            var colorAWT = new Color(rgb);
            var colorRGB = ColorRGB.fromAWT(colorAWT);
            var colorHSL = colorRGB.toHSL();
            synchronized (grayCount)
            {
                grayCount[(int) (colorHSL.l() * grayLevel)]++;
            }
        });

        int sum = 0;
        float[] lut = new float[total];
        for(int step = 0; step < grayLevel; step++)
        {
            sum += grayCount[step];
            lut[step] = sum * grayLevel / total;
        }

        var output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        loopPixel(input, (Integer x, Integer y) -> {
            var colorBeforeAWT = new Color(input.getRGB(x, y));
            var colorBeforeRGB = ColorRGB.fromAWT(colorBeforeAWT);
            var colorBeforeHSL = colorBeforeRGB.toHSL();
            var hBefore = colorBeforeHSL.l();
            var hAfter = lut[(int) (hBefore * grayLevel)];
            var colorAfterHSL = new ColorHSL(colorBeforeHSL.h(), colorBeforeHSL.s(), hAfter);
            var colorAfterRGB = colorAfterHSL.toRGB();
            var colorAfterAWT = colorAfterRGB.toAWT();
            synchronized (output)
            {
                output.setRGB(x, y, colorAfterAWT.getRGB());
            }
        });

        return output;
    }

    /**
     * 读取图片尺寸, 但是不加载图片数据
     * @see <a href="https://stackoverflow.com/a/12164026/9907751">StackOverflow</a>
     * @since 7.38.0
     * @apiNote 目前通过测试的图片格式有: .png .jpg .bmp <br>
     *          其它图片格式尚未进行测试
     */
    public static Dimension getImageSize(File file)
    {
        try(var ims = new FileImageInputStream(file))
        {
            var readers = ImageIO.getImageReaders(ims);
            TopazExceptions.ImageFormatUnknown.maybe(!readers.hasNext());

            while(readers.hasNext())
            {
                var reader = readers.next();
                reader.setInput(ims);

                try
                {
                    int width = reader.getWidth(reader.getMinIndex());
                    int height = reader.getHeight(reader.getMinIndex());
                    return new Dimension(width, height);
                }
                catch (IOException ignored)
                {
                    // 没法读取, 换下一个
                }
            }
            // 找不到能读取这个文件的 reader
            return TopazExceptions.ImageMetadataError.occur();
        }
        catch (IOException any)
        {
            return TopazExceptions.IOError.occur(any);
        }
    }
}
