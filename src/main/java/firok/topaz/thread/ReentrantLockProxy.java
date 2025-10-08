package firok.topaz.thread;

import firok.topaz.TopazExceptions;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁代理
 * @since 6.11.0
 * @author Firok
 * */
@SuppressWarnings("ClassCanBeRecord")
public class ReentrantLockProxy implements LockProxy
{
    public final ReentrantLock locker;

    public ReentrantLockProxy()
    {
        locker = new ReentrantLock();
    }
    public ReentrantLockProxy(ReentrantLock lock)
    {
        TopazExceptions.ParamValueNoneNull.ifNull(lock);
        this.locker = lock;
    }

    @Override
    public void lock()
    {
        locker.lock();
    }

    @Override
    public void unlock()
    {
        locker.unlock();
    }
}
