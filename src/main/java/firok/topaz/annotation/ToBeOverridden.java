package firok.topaz.annotation;

import java.lang.annotation.*;

/// 声明基类中某些成员在满足一定条件时应当在子类中被重写.
///
/// 这个注解一般不被标注在抽象方法上, 因为抽象方法默认就必须要在子类中被实现;
/// 一般将这个注解标注给以方法形式暴露给子类用于调节父类特定内部运行逻辑的成员,
/// 这常见于享元模式 (Flyweight) 设计.
///
/// @since 8.0.0
/// @author Firok
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface ToBeOverridden
{
    /**
     * 指明应当在什么场景重写
     * */
    ToBeOverriddenTiming value() default ToBeOverriddenTiming.WhenNeeded;

    /**
     * 可选描述信息
     * */
    String[] desc() default {};
}
