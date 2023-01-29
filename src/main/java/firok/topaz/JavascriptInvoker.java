package firok.topaz;

import lombok.SneakyThrows;
//import org.graalvm.polyglot.Context;
//import org.graalvm.polyglot.HostAccess;

import java.util.Map;

/**
 * JavaScript 脚本执行器
 *
 * <p>
 *     自 <code>3.26.0</code> 版本之后, 此类为空实现类
 * </p>
 *
 * @since 3.2.0
 * @author Firok
 * */
@Deprecated
public class JavascriptInvoker implements AutoCloseable
{
//	final Context engine;
	public JavascriptInvoker(Map<String, Object> map)
	{
//		engine = Context.newBuilder()
//				.allowHostAccess(HostAccess.ALL)
//				.allowAllAccess(true)
//				.build();
//		var bindings = engine.getBindings("js");
//		for(var entry : map.entrySet())
//		{
//			bindings.putMember(
//					entry.getKey(),
//					entry.getValue()
//			);
//		}

	}

	@SneakyThrows
	public Object eval(String command)
	{
//		return engine.eval("js", command);
		return null;
	}

	public <T> T getValue(String name)
	{
//		var value = engine.getBindings("js").getMember(name);
//		return value.asHostObject();
		return null;
	}

	@Override
	@SneakyThrows
	public void close()
	{
//		engine.close();
	}
}
