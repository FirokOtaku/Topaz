package firok.topaz.indev;

import firok.topaz.annotation.Indev;
import firok.topaz.reflection.Reflections;

import java.lang.reflect.ParameterizedType;

/**
 * 联合类型
 * */
@Indev(experimental = true)
public abstract class union<UnionType1, UnionType2>
{
    private Object value;

    private Class<?> type1, type2;
    private void calcType()
    {
        var classCurrent = this.getClass();
        var classSuper = classCurrent.getGenericSuperclass();
        System.out.println("classCurrent: " + classCurrent);
        System.out.println("classSuper: " + classSuper);
        if(classSuper instanceof ParameterizedType pt)
        {
            var ata = pt.getActualTypeArguments();
            // 获取当前类型的泛型参数是什么类型的
            System.out.println(ata[0].getTypeName());
            System.out.println(ata[1].getTypeName());
            type1 = Reflections.findClassOf(ata[0].getTypeName());
            type2 = Reflections.findClassOf(ata[1].getTypeName());
        }
        else throw new RuntimeException("Union: 获取泛型参数失败");
    }

    public union(Object value)
    {
        calcType();

        if(value == null)
            return;

        set(value);
    }
    public union()
    {
        this(null);
    }

    public void set(Object value) throws ClassCastException
    {
        System.out.println("type1: " + type1);
        System.out.println("type2: " + type2);
        if(value == null)
        {
            this.value = null;
        }
        else if(type1.isInstance(value) || type2.isInstance(value))
        {
            this.value = value;
        }
        else
            throw new ClassCastException();
    }
    @SuppressWarnings("unchecked")
    public UnionType1 getAsType1() throws ClassCastException
    {
        return (UnionType1) value;
    }
    @SuppressWarnings("unchecked")
    public UnionType2 getAsType2() throws ClassCastException
    {
        return (UnionType2) value;
    }

    @Override
    public String toString()
    {
        return "union(" + type1.getSimpleName() + "|" + type2.getSimpleName() + ": " + value + ")";
    }

    public static <UnionType1, UnionType2>
    union<UnionType1, UnionType2> of(Object value)
    {
        return new union<>(value) { };
    }
}
