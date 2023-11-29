package firok.topaz.thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁代理
 * @since 6.11.0
 * @author Firok
 * */
public class ReentrantLockProxy implements LockProxy
{
    public final ReentrantLock locker = new ReentrantLock();

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
