package firok.topaz.indev;

import firok.topaz.TopazExceptions;

/**
 * 可配置的雪花算法生成器
 * */
public class Snowflaker
{
    /**
     * 时间戳位数
     * */
    int countDigitTimestamp = -1;
    /**
     * 时间戳基准
     * */
    long timestampBase = 0;
    /**
     * 工作机器 ID 位数
     * */
    int countDigitWorkerId = 0;
    /**
     * 数据中心 ID 位数
     * */
    int countDigitDatacenterId = 0;
    /**
     * 序列号位数
     * */
    int countDigitSequence = -1;

    private void check()
    {
        TopazExceptions.ParamValueOutOfRange.maybe(
                countDigitTimestamp < 0 ||
                        countDigitDatacenterId < 0 ||
                        countDigitWorkerId < 0 ||
                        countDigitSequence < 0
        );
        TopazExceptions.ParamValueLogicError.maybe(
                countDigitTimestamp + countDigitDatacenterId + countDigitWorkerId + countDigitSequence != 63
        );
    }

    private Snowflaker() { }

    public static class Builder
    {
        private Snowflaker building;
        public Builder()
        {
            this.building = new Snowflaker();
        }

        public Builder countDigitTimestamp(int countDigitTimestamp)
        {
            this.building.countDigitTimestamp = countDigitTimestamp;
            return this;
        }

        public Builder timestampBase(long timestampBase)
        {
            this.building.timestampBase = timestampBase;
            return this;
        }

        public Builder countDigitWorkerId(int countDigitWorkerId)
        {
            this.building.countDigitWorkerId = countDigitWorkerId;
            return this;
        }

        public Builder countDigitDatacenterId(int countDigitDatacenterId)
        {
            this.building.countDigitDatacenterId = countDigitDatacenterId;
            return this;
        }

        public Builder countDigitSequence(int countDigitSequence)
        {
            this.building.countDigitSequence = countDigitSequence;
            return this;
        }

        /**
         * @apiNote 调用此方法后将无法再修改生成器配置
         * */
        public Snowflaker build()
        {
            var ret = this.building;
            ret.check();
            this.building = null;
            return ret;
        }
    }
}
