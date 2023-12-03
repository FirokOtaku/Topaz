package firok.topaz.game.item;

import firok.topaz.resource.RegistryItem;

/**
 * 物品稀有度基类
 * */
public abstract class ItemRarity<TypeKey> implements RegistryItem<TypeKey>
{
    /**
     * 稀有度显示色彩
     * */
    public abstract int getColor();
}
