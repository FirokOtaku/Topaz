package firok.topaz.database;

import firok.topaz.annotation.Indev;
import firok.topaz.reflection.Reflections;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于指定数据库查询结果集字段与 Java 字段的映射关系
 * @since 7.8.0
 * @author Firok
 * */
@Indev
public class BeanMapping
{
    public <TypeEntity> TypeEntity mapping(ResultSet rs, Class<TypeEntity> classEntity)
    {
        return null;
    }

    /**
     * 使用指定配置创建映射关系
     * @param mapping 映射配置. 键需为 {@link String} 或 {@link Integer} 类型
     * */
    public static BeanMapping create(Map<Serializable, Class<?>> mapping)
    {
        var ret = new BeanMapping();

        return ret;
    }

    /**
     * 自动创建映射关系
     * */
    public static BeanMapping create(Class<?> classEntity)
    {
        var ret = new BeanMapping();

        var listField = Reflections.listAllFieldsOf(classEntity);
        var mapping = new HashMap<String, Class<?>>(listField.size());
        for(var field : listField)
        {
            field.getName();
        }

        return ret;
    }

}
