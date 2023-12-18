package firok.topaz.test;

import firok.topaz.function.Logics;
import firok.topaz.function.MaySupplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LogicsTests
{
    @Test
    void testAnd() throws Exception
    {
        var funMayTrue = MaySupplier.that(() -> true);
        var funMayFalse = MaySupplier.that(() -> false);
        var funTrue = funMayTrue.anyway();
        var funfalse = funMayFalse.anyway();

        Assertions.assertTrue(Logics.And(funMayTrue));
        Assertions.assertTrue(Logics.And(funTrue));
        Assertions.assertTrue(Logics.And(funTrue, funTrue, funTrue));

        Assertions.assertFalse(Logics.And(funMayFalse));
        Assertions.assertFalse(Logics.And(funfalse));
        Assertions.assertFalse(Logics.And(funfalse, funfalse, funfalse));
        Assertions.assertFalse(Logics.And(funTrue, funTrue, funfalse));
    }
}
