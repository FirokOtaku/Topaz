package firok.topaz;

import firok.topaz.math.Easings;
import org.junit.jupiter.api.Test;

public class EasingsTests
{
//    @Test
    public void exportAll() throws Exception
    {
        var methods = Easings.class.getMethods();
        for(var method : methods)
        {
            System.out.println(method.getName());
        }
    }
}
