package firok.topaz.test;

import firok.topaz.platform.Processes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProcessesTests
{
    @Test
    public void testJvmExe()
    {
        var fileJvmExe = Processes.getCurrentJvmExecutable();
        System.out.println("当前 JVM 可执行文件路径为: ");
        System.out.println(fileJvmExe.getAbsolutePath());
        Assertions.assertTrue(fileJvmExe.getName().contains("java"));
    }
}
