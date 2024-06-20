package firok.topaz.platform;

import firok.topaz.annotation.Indev;

import java.io.File;
import java.util.List;

/**
 * 进程相关
 * @since 5.8.0
 * @author Firok
 * */
public final class Processes
{
    private Processes() { }

    /**
     * 直接停止进程树
     * */
    @Indev(description = "未经过详细测试")
    public static void killProcessTreeForcibly(long pid)
    {
        ProcessHandle.of(pid).ifPresent(process -> {
            // 进程已经停止, 忽略
            if(!process.isAlive()) return;

            // 递归停止所有子进程
            List<ProcessHandle> listChildren;
            while(!(listChildren = process.children().toList()).isEmpty())
            {
                listChildren.forEach(childProcess -> killProcessTreeForcibly(childProcess.pid()));
            }

            // 停止当前进程
            process.destroyForcibly();
        });
    }

    /**
     * 直接停止进程树
     * */
    @Indev(description = "未经过详细测试")
    public static void killProcessTreeForcibly(Process process) { killProcessTreeForcibly(process.pid()); }

    /**
     * 直接停止进程树
     * */
    @Indev(description = "未经过详细测试")
    public static void killProcessTreeForcibly(ProcessHandle process) { killProcessTreeForcibly(process.pid()); }

    /**
     * 获取当前 JVM 的可执行文件位置
     * @return 当前 JVM 的可执行文件位置. 如果获取不到则返回空
     * @since 7.2.0
     * */
    public static File getCurrentJvmExecutable()
    {
        var path = ProcessHandle.current().info().command();
        return path.map(File::new).orElse(null);
    }
}
