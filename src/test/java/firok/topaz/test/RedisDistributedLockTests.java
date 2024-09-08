package firok.topaz.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import firok.topaz.integration.redis.DistributedLockChannel;
import firok.topaz.thread.Threads;
import org.junit.jupiter.api.Assertions;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.util.Random;

/**
 * 测试分布式锁的效果.
 * 创建若干个线程, 分别在相同的通道上获取锁并模拟执行一些耗时操作.
 * */
public class RedisDistributedLockTests
{
    record RedisConfig(
            String host,
            int port,
            String username,
            String password
    ) {
        JedisPool buildPool()
        {
            if(username != null && password != null)
                return new JedisPool(host, port, username, password);
            else
                return new JedisPool(host, port);
        }
    }

    private static final String ChannelKey = "distributed-lock-test-channel";

    public static void main(String... args) throws Exception
    {
        var fileConfigRedis = new File("./redis-test.json");
        if(!fileConfigRedis.exists())
        {
            System.out.println("Redis 配置文件 (./redis-test.json) 不存在, 分布式锁单元测试已跳过");
            System.out.println(fileConfigRedis.getCanonicalPath());
            return;
        }

        var om = new ObjectMapper();
        var config = om.readValue(fileConfigRedis, RedisConfig.class);

        final var rand = new Random();
        for(var stepThread = 0; stepThread < 3; stepThread++)
        {
            final String threadName = "T-" + stepThread;

            var threadSingle = new Thread(() -> {
                System.out.println("线程 [" + threadName + "] 开始执行");

                try(var pool = config.buildPool();
                    var channel = new DistributedLockChannel(pool, ChannelKey))
                {
                    System.out.println(threadName + " | channel created");

                    var wait1 = 2000 + rand.nextInt(1000);
                    try(var ignored = channel.lock(30_000, 5000))
                    {
                        System.out.println(threadName + " | step 1 start | " + wait1);
                        Threads.sleep(wait1);
                        System.out.println(threadName + " | step 1 end | " + wait1);
                    }

                    Threads.sleep(500 + rand.nextInt(500));

                    var wait2 = 2000 + rand.nextInt(1000);
                    try(var ignored = channel.lock(30_000, 5000))
                    {
                        System.out.println(threadName + " | step 2 start | " + wait2);
                        Threads.sleep(wait2);
                        System.out.println(threadName + " | step 2 end | " + wait2);
                    }

                    Threads.sleep(500 + rand.nextInt(500));

                    var wait3 = 2000 + rand.nextInt(1000);
                    try(var ignored = channel.lock(30_000, 5000))
                    {
                        System.out.println(threadName + " | step 3 start | " + wait3);
                        Threads.sleep(wait3);
                        System.out.println(threadName + " | step 3 end | " + wait3);
                    }

                    System.out.println(threadName + " | channel closed");
                }
                catch (Exception any)
                {
                    Assertions.fail("测试发生未知错误", any);
                }
            }) {{
                setName(threadName);
            }};
            threadSingle.start();

        }

    }
}
