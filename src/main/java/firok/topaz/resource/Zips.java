package firok.topaz.resource;

import firok.topaz.annotation.Indev;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具方法
 * @since 5.6.0
 * @author Firok
 * */
@Indev(description = "尚未经过测试")
public final class Zips
{
    private Zips() { }

    private static void zipOne(File raw, String[] levels, Set<String> structures, ZipOutputStream ozs)
            throws IOException, InvalidPathException
    {
        if(!raw.exists())
            throw new FileNotFoundException(raw.getAbsolutePath());

        var levelsNow = Arrays.copyOf(levels, levels.length + 1);
        levelsNow[levelsNow.length - 1] = raw.getName();

        if(raw.isFile())
        {
            var structure = String.join("/", levelsNow);
            if(structures.contains(structure))
            {
                throw new InvalidPathException(structure, "结构重复");
            }
            var entry = new ZipEntry(structure);
            try(var ifs = new FileInputStream(raw))
            {
                ozs.putNextEntry(entry);
                ifs.transferTo(ozs);
                ozs.closeEntry();
            }
        }
        else if(raw.isDirectory())
        {
            var children = raw.listFiles();
            if(children == null || children.length == 0) return;
            for(var child : children)
            {
                zipOne(child, levelsNow, structures, ozs);
            }
        }
    }

    /**
     * 将多个文件或目录压缩至指定输出流
     * @implNote 此方法不负责关闭输出流
     * @throws IOException 输入输出发生错误
     * @throws InvalidPathException 如果多个实体向压缩包内同一个目标位置写入数据, 则抛出此异常
     * @since 5.6.0
     * @author Firok
     * */
    public static void zipTo(File[] raws, OutputStream os) throws IOException, InvalidPathException
    {
        try(var ozs = new ZipOutputStream(os))
        {
            var levelsNow = new String[0];
            var structures = new HashSet<String>();
            for(var raw : raws)
            {
                zipOne(raw, levelsNow, structures, ozs);
            }
            ozs.flush();
        }
    }
}
