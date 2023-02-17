package firok.topaz.thread;

/**
 * 用来给 lambda 内操作外部数据用
 *
 * @since 3.20.0
 * @author Firok
 * */
public class Ref<TypeEntry>
{
	public TypeEntry entry;

	public Ref() { }
	public Ref(TypeEntry entry) { this.entry = entry; }
}
