package firok.topaz.function;

/**
 * @since 4.1.0
 * @author Firok
 * */
public interface MayConsumer<Type>
{
	void accept(Type param) throws Exception;
}
