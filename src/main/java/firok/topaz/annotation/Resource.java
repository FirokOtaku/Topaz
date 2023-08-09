package firok.topaz.annotation;

/**
 * 系统资源
 * */
public enum Resource
{
	/**
	 * CPU 资源
	 * */
	Cpu,

	/**
	 * GPU 资源
	 * */
	Gpu,

	/**
	 * 内存资源
	 * */
	Mem,

	/**
	 * 磁盘资源
	 * */
	Disk,

	/**
	 * 网络资源
	 * */
	Network,

	/**
	 * 时间资源
	 * */
	Time, // time is money

	/**
	 * 金钱资源
	 * */
	Money, // can money buy time?

	/**
	 * 其它资源
	 * */
	Other,

	/**
	 * 未知
	 * */
	Unknown,
}
