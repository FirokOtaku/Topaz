package firok.topaz.function;

/**
 * 三参数 Function
 * @since 7.5.0
 * @author Firok
 * */
@FunctionalInterface
public interface TriFunction<TypeParamA, TypeParamB, TypeParamC, TypeReturn>
{
    TypeReturn apply(TypeParamA paramA, TypeParamB paramB, TypeParamC paramC);
}
