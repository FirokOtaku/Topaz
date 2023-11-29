package firok.topaz.thread;

/**
 * 无锁代理
 * @since 6.11.0
 * @author Firok
 * */
public final class NoLockProxy implements LockProxy
{
    private NoLockProxy() { }

    public static NoLockProxy Instance = new NoLockProxy();

    @Override
    public void lock() { }

    @Override
    public void unlock() { }
}
