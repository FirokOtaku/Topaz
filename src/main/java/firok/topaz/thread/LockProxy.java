package firok.topaz.thread;

/**
 * 执行脚本时的锁代理
 * @see NoLockProxy 无锁实现
 * @see ReentrantLockProxy 可重入实现
 * @since 6.11.0
 * @author Firok
 * */
public interface LockProxy
{
    /**
     * 加锁
     * @implNote 不能抛出异常
     * */
    void lock();

    /**
     * 解锁
     * @implNote 不能抛出异常
     * */
    void unlock();
}
