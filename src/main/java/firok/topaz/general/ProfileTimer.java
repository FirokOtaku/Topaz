package firok.topaz.general;

import firok.topaz.annotation.Unstable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 一个简单的记录程序运行所用时间的工具
 * @since 7.4.0
 * @author Firok
 * */
public class ProfileTimer
{
    public record Checkpoint(long timestamp, String msg) { }

    private final List<Checkpoint> listCheckpoint;
    public ProfileTimer()
    {
        this("");
    }
    public ProfileTimer(String msg)
    {
        var now = System.currentTimeMillis();
        this.listCheckpoint = new LinkedList<>();
        this.listCheckpoint.add(new Checkpoint(now, msg));
    }

    /**
     * 创建一个检查点
     * @return 距离上一个检查点过去了多少毫秒
     * */
    public synchronized long check(String msg)
    {
        var now = System.currentTimeMillis();
        var cpNow = new Checkpoint(now, msg);
        var cpLast = this.listCheckpoint.getLast();
        this.listCheckpoint.add(cpNow);
        return cpNow.timestamp - cpLast.timestamp;
    }

    /**
     * 查询当前所有检查点数据
     * */
    public synchronized List<Checkpoint> snapshot()
    {
        return new ArrayList<>(this.listCheckpoint);
    }

    @Unstable
    @Override
    public synchronized String toString()
    {
        return "ProfileTimer [" + this.listCheckpoint.size() + "]";
    }
}
