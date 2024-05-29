/**
 * 一些计算哈希用的工具代码.
 * 主要分为两个部分: <br>
 * 1. 第一部分由 {@link firok.topaz.hash.IHashMapper} 和 {@link firok.topaz.hash.IMappedHash} 相关的接口实现组成,
 *    设计原意是用于后端系统存储大量文件时的文件名散列.
 *    这部分工具基于 SPI 驱动和使用; 可以通过 SPI 扩展自定义散列算法. <br>
 * 2. 第二部分由 相关的接口实现组成,
 *    设计原意是用于简易分库分表时的数据库表名散列.
 *    这部分工具可以通过工厂方法直接创建不可变实例使用.
 *    (暂未实现)
 * */
package firok.topaz.hash;
