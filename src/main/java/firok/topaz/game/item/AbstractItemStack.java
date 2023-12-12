package firok.topaz.game.item;

import lombok.Getter;

/**
 * 物品摞
 * @since 6.12.0
 * @version 7.0.0
 * */
@SuppressWarnings({"rawtypes", "unchecked"})
public class AbstractItemStack<TypeItem extends AbstractItem>
{
	private final TypeItem delegate;
	@Getter
	private int count;
	public AbstractItemStack(TypeItem delegate, int count)
	{
		this.delegate = delegate;
		setCount(count);
	}
	public AbstractItemStack(TypeItem delegate)
	{
		this.delegate = delegate;
		setCount(1);
	}
	private AbstractItemStack()
	{
		this.delegate = null;
		this.count = 0;
	}

	public static final AbstractItemStack EmptyStack = new AbstractItemStack();

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
