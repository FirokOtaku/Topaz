package firok.topaz;

import lombok.SneakyThrows;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;

import java.util.Map;

/**
 * JavaScript 脚本执行器
 *
 * @since 3.2.0
 * @author Firok
 * */
public class JavascriptInvoker implements AutoCloseable
{
	final Context engine;
	public JavascriptInvoker(Map<String, Object> map)
	{
		engine = Context.newBuilder()
				.allowHostAccess(HostAccess.ALL)
				.allowAllAccess(true)
				.build();
		var bindings = engine.getBindings("js");
		for(var entry : map.entrySet())
		{
			bindings.putMember(
					entry.getKey(),
					entry.getValue()
			);
		}

	}

	@SneakyThrows
	public Object eval(String command)
	{
		return engine.eval("js", command);
	}

	public <T> T getValue(String name)
	{
		var value = engine.getBindings("js").getMember(name);
		return value.asHostObject();
	}

	@Override
	@SneakyThrows
	public void close()
	{
		engine.close();
	}
}