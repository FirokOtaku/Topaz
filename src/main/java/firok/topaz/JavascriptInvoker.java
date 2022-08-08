package firok.topaz;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;

import javax.script.Bindings;
import javax.script.SimpleBindings;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

/**
 * JavaScript 脚本执行器
 *
 * @since 3.2.0
 * @author Firok
 * */
public class JavascriptInvoker implements AutoCloseable
{
	final GraalJSScriptEngine engine;
	final Bindings context;

	public JavascriptInvoker(Map<String, Object> context)
	{
		Context.Builder builder = Context.newBuilder().allowHostAccess(HostAccess.ALL).allowAllAccess(true);
		this.engine = GraalJSScriptEngine.create(null, builder);
		this.context = new SimpleBindings(context);
	}

	@SneakyThrows
	public Object eval(String command)
	{
		return engine.eval(command, context);
	}

	@Override
	public void close()
	{
		this.engine.close();
	}
}
