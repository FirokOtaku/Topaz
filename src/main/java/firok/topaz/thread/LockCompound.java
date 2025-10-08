package firok.topaz.thread;

import firok.topaz.TopazExceptions;
import firok.topaz.annotation.NotThreadSafe;
import firok.topaz.function.MustCloseable;
import firok.topaz.general.Collections;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

/// 利用 Java 的 try-with-resources 语法,
/// 一次性可加锁多个锁来源,
/// 省去每次需要在 finally 里面写 unlock() 的麻烦.
///
/// 这个类目前面向各个模块内部的本地代码使用,
/// 用于快速对多个锁来源加锁,
/// 没有考虑创建后作为静态变量在多个模块和线程间使用的情况,
/// 所以这个类 **不是** 线程安全的,
/// 也 **没有** 记录当前锁状态 (是否已经加锁),
/// 对 {@link #lock} 和 {@link #close} 的调用会直接转发给内部保存的所有锁代理,
/// 所以重复调用可能会触发异常.
///
/// ----
///
/// ```java
/// try(var ignored = new LockCompound(lock1, lock2, lock3))
/// {
///     // anything
/// }
/// ```
///
/// @since 6.2.0
/// @version 8.0.0
/// @author Firok
/// @see ReentrantLock
/// @see LockProxy
@NotThreadSafe
public class LockCompound implements MustCloseable
{
    private final LockProxy[] locks;

    private LockCompound(Class<?> ignored, boolean lockNow, Object[] locks)
    {
        TopazExceptions.ParamValueNoneNull.ifNull(locks);
        var count = Collections.sizeOf(locks);
        this.locks = new LockProxy[count];
        for(var step = 0; step < count; step++)
        {
            var lock = locks[step];
            TopazExceptions.ParamValueNoneNull.ifNull(lock);
            this.locks[step] = switch(lock) {
                case LockProxy proxy -> proxy;
                case ReentrantLock instance -> new ReentrantLockProxy(instance);
                default -> null; // make compiler happy
            };
        }

        if(lockNow)
        {
            this.lock();
        }
    }

    /// @param lockNow 是否在构造时自动加锁
    /// @param locks 锁数组. 数组引用和数组元素均 **不可为 null**
    /// @since 8.0.0
    public LockCompound(boolean lockNow, @NotNull ReentrantLock...locks)
    {
        this(ReentrantLock.class, lockNow, locks);
    }
    /// @apiNote 此接口提供了对此前 `ReentrantLockCompound` 使用方式的继承.
    /// @param locks 锁数组. 数组引用和数组元素均 **不可为 null**
    public LockCompound(@NotNull ReentrantLock... locks)
    {
        this(true, locks);
    }
    /// @param lockNow 是否在构造时自动加锁
    /// @param locks 锁数组. 数组引用和数组元素均 **不可为 null**
    /// @since 8.0.0
    public LockCompound(boolean lockNow, @NotNull LockProxy...locks)
    {
        this(LockProxy.class, lockNow, locks);
    }
    /// @param locks 锁数组. 数组引用和数组元素均 **不可为 null**
    /// @since 8.0.0
    public LockCompound(@NotNull LockProxy... locks)
    {
        this(true, locks);
    }

    /// 手动加锁
    /// @since 8.0.0
    public void lock()
    {
        for(var lock : this.locks)
        {
            lock.lock();
        }
    }

    /// 释放所有锁.
    /// 如果是通过 {@link LockCompound(false, LockProxy[])} 创建的, 需要先 **确保** 已经手动调用过 {@link #lock()} 方法;
    /// 如果使用 try-with-resources 语法就 **不要** 手动调用此方法, JVM 会保证调用这个方法, 对所有锁解锁.
    /// 如果不使用 try-with-resources 语法, **需要** 手动调用此方法.
    @Override
    public void close()
    {
        for(var lock : this.locks)
        {
            lock.unlock();
        }
    }
}
