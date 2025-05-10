package firok.topaz.integration.ebean;

import firok.topaz.function.MayFunction;
import firok.topaz.spring.Page;
import io.ebean.Database;
import io.ebean.PagedList;
import io.ebean.Transaction;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 一些使用 ebean 库过程中可能用到的工具方法
 * @apiNote 使用这个类, 需要 classpath 内有 ebean 依赖
 * @author Firok
 * @since 7.30.0
 * */
public final class Queries
{
    private Queries() { }

    /**
     * 将 ebean 接口返回的分页查询数据转换为一个可序列化的 {@link Page} 对象
     * */
    public static <TypeData extends Serializable>
    Page<TypeData> pageOf(PagedList<TypeData> page)
    {
        var ret = new Page<TypeData>();

        ret.setCountPage(page.getTotalPageCount());
        ret.setCountRecord(page.getTotalCount());
        ret.setPageIndex(page.getPageIndex());
        ret.setPageSize(page.getPageSize());
        ret.setRecords(new ArrayList<>(page.getList()));

        return ret;
    }

    /**
     * 在数据库事务里执行若干数据库操作
     * @since 7.46.0
     * */
    public static <TypeData extends Serializable>
    TypeData transaction(Database database, MayFunction<Transaction, TypeData> function)
    {
        var trans = database.beginTransaction();
        try
        {
            var ret = function.apply(trans);
            trans.commit();
            return ret;
        }
        catch (Exception any)
        {
            trans.rollback();
            throw new RuntimeException(any);
        }
        finally
        {
            trans.close();
        }
    }
}
