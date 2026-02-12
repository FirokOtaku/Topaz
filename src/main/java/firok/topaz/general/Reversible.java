package firok.topaz.general;

import firok.topaz.reflection.Reflections;

/// 标明某个类可以进行反转操作
///
/// @author Firok
/// @since 8.0.0
public interface Reversible
{
    /// 进行反转操作
    ///
    /// @implNote 这个
    void makeReverse();

    /// 获得一个对象的反转对象
    ///
    /// @param entity 要反转的对象
    /// @param <TypeEntity> 请注意, 这个对象的类型需要符合 [Reflections#cloneOf(java.lang.Cloneable)] 的要求, 否则执行此方法会抛出异常
    /// @throws CodeException 如果复制对象过程发生错误, 将会抛出此异常
    /// @see Reflections#cloneOf(java.lang.Cloneable)
    /// @since 8.0.0
    static <TypeEntity extends Reversible & Cloneable>
    TypeEntity reverseOf(TypeEntity entity)
    throws CodeException
    {
        var ret = Reflections.cloneOf(entity);
        ret.makeReverse();
        return ret;
    }
}
