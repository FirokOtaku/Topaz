package firok.topaz.database;

import firok.topaz.TopazExceptions;
import firok.topaz.annotation.PerformanceIssue;
import firok.topaz.annotation.Resource;
import firok.topaz.function.MustCloseable;
import firok.topaz.general.Collections;
import firok.topaz.reflection.Reflections;
import org.intellij.lang.annotations.Language;

import java.io.Closeable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static firok.topaz.general.Collections.sizeOf;

/**
 * 数据库相关工具, 提供一些对原生 JDBC 的辅助方法
 * @since 7.8.0
 * @author Firok
 * */
@SuppressWarnings("SqlSourceToSinkFlow")
public class Databases
{
    private static final Map<Class<?>, Map<String, Field>> cachedClassFields = new HashMap<>();
    private static Map<String, Field> loadClassFields(Class<?> classAny)
    {
        synchronized (cachedClassFields)
        {
            if(cachedClassFields.containsKey(classAny))
            {
                return cachedClassFields.get(classAny);
            }
            else
            {
                var fields = Reflections.listAllFieldsOf(classAny);
                var mapFields = new HashMap<String, Field>(fields.size());
                for(var field : fields)
                {
                    mapFields.put(field.getName(), field);
                }
                cachedClassFields.put(classAny, mapFields);
                return mapFields;
            }
        }
    }

    /**
     * 将一个数据库查询结果集转换为指定类型
     * @param rs 要操作的结果集. 这个方法不会关闭结果集
     * @since 7.9.0
     * */
    public static <TypeEntity> TypeEntity mapping(
            ResultSet rs,
            Class<TypeEntity> classEntity,
            Function<String, String> funColumnName2FieldName
    )
    {
        var mapColumnName2FieldName = new HashMap<String, String>();
        try
        {
            var meta = rs.getMetaData();
            var countColumn = meta.getColumnCount();
            for(var step = 1; step <= countColumn; step++)
            {
                var nameColumn = meta.getColumnName(step);
                var nameField = funColumnName2FieldName.apply(nameColumn);
                mapColumnName2FieldName.put(nameColumn, nameField);
            }
        }
        catch (Exception any)
        {
            return TopazExceptions.DatabaseOperationError.occur(any);
        }

        return mapping(rs, classEntity, mapColumnName2FieldName);
    }

    /**
     * 将一个数据库查询结果集转换为指定类型
     * @param rs 要操作的结果集. 这个方法不会关闭结果集
     * @since 7.9.0
     * */
    public static <TypeEntity> TypeEntity mapping(
            ResultSet rs,
            Class<TypeEntity> classEntity,
            HashMap<String, String> mapColumnName2FieldName
    )
    {
        var mapField = loadClassFields(classEntity);
        TypeEntity ret;
        try
        {
            ret = classEntity.getConstructor().newInstance();
        }
        catch (Exception any)
        {
            return TopazExceptions.BeanInstantiationError.occur(any);
        }
        try
        {
            var meta = rs.getMetaData();
            var countColumn = meta.getColumnCount();
            for(var step = 1; step <= countColumn; step++)
            {
                var nameColumn = meta.getColumnName(step);
                var nameField = mapColumnName2FieldName.get(nameColumn);
                var field = mapField.get(nameField);
                if(field == null) continue;

                var classField = field.getType();
                var value = rs.getObject(step, classField);

                var acc = field.canAccess(ret);
                if(!acc) field.setAccessible(true);
                field.set(ret, value);
                if(!acc) field.setAccessible(false);
            }

            return ret;
        }
        catch (Exception any)
        {
            return TopazExceptions.DatabaseOperationError.occur(any);
        }
    }

    /**
     * 遍历一个数据库查询结果集的某个字段
     * */
    @SuppressWarnings("DuplicatedCode")
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
     * @since 7.9.0
     * */
    public static <TypeField> List<TypeField> collectField(ResultSet rs, String fieldName, Class<TypeField> classField)
    {
        return Collections.collect(iteratorField(rs, fieldName, classField));
    }

    /**
     * 遍历一个数据库查询结果集的某个字段
     * */
    @SuppressWarnings("DuplicatedCode")
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
     * 遍历一个数据库查询结果集的某个字段
     * @since 7.9.0
     * */
    public static <TypeField> List<TypeField> collectField(ResultSet rs, int fieldIndex, Class<TypeField> classField)
    {
        return Collections.collect(iteratorField(rs, fieldIndex, classField));
    }

    /**
     * 遍历一个数据库查询结果集, 并转换为指定类型
     * */
    public static <TypeEntity> Iterator<TypeEntity> iterator(
            ResultSet rs,
            Class<TypeEntity> classEntity,
            Function<String, String> funColumnName2FieldName
    )
    {
        return new Iterator<>() {
            @Override
            public boolean hasNext()
            {
                try { return rs.next(); }
                catch (Exception any) { return TopazExceptions.DatabaseOperationError.occur(any); }
            }

            @Override
            public TypeEntity next()
            {
                try { return mapping(rs, classEntity, funColumnName2FieldName); }
                catch (Exception any) { return TopazExceptions.DatabaseOperationError.occur(any); }
            }
        };
    }

    /**
     * 遍历一个数据库查询结果集, 并转换为指定类型
     * @since 7.9.0
     * */
    public static <TypeEntity> List<TypeEntity> collect(
            ResultSet rs,
            Class<TypeEntity> classEntity,
            Function<String, String> funColumnName2FieldName
    )
    {
        return Collections.collect(iterator(rs, classEntity, funColumnName2FieldName));
    }

    /**
     * 一次查询操作的上下文
     * */
    public record QueryContext(PreparedStatement stmt, ResultSet rs) implements MustCloseable
    {
        @Override
        public void close()
        {
            try { stmt.close(); } catch (Exception ignored) { }
            try { rs.close(); } catch (Exception ignored) { }
        }
    }

    /**
     * 执行查询
     * @since 7.9.0
     * */
    @PerformanceIssue(cost = Resource.Cpu, value = "每次执行这个方法都会实例化一个新的 PreparedStatement 对象")
    public static QueryContext executeQuery(
            Connection conn,
            @Language("SQL")
            String sql,
            Object... params
    )
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            stmt = conn.prepareStatement(sql);
            var count = sizeOf(params);
            for(var step = 0; step < count; step++)
            {
                stmt.setObject(step + 1, params[step]);
            }
            rs = stmt.executeQuery();
            return new QueryContext(stmt, rs);
        }
        catch (Exception any)
        {
            if(stmt != null) try { stmt.close(); } catch (Exception ignored) { }
            if(rs != null) try { rs.close(); } catch (Exception ignored) { }
            return TopazExceptions.DatabaseOperationError.occur(any);
        }
    }

    /**
     * 一次更新操作的上下文
     * */
    public record UpdateContext(PreparedStatement stmt, int count) implements Closeable
    {
        @Override
        public void close()
        {
            try { stmt.close(); } catch (Exception ignored) { }
        }
    }

    /**
     * 执行更新
     * @since 7.9.0
     * */
    @PerformanceIssue(cost = Resource.Cpu, value = "每次执行这个方法都会实例化一个新的 PreparedStatement 对象")
    public static UpdateContext executeUpdate(
            Connection conn,
            @Language("SQL")
            String sql,
            Object... params
    )
    {
        PreparedStatement stmt = null;
        try
        {
            stmt = conn.prepareStatement(sql);
            var count = sizeOf(params);
            for(var step = 0; step < count; step++)
            {
                stmt.setObject(step + 1, params[step]);
            }
            var countUpdate = stmt.executeUpdate();
            return new UpdateContext(stmt, countUpdate);
        }
        catch (Exception any)
        {
            if(stmt != null) try { stmt.close(); } catch (Exception ignored) { }
            return TopazExceptions.DatabaseOperationError.occur(any);
        }
    }
}
