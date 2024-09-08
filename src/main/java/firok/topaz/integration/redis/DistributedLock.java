package firok.topaz.integration.redis;

import firok.topaz.function.MustCloseable;

import java.util.UUID;

/**
 * 一个获取到的分布式锁实例.
 * @apiNote 使用此类, 需要 classpath 内有 jedis 依赖;
 *          尽量使用 try-with-resources 语法来使用, 避免忘记解锁
 * @see DistributedLockChannel
 * @since 7.29.0
 * @author Firok
 * */
public record DistributedLock(
        DistributedLockChannel channel,
        UUID uuid,
        String key
) implements MustCloseable
{
    @Override
    public void close()
    {
        this.channel.unlock(this);
    }
}
