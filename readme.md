# Topaz 托帕石

> ![topaz](https://github.com/FirokOtaku/Topaz/blob/master/docs/topaz.jpg?raw=true)  
> 托帕石，矿物学中也称黄玉或黄晶，含氟铝硅酸盐矿物，英文称*Topaz*。  
> 因为托帕石的透明度很高，又很坚硬，所以反光效应很好，加之颜色美丽，颇受青睐。

这个库包含了我自己日常开发中遇到的所有或简单或复杂的可复用逻辑, 均抽象并封装为项目无关的 API.  
**基于 Java 25**, 以 **木兰宽松许可证 (第二版) 协议** 开源.

此项目为自用代码库, 依赖的 JDK 版本会随着自身开发进度 *慢慢升级*,  
一般来说会基于 *最新的 LTS 版本 JDK*, 忽略其它中间版本.  
为了方便升级和高版本 JDK 调用, 此库一般 *不采用实验性语法*, 依赖项目可无须以 `--enable-preview` 参数启用.

> 如果你对我写的其它工具库感兴趣,
> 可以看看这个面向 Web / Javascript 生态的 [Opal 库](https://github.com/FirokOtaku/opal)

* ~~工具列表~~ (此列表不再维护, 有需要请直接翻阅源码)
* [改动记录](docs/changelog.md)
* [相关项目 - 琥珀](https://github.com/FirokOtaku/Amber) (如果你需要 Graal 的多语言功能, 请查阅这个子项目)

此库会相对活跃地更新. 如果你也 ~~想不开了~~ 使用这个库并遇到问题, 可提交 issue.

## 最新改动

> 本库遵循 [语义化版本控制规范](https://semver.org/lang/zh-CN/).  
> 除非有特殊说明, 否则在同一主版本号下均可无痛升级

* **8.0.0**
  * **Java 支持版本从 Java 21 更新至 Java 25**
    * 新增或重做的类和接口的注释开始采用 Markdown 语法
  * **重做二进制计算工具类**
    * 修复已知的高低字节序转换错误问题
  * **重做 `CodeException` 和配套工具**
    * 现在需要从 `CodeExceptionThrower` 抛出 `CodeException`. 不再允许自行实例化 `CodeException`
    * 现在异常的详细信息被包含在 `CodeExceptionContext` 中
    * 为 `CodeExceptionThrower` 增加更多工具接口
  * **调整 `Ret` 和 `CodeRet` 部分接口参数类型**
    * 调用者源码不需要改动, 但可能需要重新编译项目
  * **调整部分时间运算相关工具方法的计算方式**
  * **`ReentrantLockCompound` 已被重做为 `LockCompound`**
    * 为已有的 `LockProxy` 增加更多支持
    * 提供手动加锁和解锁方法
  * 新增数学计算工具方法
  * 新增打乱集合和打乱数组工具方法
  * 新增数组反序操作工具方法
  * 新增数组映射处理工具方法
  * 新增注释型注解
  * 用于获取调用者信息的反射工具方法现在不再为实验性的
  * 调整部分接口异常抛出类型
  * 开发用 package 的可见性不再为公开的
  * 升级部分库依赖

## 已知问题

* `Binaries` 各工具方法包含错误, 将会在后续版本重构代码
* 部分单元测试可能因为各种原因无法通过, 如果你只是为了使用本库,  
  一般来说只需要启用 Maven 的 `skip-test` 模式然后 `mvn install` 即可

## 安装

目前可以使用如下方式安装依赖:

* `clone repo` 并 `mvn install`
* 使用 GitHub Maven Packages
  ```xml
  <repositories>
    <repository>
      <id>github</id>
      <url>https://maven.pkg.github.com/FirokOtaku/Topaz</url>
    </repository>
  </repositories>
  
  <dependencies>
    <dependency>
      <groupId>firok</groupId>
      <artifactId>topaz</artifactId>
      <version>{VERSION}</version>
    </dependency>
  </dependencies>
  ```

> 正常使用 GitHub Maven Packages 需 [配置验证](https://docs.github.com/cn/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry)
