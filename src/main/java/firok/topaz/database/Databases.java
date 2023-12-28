package firok.topaz.database;

import firok.topaz.TopazExceptions;
import firok.topaz.annotation.Indev;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map;

/**
 * 数据库相关工具, 提供一些对原生 JDBC 的辅助方法
 * @since 7.8.0
 * @author Firok
 * */
public class Databases
{

    /**
     * 将一个数据库查询结果集转换为指定类型
     * */
    @Indev
    public static <TypeEntity> TypeEntity mapping(ResultSet rs, Class<TypeEntity> classEntity)
    {
        return null;
    }

    /**
     * 使用指定配置将一个数据库查询结果集转换为指定类型
     * */
    @Indev
    public static <TypeEntity> TypeEntity mapping(ResultSet rs, Class<TypeEntity> classEntity, Map<Field, Class<?>> config)
    {
        return null;
    }

    /**
     * 遍历一个数据库查询结果集的某个字段
     * */
    public static <TypeField> Iterator<TypeField> iteratorField(ResultSet rs, String fieldName, Class<TypeField> classField)
    {
        return new Iterator<>() {
            @Override
            public boolean hasNext()
            {
                try { return rs.next(); }
                catch (Exception any) { return TopazExceptions.DatabaseOperationError.occur(any); }
            }

            @Override
            public TypeField next()
            {
                try { return rs.getObject(fieldName, classField); }
                catch (Exception any) { return TopazExceptions.DatabaseOperationError.occur(any); }
            }
        };
    }

    /**
     * 遍历一个数据库查询结果集的某个字段
     * */
    public static <TypeField> Iterator<TypeField> iteratorField(ResultSet rs, int fieldIndex, Class<TypeField> classField)
    {
        return new Iterator<>() {
            @Override
            public boolean hasNext()
            {
                try { return rs.next(); }
                catch (Exception any) { return TopazExceptions.DatabaseOperationError.occur(any); }
            }

            @Override
            public TypeField next()
            {
                try { return rs.getObject(fieldIndex, classField); }
                catch (Exception any) { return TopazExceptions.DatabaseOperationError.occur(any); }
            }
        };
    }

    /**
     * 遍历一个数据库查询结果集, 并转换为指定类型
     * */
    @Indev
    public static <TypeEntity> Iterator<TypeEntity> iterator(ResultSet rs, Class<TypeEntity> classEntity)
    {
        return new Iterator<TypeEntity>() {
            @Override
            public boolean hasNext()
            {
                try { return rs.next(); }
                catch (Exception any) { return TopazExceptions.DatabaseOperationError.occur(any); }
            }

            @Override
            public TypeEntity next()
            {
                try { return mapping(rs, classEntity); }
                catch (Exception any) { return TopazExceptions.DatabaseOperationError.occur(any); }
            }
        };
    }
}
