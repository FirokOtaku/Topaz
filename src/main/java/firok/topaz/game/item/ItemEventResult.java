package firok.topaz.game.item;

/**
 * 物品事件结果
 * @since 0.1.0
 * @author Firok
 * */
public enum ItemEventResult
{
    /**
     * 判断外部调用者可以继续执行逻辑
     * */
    Allowed,

    /**
     * 判断外部调用者停止执行逻辑
     * */
    Denied,

    /**
     * 已由内部逻辑处理
     * */
    Delegated,
}
