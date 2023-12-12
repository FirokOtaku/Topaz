package firok.topaz.game.item;

import firok.topaz.resource.RegistryItem;
import org.jetbrains.annotations.NotNull;

/**
 * 物品基类
 * @param <TypeEntity> 调用此库时, 相关的实体基类类型
 * @since 6.12.0
 * @author Firok
 * @version 7.0.0
 * */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public abstract class AbstractItem<
        TypeStack extends AbstractItemStack,
        TypeRarity extends AbstractItemRarity,
        TypeSlot extends AbstractItemSlot,
        TypeEntity,
        TypeKey
> implements RegistryItem<TypeKey>
{
    /**
     * 最大堆叠上限
     * */
    public abstract int getMaxStackSize(TypeStack stack);

    /**
     * 物品稀有度
     * */
    public abstract TypeRarity getRarity(TypeStack stack);

    /**
     * 是否可被捡拾
     * */
    public abstract boolean isPickable(TypeEntity entity, TypeStack stack);

    /**
     * 当被捡拾
     * */
    public abstract @NotNull ItemEventResult onPick(TypeEntity entity, TypeStack stack);

    /**
     * 是否可被合成
     * @param source 合成来源, 可能是实体或其它类
     * @implNote 合成之前还没有相关的 ItemStack 实例
     * */
    public abstract boolean isCraftable(Object source);

    /**
     * 当被合成
     * @param source 合成来源, 可能是实体或其它类
     * */
    public abstract @NotNull ItemEventResult onCraft(Object source, TypeStack stack);

    /**
     * 是否可被丢弃
     * */
    public abstract boolean isDroppable(TypeEntity entity, TypeStack stack);

    /**
     * 当被丢弃
     * */
    public abstract @NotNull ItemEventResult onDrop(TypeEntity entity, TypeStack stack);

    /**
     * 是否可被使用
     * */
    public abstract boolean isUsable(TypeEntity entity, TypeStack stack);

    /**
     * 当被使用
     * */
    public abstract @NotNull ItemEventResult onUse(TypeEntity entity, TypeStack stack);

    /**
     * 是否可装备于指定物品槽
     * */
    public abstract boolean isEquipable(TypeEntity entity, TypeSlot slot, TypeStack stack);

    /**
     * 当被装备于指定物品槽
     * */
    public abstract @NotNull ItemEventResult onEquip(TypeEntity entity, TypeSlot slot, TypeStack stack);

    /**
     * 是否可被脱卸于指定物品槽
     * */
    public abstract boolean isDeequipable(TypeEntity entity, TypeSlot slot, TypeStack stack);

    /**
     * 当被脱卸于指定物品槽
     * */
    public abstract @NotNull ItemEventResult onDeequip(TypeEntity entity, TypeSlot slot, TypeStack stack);

    /**
     * 是否参与游戏刻更新
     * */
    public abstract boolean isTickable(TypeEntity entity, TypeSlot slot, TypeStack stack);

    /**
     * 当被游戏刻更新
     * */
    public abstract void onTick(TypeEntity entity, TypeSlot slot, TypeStack stack);
}
