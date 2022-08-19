package firok.topaz;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * 方便跟本地进程交互的玩意
 * 主要是为了获取两个流里面的内容
 *
 * @author Firok
 * @since 2.0.0
 */
public final class NativeProcess implements AutoCloseable
{
	private final Process process;
	private final ThreadStream threadOut, threadErr;
	private final OutputStream os;
	private final PrintStream ps;

	/**
	 * @param command 需要执行的命令
	 * @throws Exception 创建进程失败
	 */
	public NativeProcess(String command) throws Exception
	{
		try
		{
			process = Runtime.getRuntime().exec(command);

			var inOut = process.getInputStream();
			var inErr = process.getErrorStream();
			os = process.getOutputStream();
			ps = new PrintStream(os);
			threadOut = new ThreadStream(inOut);
			threadErr = new ThreadStream(inErr);
			threadOut.start();
			threadErr.start();
		}
		catch (Exception e)
		{
			close();
			throw new IllegalArgumentException("创建进程失败", e);
		}
	}

	/**
	 * 用来缓存流内容的工具类
	 * @implNote 进程不停止 流是不会关的 在主线程里做while循环会阻塞主线程 所以开个子线程做这事
	 */
	private static class ThreadStream extends Thread
	{
		private static final String SYS_ENCODING = System.getProperty("sun.jnu.encoding");

		final InputStream is;
		final StringBuilder buffer;
		ThreadStream(InputStream is)
		{
			this.is = is;
			this.buffer = new StringBuilder();

			this.setDaemon(true);
		}

		@Override
		public void run()
		{
			var charset = Charset.forName(SYS_ENCODING);
			try(var scanner = new Scanner(this.is, charset))
			{
				while(scanner.hasNextLine())
				{
					buffer.append(scanner.nextLine()).append("\n");
				}
			}
		}

		public String content()
		{
			return buffer.toString();
		}
	}

	public int waitFor() throws InterruptedException
	{
		return process.waitFor();
	}
	public boolean waitFor(long timeout, TimeUnit unit) throws InterruptedException
	{
		return process.waitFor(timeout, unit);
	}

	public void print(String content)
	{
		ps.print(content);
	}
	public void println(String content)
	{
		ps.println(content);
	}

	public String contentErr()
	{
		return threadErr.content();
	}
	public String contentOut()
	{
		return threadOut.content();
	}

	@Override
	public void close() throws Exception
	{
		if(process != null && process.isAlive())
			process.destroy();

		if(threadOut != null && threadOut.isAlive())
			threadOut.interrupt();
		if(threadErr != null && threadErr.isAlive())
			threadErr.interrupt();

		if(ps != null) ps.close();
		if(os != null) os.close();
	}
}
