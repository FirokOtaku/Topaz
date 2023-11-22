package firok.topaz.general;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * 程序的元数据
 * @version 6.7.0
 * @since 6.7.0
 * @apiNote 请注意, 这个类的字段和构造方法可能会 <b>频繁改变</b>, 但是 <b>不会</b> 在 Topaz 主版本号里表现.
 *          如果你使用了这个类, 请留意所用版本号和其字段列表.
 *          一般来说字段只会新增, 而不会删除或改名, 所以仅从这个类的实例读取数据应当是安全的.
 * @author Firok
 * */
public class ProgramMeta
{
    /**
     * 简短 id
     * */
    @NotNull
    public final String id;

    /**
     * 程序名称
     * */
    @NotNull
    public final String name;

    /**
     * 版本号
     * */
    @NotNull
    public final Version version;

    /**
     * 描述信息
     * */
    @NotNull
    public final String desc;

    /**
     * 作者
     * */
    @NotNull
    public final List<String> authors;

    /**
     * repo 链接
     * */
    @NotNull
    public final List<String> linkRepos;

    /**
     * 主页链接
     * */
    @NotNull
    public final List<String> linkHomepages;

    @NotNull
    public final String license;

    public ProgramMeta(
            @NotNull String id,
            @NotNull String name,
            @NotNull Version version,
            @NotNull String desc,
            @NotNull List<String> authors,
            @NotNull List<String> linkRepos,
            @NotNull List<String> listHomepages,
            @NotNull String license
    )
    {
        this.id = id;
        this.name = name;
        this.version = version;
        this.desc = desc;
        this.authors = Collections.unmodifiableList(authors); // todo low 可能会改其它实现
        this.linkRepos = Collections.unmodifiableList(linkRepos);
        this.linkHomepages = Collections.unmodifiableList(listHomepages);
        this.license = license;
    }
}
