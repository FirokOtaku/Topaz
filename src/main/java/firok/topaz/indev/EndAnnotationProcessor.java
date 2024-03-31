package firok.topaz.indev;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public abstract class EndAnnotationProcessor extends AbstractProcessor
{
    protected final Types typesUtil;
    protected final Elements elementsUtil;
    protected final Filer filerUtil;
    protected final Messager messager;
    public EndAnnotationProcessor(
        Types typesUtil,
        Elements elementsUtil,
        Filer filerUtil,
        Messager messager
    )
    {
        this.typesUtil = typesUtil;
        this.elementsUtil = elementsUtil;
        this.filerUtil = filerUtil;
        this.messager = messager;
    }
}
