package firok.topaz.test;

import firok.topaz.thread.LockCompound;
import firok.topaz.thread.Threads;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class LockCompoundTests
{
    @Test
    public void test()
    {
        var lock1 = new ReentrantLock();
        var lock2 = new ReentrantLock();

        var ret = new ArrayList<Integer>();

        Threads.start(false, () -> {
            try(var ignored = new LockCompound(lock1))
            {
                Threads.sleep(1000);
                ret.add(1);
            }
        });
        Threads.start(false, () -> {
            Threads.sleep(300);
            try(var ignored = new LockCompound(lock1))
            {
                Threads.sleep(300);
                ret.add(2);
            }
        });
        Threads.start(false, () -> {
            Threads.sleep(400);
            try(var ignored = new LockCompound(lock1, lock2))
            {
                Threads.sleep(600);
                ret.add(3);
            }
        });

        Threads.sleep(2500);
        Assertions.assertEquals(1, ret.get(0));
        Assertions.assertEquals(2, ret.get(1));
        Assertions.assertEquals(3, ret.get(2));
        Assertions.assertEquals(3, ret.size());
    }
}
