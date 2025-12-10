package firok.topaz.annotation;

/// 指明在什么时候可以重写某个成员
/// @since 8.0.0
/// @author Firok
public enum ToBeOverriddenTiming
{
    /// 子类应当尽快重写这个成员
    AsSoonAsPossible,

    /// 子类在有需要时就可以重写这个字段
    WhenNeeded,

    /// 子类在有需要时可以重写这个字段, 但是不推荐这样做, 推荐使用其它方式实现相关特性
    WhenNeededButNotRecommended,

    /// 这个成员不允许被重写
    /// (一般是一些受限于 Java 落后的可见性管理特性而被迫暴露的内部成员)
    /// @see org.jetbrains.annotations.ApiStatus.Internal 此时语义类似于 JetBrains 实现的 @Internal 注解
    Never,
}
