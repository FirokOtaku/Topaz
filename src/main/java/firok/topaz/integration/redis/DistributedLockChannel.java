package firok.topaz.integration.redis;

import firok.topaz.TopazExceptions;
import firok.topaz.thread.Threads;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * 基于 Redis 的分布式锁实现.
 * 当前实现 <b>不是</b> 公平锁.
 * @see DistributedLock
 * @apiNote 使用此类, 需要 classpath 内有 jedis 依赖
 * @since 7.29.0
 * @author Firok
 * */
public class DistributedLockChannel implements AutoCloseable
{
    private final JedisPool pool;
    private final String channelKey;
    /**
     * @param pool Redis 连接池
     * @param channelKey 通道键. 在同一个通道上的锁会互斥
     * */
    public DistributedLockChannel(JedisPool pool, String channelKey)
    {
        this.pool = pool;
        this.channelKey = channelKey;
    }
    /**
     * @param host Redis 服务器地址
     * @param port Redis 服务器端口
     * @param channelKey 通道键. 在同一个通道上的锁会互斥
     * */
    public DistributedLockChannel(String host, int port, String channelKey)
    {
        this(new JedisPool(host, port), channelKey);
    }
    /**
     * @param host Redis 服务器地址
     * @param port Redis 服务器端口
     * @param username Redis 服务器用户名
     * @param password Redis 服务器密码
     * @param channelKey 通道键. 在同一个通道上的锁会互斥
     * */
    public DistributedLockChannel(String host, int port, String username, String password, String channelKey)
    {
        this(new JedisPool(host, port, username, password), channelKey);
    }

    private boolean closed = false;
    private void checkClosed()
    {
        synchronized (this)
        {
            if(closed) throw new RuntimeException("分布式锁通道已关闭");
        }
    }

    public String channelKey()
    {
        return channelKey;
    }

    /**
     * 轮询 redis 数据库的间隔时间
     * */
    private int queryInterval = 50;
    public int queryInterval()
    {
        synchronized (this)
        {
            return queryInterval;
        }
    }
    public DistributedLockChannel queryInterval(int queryInterval)
    {
        synchronized (this)
        {
            this.queryInterval = queryInterval;
        }
        return this;
    }

    DistributedLock currentLock;
    /**
     * 尝试获取一个分布式锁
     * @param tryTime 尝试时间. 如果超过指定这个时间还没获取到锁则抛出异常
     * @param expireTime 锁的过期时间
     * */
    public DistributedLock lock(long tryTime, long expireTime)
    {
        TopazExceptions.ParamValueOutOfRange.ifLesserOrEqual(tryTime, 0L);
        TopazExceptions.ParamValueOutOfRange.ifLesserOrEqual(expireTime, 0L);

        checkClosed();

        final long startTime = System.currentTimeMillis();

        try(var conn = pool.getResource())
        {
            var uuid = UUID.randomUUID();
            var uuidString = uuid.toString();
            while(true)
            {
                synchronized (this)
                {
                    var result = conn.set(channelKey, uuidString, SetParams.setParams().nx().ex(expireTime));
                    if("OK".equals(result))
                    {
                        var ret = new DistributedLock(this, uuid, channelKey);
                        currentLock = ret;
                        return ret;
                    }
                }

                final long nowTime = System.currentTimeMillis();
                if(nowTime - startTime > tryTime)
                {
                    throw new TimeoutException("获取分布式锁超时");
                }

                Threads.sleep(queryInterval);
            }
        }
        catch (Exception any)
        {
            return TopazExceptions.DatabaseOperationError.occur(new RuntimeException("获取分布式锁失败", any));
        }
    }

    /**
     * 释放一个分布式锁
     * @see DistributedLock#close
     * */
    void unlock(DistributedLock lock)
    {
        checkClosed();

        synchronized (this)
        {
            if(currentLock != lock) return;

            try(var conn = pool.getResource())
            {
                var value = conn.get(lock.key());
                if(value != null && value.equals(lock.uuid().toString()))
                {
                    conn.del(lock.key());
                }

                currentLock = null;
            }
            catch (Exception any)
            {
                // todo 这里以后可能增加一些判断, 如果锁不是自己加的, 也许有必要抛出异常
                TopazExceptions.DatabaseOperationError.occur(new Exception("释放分布式锁失败", any));
            }
        }
    }

    /**
     * 关闭此通道.
     * 通道关闭后, 再调用 {@link #lock} 方法和 {@link DistributedLock#close} 方法会抛出异常
     * */
    @Override
    public void close()
    {
        checkClosed();
        synchronized (this)
        {
            if(currentLock != null)
                unlock(currentLock);
            pool.close();
            closed = true;
        }
    }
}
