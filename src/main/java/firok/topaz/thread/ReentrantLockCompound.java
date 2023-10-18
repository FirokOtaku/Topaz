package firok.topaz.thread;

import firok.topaz.function.MustCloseable;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 利用 Java 的 try-with-resources 语法,
 * 一次性可对多个 ReentrantLock 加锁,
 * 省去每次需要在 finally 里面写 unlock() 的麻烦
 *
 * <hr/>
 *
 * <code lang="java">
 *     try(var ignored = new LockCompound(lock1, lock2, lock3))
 *     {
 *         // anything
 *     }
 * </code>
 *
 * @since 6.2.0
 * @author Firok
 * @see ReentrantLock
 * */
public class ReentrantLockCompound implements MustCloseable
{
    private final ReentrantLock[] locks;

    /**
     * 开始组合多个锁
     * @apiNote 这个类在实例化的时候就会对所有锁加锁, 目标代码场景就是要在 try-with-resources 语句内实例化的
     * */
    public ReentrantLockCompound(ReentrantLock... locks)
    {
        Objects.requireNonNull(locks);
        this.locks = Arrays.copyOf(locks, locks.length);

        for(var lock : locks)
        {
            lock.lock();
        }
    }

    /**
     * @apiNote 如果使用 try-with-resources 语法就不需要手动调用, VM 会保证调用这个方法, 对所有锁解锁
     * */
    @Override
    public void close()
    {
        for(var lock : locks)
        {
            lock.unlock();
        }
    }
}
