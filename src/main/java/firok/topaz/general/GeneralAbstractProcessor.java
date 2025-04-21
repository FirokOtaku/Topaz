package firok.topaz.general;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static firok.topaz.general.Collections.isNotEmpty;

/**
 * 一个对 {@link AbstractProcessor} 稍微增加了一点封装的注解处理器
 * @since 7.42.0
 * @author Firok
 * */
public abstract class GeneralAbstractProcessor extends AbstractProcessor
{
    protected String id;
    protected Types types;
    protected Elements elements;
    protected Filer filer;
    protected Messager messager;
    protected Map<String, String> options;
    protected Locale locale;
    protected SourceVersion sourceVersion;

    protected GeneralAbstractProcessor(String id)
    {
        this.id = id;
    }
    protected GeneralAbstractProcessor()
    {
        var name = getClass().getSimpleName();
        this.id = name;
    }

    private String content(Object obj)
    {
        var buffer = new StringBuilder();
        buffer.append('[').append(id).append("] ");

        if(obj instanceof Throwable any)
        {
            var textStackTrace = Strings.stackTraceOf(any);
            buffer.append(textStackTrace);
        }
        else
        {
            buffer.append(obj);
        }
        return buffer.toString();
    }

    protected void logNote(Object obj)
    {
        messager.printNote(content(obj));
    }
    protected void logWarning(Object obj)
    {
        messager.printWarning(content(obj));
    }
    protected void logError(Object obj)
    {
        messager.printError(content(obj));
    }

    @Override
    public synchronized void init(ProcessingEnvironment env)
    {
        super.init(env);
        this.types = env.getTypeUtils();
        this.elements = env.getElementUtils();
        this.filer = env.getFiler();
        this.messager = env.getMessager();
        this.locale = env.getLocale();
        this.sourceVersion = env.getSourceVersion();
        this.options = new HashMap<>();
        var optionsRaw = env.getOptions();
        if(isNotEmpty(optionsRaw))
        {
           this.options.putAll(optionsRaw);
        }
    }
}
