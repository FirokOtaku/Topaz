package firok.topaz.function;

/**
 * 一个表示必须能够无痛关闭的接口, 也就是不会抛出异常的 {@link AutoCloseable}
 * @since 5.7.0
 * @author Firok
 * @see AutoCloseable
 * */
@SuppressWarnings("DeprecatedIsStillUsed")
@FunctionalInterface
public interface MustCloseable extends AutoCloseable
{
    @Override
    void close();

    /**
     * 直接吞掉内部错误, you lose!
     * @see MayRunnable#anyway()
     * */
    static MustCloseable whatever(final AutoCloseable target)
    {
        return whatever(false, target);
    }

    /**
     * @deprecated 对外部调用者: 你都用这个接口了, 为什么还要用这个?
     * @see MayRunnable#anyway(boolean)
     * */
    @Deprecated
    static MustCloseable whatever(final boolean throwInternalException, final AutoCloseable target)
    {
        return () -> {
            try { target.close(); }
            catch (Exception any) { if(throwInternalException) throw new RuntimeException(any); }
        };
    }
}
