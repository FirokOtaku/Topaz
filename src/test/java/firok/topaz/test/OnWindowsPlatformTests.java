package firok.topaz.test;

import firok.topaz.function.Conditions;
import firok.topaz.function.MayRunnable;
import firok.topaz.platform.OnWindows;
import firok.topaz.platform.PlatformTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

public class OnWindowsPlatformTests
{
    @Test
    void testConvertBatToExe() throws Exception
    {
        Conditions.onlyRun((MayRunnable) () -> {

            var fileExe = new File("test-cache/test.exe").getCanonicalFile();
            fileExe.delete();
            var fileBat = new File("test-cache/test.bat").getCanonicalFile();
            fileBat.getParentFile().mkdirs();
            fileBat.createNewFile();
            fileBat.deleteOnExit();
            try(var ofs = new FileWriter(fileBat, StandardCharsets.UTF_8))
            {
                ofs.append("echo test1\r\necho test2\r\npause");
            }

            var ret = OnWindows.convertBatToExe(fileBat);
            System.out.println("子进程返回值: " + ret);
            Assertions.assertTrue(fileExe.exists());
            fileExe.deleteOnExit();

        }, PlatformTypes.Windows::isCurrent);
    }
}
