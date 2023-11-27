package firok.topaz.test;

import firok.topaz.math.Maths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MathTests
{
    @Test
    public void testMaxMinMid()
    {
        // max
        Assertions.assertEquals(Integer.MAX_VALUE, Maths.max(Integer.MAX_VALUE));
        Assertions.assertEquals(100, Maths.max(1, 2, 3, 4, 100, 5));
        Assertions.assertEquals(3, Maths.max(-1, 1, 3, 2, -3));

        // min
        Assertions.assertEquals(-1, Maths.min(-1, 2, 3, 4));
        Assertions.assertEquals(-100, Maths.min(100, 20, -10, -100, 30));

        // mid
        Assertions.assertEquals(2, Maths.mid(1, 2, 3));
        Assertions.assertEquals(2, Maths.mid(1, 3, 2));
        Assertions.assertEquals(999, Maths.mid(-1, 9999, 999));
        Assertions.assertEquals(999L, Maths.mid(-999L, 9999L, 999L));
        Assertions.assertEquals((short) 0, Maths.mid((short) 0, (short) -1, (short) 1));
    }
}
