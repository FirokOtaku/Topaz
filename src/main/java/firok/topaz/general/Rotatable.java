package firok.topaz.general;

import firok.topaz.reflection.Reflections;

/// 标明某个类可以进行旋转操作
///
/// @author Firok
/// @since 8.0.0
public interface Rotatable<TypeAngle>
{
    /// 进行旋转操作
    /// @param angle 旋转角度
    void makeRotate(TypeAngle angle);

    /// 获得一个对象的旋转对象
    ///
    /// @param entity 要旋转的对象
    /// @param angle 旋转角度
    /// @param <TypeEntity> 请注意, 这个对象的类型需要符合 [Reflections#cloneOf(java.lang.Cloneable)] 的要求, 否则执行此方法会抛出异常
    /// @throws CodeException 如果复制对象过程发生错误, 将会抛出此异常
    /// @see Reflections#cloneOf(java.lang.Cloneable)
    /// @since 8.0.0
    static <TypeEntity extends Rotatable<TypeAngle> & Cloneable, TypeAngle>
    TypeEntity rotationOf(TypeEntity entity, TypeAngle angle)
            throws CodeException
    {
        var ret = Reflections.cloneOf(entity);
        ret.makeRotate(angle);
        return ret;
    }
}
