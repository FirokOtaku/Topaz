package firok.topaz.game.item;

import firok.topaz.resource.RegistryItem;

/**
 * 物品稀有度基类
 * @since 6.12.0
 * @version 7.0.0
 * */
public abstract class AbstractItemRarity<TypeKey> implements RegistryItem<TypeKey>
{
    /**
     * 稀有度显示色彩
     * */
    public abstract int getColor();
}
