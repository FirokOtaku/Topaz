package firok.topaz.platform;

import firok.topaz.TopazExceptions;
import firok.topaz.general.CodeException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 一些 Windows 用的代码
 *
 * @author Firok
 * @version 8.0.0
 * */
public class OnWindows
{
    /**
     * @see <a href="https://stackoverflow.com/questions/51098378/converting-bat-to-exe-with-no-additional-external-software-create-sfx">StackOverflow 来源</a>
     * @see <a href="https://github.com/npocmaka/batch.scripts/blob/master/hybrids/iexpress/bat2exeIEXP.bat">原始脚本</a>
     * */
    private static final String ScriptConvertBatToExe = """
            ;set "target.exe=%__cd__%%~n1.exe"
            ;set "batch_file=%~f1"
            ;set "bat_name=%~nx1"
            ;set "bat_dir=%~dp1"
            ;Set "sed=%temp%\\2exe.sed"
            ;copy /y "%~f0" "%sed%" >nul
            ;(
                ;(echo()
                ;(echo(AppLaunched=cmd /c "%bat_name%")
                ;(echo(TargetName=%target.exe%)
                ;(echo(FILE0="%bat_name%")
                ;(echo([SourceFiles])
                ;(echo(SourceFiles0=%bat_dir%)
                ;(echo([SourceFiles0])
                ;(echo(%%FILE0%%=)
            ;)>>"%sed%"
                        
            ;C:\\Windows\\System32\\iexpress.exe /n /q /m %sed%
            ;del /q /f "%sed%"
            ;exit /b 0
                        
            [Version]
            Class=IEXPRESS
            SEDVersion=3
            [Options]
            PackagePurpose=InstallApp
            ShowInstallProgramWindow=0
            HideExtractAnimation=1
            UseLongFileName=1
            InsideCompressed=0
            CAB_FixedSize=0
            CAB_ResvCodeSigning=0
            RebootMode=N
            InstallPrompt=%InstallPrompt%
            DisplayLicense=%DisplayLicense%
            FinishMessage=%FinishMessage%
            TargetName=%TargetName%
            FriendlyName=%FriendlyName%
            AppLaunched=%AppLaunched%
            PostInstallCmd=%PostInstallCmd%
            AdminQuietInstCmd=%AdminQuietInstCmd%
            UserQuietInstCmd=%UserQuietInstCmd%
            SourceFiles=SourceFiles
                        
            [Strings]
            InstallPrompt=
            DisplayLicense=
            FinishMessage=
            FriendlyName=-
            PostInstallCmd=<None>
            AdminQuietInstCmd=
            """;

    /**
     * 临时转换脚本
     * */
    static final File FileTempConvertScript;
    static final String PathTempConvertScript;
    static
    {
        try
        {
            FileTempConvertScript = File.createTempFile("convert", ".bat");
            FileTempConvertScript.deleteOnExit();
            try(var ofs = new FileWriter(FileTempConvertScript, StandardCharsets.UTF_8))
            {
                ofs.write(ScriptConvertBatToExe);
                ofs.flush();
            }
            PathTempConvertScript = FileTempConvertScript.getCanonicalPath();
        }
        catch (IOException any)
        {
            throw new RuntimeException("创建临时转换脚本出错", any);
        }
    }

    /**
     * 将 BAT 批处理文件包装为 EXE 可执行文件
     * @since 7.27.0
     * @apiNote 需要系统存在 C:\Windows\System32\iexpress.exe 程序
     * @return 暂时没什么意义, 因为成功与否都会返回 0. 后续可能返回其它值
     * */
    public static int convertBatToExe(
            File fileBat
    ) throws CodeException
    {
        Process process = null;
        try
        {
            var dir = fileBat.getParentFile();
            var params = new String[] {
                    PathTempConvertScript,
                    fileBat.getCanonicalPath(),
            };
            process = new ProcessBuilder(params).directory(dir).start();
            return process.waitFor();
        }
        catch (IOException | InterruptedException any)
        {
            return TopazExceptions.IOError.occur(any);
        }
        finally
        {
            if(process != null) process.destroyForcibly();
        }
    }
}
