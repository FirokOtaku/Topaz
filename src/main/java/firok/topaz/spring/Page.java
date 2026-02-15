package firok.topaz.spring;

import firok.topaz.integration.ebean.Queries;
import firok.topaz.internal.SerializableInfo;
import io.ebean.PagedList;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/// 查询分页数据对象
/// @param <TypeData> 记录数据类型. 需要注意的是, 为了使得 {@link Page} 对象完全可序列化, 该类型也需要是可序列化的
/// @apiNote 页码编号从 0 开始
/// @see Queries#pageOf(PagedList) EBean 相关支持
/// @author Firok
/// @since 7.30.0
@Data
@ToString
public class Page<TypeData extends Serializable> implements Serializable
{
    @Serial
    private static final long serialVersionUID = SerializableInfo.SIDBase + 30000 + 1;

    /**
     * 总页码数
     * */
    private int countPage;
    /**
     * 总记录数
     * */
    private int countRecord;
    /**
     * 当前页码数
     * */
    private int pageIndex;
    /**
     * 页大小
     * */
    private int pageSize;
    /**
     * 记录列表
     * */
    private List<TypeData> records;

    /**
     * @return 是否有下一页
     * */
    public boolean hasNextPage()
    {
        return pageIndex < countPage;
    }
    /**
     * @return 是否有上一页
     * */
    public boolean hasPrevPage()
    {
        return pageIndex > 1;
    }

    /**
     * 创建一个空分页, 首页页码从 0 开始
     * @since 7.44.0
     * */
    public static <TypeData extends Serializable>
    Page<TypeData> emptyOf0()
    {
        var ret = new Page<TypeData>();

        ret.setCountPage(0);
        ret.setCountRecord(0);
        ret.setPageIndex(0);
        ret.setPageSize(0);
        ret.setRecords(new ArrayList<>());

        return ret;
    }

    /**
     * 创建一个空分页, 首页页码从 1 开始
     * @since 7.44.0
     * */
    public static <TypeData extends Serializable>
    Page<TypeData> emptyOf1()
    {
        var ret = new Page<TypeData>();

        ret.setCountPage(0);
        ret.setCountRecord(0);
        ret.setPageIndex(1);
        ret.setPageSize(0);
        ret.setRecords(new ArrayList<>());

        return ret;
    }
}
