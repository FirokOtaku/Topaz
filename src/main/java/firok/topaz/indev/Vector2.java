package firok.topaz.indev;

import firok.topaz.TopazExceptions;
import firok.topaz.math.Maths;

public record Vector2<Type extends Number>(Type x, Type y)
{
    public Vector2
    {
        TopazExceptions.ParamValueNoneNull.maybe(x == null || y == null);
        assert x != null && y != null;
    }

//    public Vector2<Double> toDouble()
//    {
//        return new Vector2<>(
//                x.doubleValue(),
//                y.doubleValue()
//        );
//    }

    public Vector2<Type> unit()
    {
        double dx = x.doubleValue(), dy = y.doubleValue();
        if(dx == 0 && dy == 0) return this;

        var magnitude = Math.sqrt(dx * dx + dy * dy);

        return new Vector2<>(
                (Type) Maths.parseDouble(String.valueOf(dx / magnitude)),
                (Type) Maths.parseDouble(String.valueOf(dy / magnitude))
        );
    }
}
