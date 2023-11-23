# 托帕石

> ![topaz](https://github.com/FirokOtaku/Topaz/blob/main/docs/topaz.jpg?raw=true)  
> 托帕石，矿物学中也称黄玉或黄晶，含氟铝硅酸盐矿物，英文称*Topaz*。  
> 因为托帕石的透明度很高，又很坚硬，所以反光效应很好，加之颜色美丽，颇受青睐。

储存各类代码碎片的小项目. **代码基于 Java 21**.

此项目为自用代码库, 依赖的 JDK 版本会随着自身开发进度慢慢升级,  
但一般来说会基于 *最新的 LTS 版本 JDK*, 忽略其它中间版本.  
为了方便高版本 JDK 调用, 此库一般不采用实验性语法.

* [工具列表](docs/tools.md)
* [改动记录](docs/changelog.md)

最新改动:

* 6.9.0
  * 新增反射工具方法
* **6.0.0**
  * **Java 支持版本从 Java 17 更新至 Java 21**
    * 此升级不包含底层代码和 API 变动, 仅需升级 JDK
  * **开源协议从 MIT 更新至木兰宽松许可证 2.0**
    * MIT 和木兰宽松许可证 2.0 为兼容协议, 目前情况下不需要额外注意

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
