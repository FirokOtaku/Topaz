package firok.topaz.platform;

import firok.topaz.function.MayConsumer;
import firok.topaz.resource.StreamLineEmitter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

/**
 * 方便跟本地进程交互的玩意
 * 主要是为了获取两个流里面的内容
 *
 * @author Firok
 * @since 2.0.0, 5.0.0
 */
@SuppressWarnings("unused")
public final class NativeProcess implements AutoCloseable
{
	private final Process process;
	private final StreamLineEmitter sleStdOut, sleStdErr;
	private final OutputStream os;
	private final PrintStream ps;

	private final MayConsumer<String> onStdOut, onStdErr;
	private void receiveStdOut(String line)
	{
		if(onStdOut != null)
		{
			try { onStdOut.accept(line); }
			catch (Exception ignored) { }
		}
	}
	private void receiveStdErr(String line)
	{
		if(onStdErr != null)
		{
			try { onStdErr.accept(line); }
			catch (Exception ignored) { }
		}
	}

	/**
	 * @param command 需要执行的命令
	 * @throws Exception 创建进程失败
	 */
	public NativeProcess(String command) throws Exception
	{
		this(command, null, null);
	}
	/**
	 * @param command 需要执行的命令
	 * @param onStdOut 标准输出流监听器
	 * @param onStdErr 标准错误流监听器
	 * @throws IOException 创建进程失败
	 * @implNote 监听器运行在 StreamLineEmitter 子线程
	 */
	public NativeProcess(String command, MayConsumer<String> onStdOut, MayConsumer<String> onStdErr) throws IOException
	{
		try
		{
			process = Runtime.getRuntime().exec(command);

			var inOut = process.getInputStream();
			var inErr = process.getErrorStream();
			this.onStdOut = onStdOut;
			this.onStdErr = onStdErr;
			sleStdOut = new StreamLineEmitter(inOut, true, this::receiveStdOut);
			sleStdErr = new StreamLineEmitter(inErr, true, this::receiveStdErr);
			os = process.getOutputStream();
			ps = new PrintStream(os);
		}
		catch (Exception e)
		{
			close();
			throw new IllegalArgumentException("创建进程失败", e);
		}
	}

	/**
	 * 阻塞等待本地进程
	 * */
	public int waitFor() throws InterruptedException
	{
		return process.waitFor();
	}
	/**
	 * 阻塞等待本地进程
	 * */
	public boolean waitFor(long timeout, TimeUnit unit) throws InterruptedException
	{
		return process.waitFor(timeout, unit);
	}

	/**
	 * 向命令行输出指令
	 * */
	public void print(String content)
	{
		ps.print(content);
	}
	/**
	 * 向命令行输出指令
	 * */
	public void println(String content)
	{
		ps.println(content);
	}

	public long pid() { return process.pid(); }

	/**
	 * @since 5.10.0
	 * */
	public void killTreeForcibly()
	{
		Processes.killProcessTreeForcibly(this.process);
	}

	/**
	 * @since 5.10.0
	 * */
	public void close(boolean isForcibly) throws IOException
	{
		if(process != null && process.isAlive())
		{
			if(!isForcibly) process.destroy();
			else process.destroyForcibly();
		}

		if(sleStdOut != null && !sleStdOut.isClosed()) sleStdOut.close();
		if(sleStdErr != null && !sleStdErr.isClosed()) sleStdErr.close();

		if(ps != null) ps.close();
		if(os != null) os.close();
	}

	@Override
	public void close() throws IOException
	{
		this.close(false);
	}
}
