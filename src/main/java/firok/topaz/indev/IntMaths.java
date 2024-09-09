package firok.topaz.indev;

public final class IntMaths
{
    private IntMaths() { }

    /**
     * 计算需要将一个整型拆分成若干份之后每份的大小
     * @param valueTotal 需要拆分的整型
     * @param countPart 需要拆分的份数, 一般来说是 tick-per-second 之类的值
     * @param stepPart 当前正在计算第几份
     * @since 7.30.0
     * */
    public static int intDivide(int valueTotal, int countPart, int stepPart)
    {
        if(valueTotal == countPart) return 1;
        else if(valueTotal > countPart)
        {
            int valuePart = valueTotal / countPart;
            return stepPart == countPart - 1 ? valuePart + valueTotal % countPart : valuePart;
        }
        else // 如果总数小数份数, 则只有最后一 part 会返回 value Total
        {
            return stepPart == countPart - 1 ? valueTotal : 0;
        }
    }
}
