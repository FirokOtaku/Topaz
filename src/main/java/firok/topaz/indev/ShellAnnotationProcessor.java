package firok.topaz.indev;

import firok.topaz.reflection.Reflections;
import firok.topaz.thread.LockCompound;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.ToolProvider;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

@SupportedAnnotationTypes("firok.topaz.indev.AnnotationProcessingShell")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class ShellAnnotationProcessor extends AbstractProcessor
{
    private Types typesUtil;
    private Elements elementsUtil;
    private Filer filerUtil;
    private Messager messager;
    private final ReentrantLock LockFiler = new ReentrantLock();
    private final ReentrantLock LockReflect = new ReentrantLock();

    @Override
    public synchronized void init(ProcessingEnvironment env)
    {
        super.init(env);
        typesUtil = env.getTypeUtils();
        elementsUtil = env.getElementUtils();
        filerUtil = env.getFiler();
        messager = env.getMessager();
        messager.printNote("ShellAnnotationProcessor 初始化完成");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env)
    {
        messager.printNote("ShellAnnotationProcessor 处理中");
        var compiler = ToolProvider.getSystemJavaCompiler();
        messager.printNote("当前编译器: " + compiler);

        for (Element ele : env.getElementsAnnotatedWith(AnnotationProcessingShell.class))
        {
            AnnotationProcessingShell anno;
            try (var ignored = new LockCompound(LockReflect))
            {
                messager.printNote("找到注解处理器于: " + ele);
                anno = ele.getAnnotation(AnnotationProcessingShell.class);
            }
            Element eleMirror = null;
            String className = null;
            try
            {
                var ignored = anno.value();
            }
            catch (MirroredTypeException any)
            {
                var mirror = any.getTypeMirror();

                eleMirror = typesUtil.asElement(mirror);
                messager.printNote("加载到 TypeMirror: " + eleMirror);
                messager.printNote("TypeMirror 类型: " + eleMirror.getKind());
                className = eleMirror.toString();
            }

            try
            {
                var file = elementsUtil.getFileObjectOf(eleMirror);
                messager.printNote("加载资源完成: " + file);
                var uri = file.toUri();
                messager.printNote("路径: " + uri.getPath());
                var result = compiler.run(null, null, null, uri.getPath());
                messager.printNote("编译结果: " + result);
            }
            catch (Exception any)
            {
                messager.printError("加载资源时出现异常: " + any.getLocalizedMessage());
            }

            try
            {
                var classProcessor = (Class<? extends EndAnnotationProcessor>) Reflections.findClassOf(className);
                messager.printNote("加载到 Class: " + classProcessor);
                var cons = Reflections.constructorOf(
                        classProcessor,
                        Types.class,
                        Elements.class,
                        Filer.class,
                        Messager.class
                );
                var instance = Reflections.newInstanceOf(cons, typesUtil, elementsUtil, filerUtil, messager);
                messager.printNote("初始化完成: " + instance);
                var result = instance.process(annotations, env);
                messager.printNote("执行完成: " + result);
            }
            catch (Exception any)
            {
                messager.printError("处理注解时出现异常: " + any.getLocalizedMessage());
            }
        }
        return true;
    }
}
