package firok.topaz.game.item;

import lombok.Getter;

/**
 * 物品摞
 * @deprecated 在 7.0.0 中将会被重命名为 AbstractItemStack
 * */
@Deprecated(forRemoval = true)
@SuppressWarnings({"rawtypes", "unchecked"})
public class ItemStack<TypeItem extends Item>
{
	private final TypeItem delegate;
	@Getter
	private int count;
	public ItemStack(TypeItem delegate, int count)
	{
		this.delegate = delegate;
		setCount(count);
	}
	public ItemStack(TypeItem delegate)
	{
		this.delegate = delegate;
		setCount(1);
	}
	private ItemStack()
	{
		this.delegate = null;
		this.count = 0;
	}

	public static final ItemStack EmptyStack = new ItemStack();

	public TypeItem getItem()
	{
		return delegate;
	}

	@SuppressWarnings("ManualMinMaxCalculation")
	public void setCount(int count)
	{
		var maxStackSize = delegate.getMaxStackSize(this);
		if(count <= 0) this.count = 0;
		else if(count >= maxStackSize) this.count = maxStackSize;
		else this.count = count;
	}

	public boolean isEmpty()
	{
		return this.delegate == null || this.count <= 0;
	}

	public boolean isFull()
	{
		return this.delegate != null && this.count >= delegate.getMaxStackSize(this);
	}
}
