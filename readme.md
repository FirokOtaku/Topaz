# Topaz 托帕石

> ![topaz](https://github.com/FirokOtaku/Topaz/blob/master/docs/topaz.jpg?raw=true)  
> 托帕石，矿物学中也称黄玉或黄晶，含氟铝硅酸盐矿物，英文称*Topaz*。  
> 因为托帕石的透明度很高，又很坚硬，所以反光效应很好，加之颜色美丽，颇受青睐。

这个库包含了我自己日常开发中遇到的所有或简单或复杂的可复用逻辑, 均抽象并封装为项目无关的 API.  
**基于 Java 21**, 以 **木兰宽松许可证 (第二版) 协议** 开源.

此项目为自用代码库, 依赖的 JDK 版本会随着自身开发进度 *慢慢升级*,  
一般来说会基于 *最新的 LTS 版本 JDK*, 忽略其它中间版本.  
为了方便升级和高版本 JDK 调用, 此库一般 *不采用实验性语法*, 依赖项目可无须以 `--enable-preview` 参数启用.

> 虽然但是, [Java 21 不是 LTS 版本](https://www.youtube.com/watch?v=3bfR22iv8Pc);  
> 虽然但是², [OpenJDK 21 是 LTS 的](https://openjdk.org/projects/jdk/21/).

* ~~工具列表~~ (此列表不再维护, 有需要请直接翻阅源码)
* [改动记录](docs/changelog.md)
* [相关项目 - 琥珀](https://github.com/FirokOtaku/Amber) (如果你需要 Graal 的多语言功能, 请查阅这个子项目)

此库会相对活跃地更新. 如果你也 ~~想不开了~~ 使用这个库并遇到问题, 可提交 issue.

## 最新改动

> 本库遵循 [语义化版本控制规范](https://semver.org/lang/zh-CN/).  
> 除非有特殊说明, 否则在同一主版本号下均可无痛升级

* 7.28.0
  * 新增分页计算工具方法
  * 细微代码改进
* **7.0.0**
  * **重做 i18n 工具类**
    * 现在可以以自定义资源键实例化, 以支持任意资源文件
  * **重做异常工具类**
    * 现在 Topaz 各接口会抛出 `CodeException` 类型异常
    * 现在推荐使用 `CodeExceptionThrower` 来管理和抛出异常类型
  * **重命名部分游戏工具类, 方便调用者项目的代码命名**
  * 统一各反射工具类的 API
  * 新增函数式编程工具类工具方法
  * 清理各废弃成员
  * 清理内部代码

## 已知问题

* `Binaries` 各工具方法包含错误

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
