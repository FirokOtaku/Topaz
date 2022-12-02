# 托帕石

> ![topaz](topaz.jpg)  
> 托帕石，矿物学中也称黄玉或黄晶，含氟铝硅酸盐矿物，英文称*Topaz*。  
> 因为托帕石的透明度很高，又很坚硬，所以反光效应很好，加之颜色美丽，颇受青睐。

储存各类代码碎片的小项目. **代码基于 Java 17**.

## 工具列表

* Spring 相关
  * `Ret` _Restful 返回值封装_
* 通用代码
  * `Enums` _无异常枚举转换_
  * `RegexPipeline` _正则批量替换加速_
  * `NativeProcess` _本地进程调用辅助工具_
  * `IHashMapper` _简易哈希运算, 用于文件储存散列_
  * `Encrypts` _RSA, DES, HMAC, Base64 加密解密封装_
  * `Collections` _高级集合操作封装_
  * `I18N` _简易本地化支持_
  * `EnumerationMultiFileInputStream` _文件流枚举器_
  * `AutoClosablePool` _自动资源缓存池_
  * `PlatformTypes` _运行平台检测封装_
  * `JavascriptInvoker` _提供 Javascript 脚本与 Java 环境的交互_
  * `StreamLineEmitter` _以行为单位的流数据监听器_
  * `SimpleMultiThread` _简易多线程创建工具_
  * `Threads` _多线程工具方法_
  * `Shapes` _多边形处理相关方法_
  * `Maths` _数学运算工具方法_
  * `Version` _语义化版本号简易实现_
  * `Files` _文件操作辅助工具_
* 数学运算相关
  * `MathHelper` _数组缓存加速三角函数运算_
  * `Capacities` _储存容量计算_
  * `Binaries` _二进制数据与十六进制字符串的转换_
* 设计相关
  * `Colors` _简易 RGB 空间色彩运算_
  * `ChineseSolarTermColors` _中国传统色 - 故宫 24 节气_
    * `chinese-solar-term-colors.json` _JSON 格式数据_
  * `CssColors` CSS 常用色
    * `css-colors.json` _JSON 格式数据_

> 色彩数据来源自 [uTools](https://www.u.tools/)

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

## 改动记录

* 3.25.0
  * 新增线程操作辅助方法
* 3.24.0
  * 新增集合操作工具方法
* 3.23.0
  * 新增集合操作工具方法
  * 新增函数式编程辅助接口
* 3.22.0
  * 新增简易事件总线实现
* 3.21.0
  * 简易多线程辅助工具新增辅助方法
* 3.20.0
  * 新增闭包引用辅助
  * 新增数字比较工具方法
  * 增强色彩类 Javadoc
* 3.19.0
  * 新增文件操作辅助方法
  * 新增注释型注解
* 3.18.0
  * 语义化版本号新增工具方法
* 3.17.0
  * 新增语义化版本号简易实现
* 3.16.0
  * 新增数学工具方法
* 3.15.0
  * 新增多边形计算工具
  * 新增注释型注解
* 3.14.0
  * 新增简易多线程执行工具
  * 新增多线程工具方法
* 3.13.0
  * 现在打包时会附带源码等内容
* 3.12.1
  * 新增枚举工具方法
* 3.12.0
  * 新增集合操作工具方法
* 3.11.1
  * 新增颜色 JSON 源数据资源文件
* 3.11.0
  * 新增颜色工具类
* 3.10.0
  * 新增简易字符串哈希计算工具类
* 3.9.0
  * 新增操作系统类型判断工具类
* 3.8.0
  * 新增正则管线
* 3.7.0
  * 新增枚举转换相关工具类
* 3.6.0
  * 新增二进制转换相关工具类
* 3.5.0
  * 调整 HMAC 相关 API 签名
* 3.4.0
  * 新增加密解密工具类
* 3.3.0
  * 修复模块化导致的 JVM 无法启动问题
  * 修复数学运算工具错误
  * 新增 JavaScript 执行器单元测试
* 3.2.0
  * 新增行字符串发射器
  * 新增简易 JavaScript 执行器
* 3.1.2
  * 微小代码改进
* 3.1.1
  * 错误修复
* 3.1.0
  * 新增自动关闭缓存池
* 3.0.0
  * 增加模块化支持
* 2.3.0
  * 新增数学运算工具
* 2.2.0
  * 新增集合操作方法
  * 新增封装数据用类
  * 微调多文件流枚举器
* 2.1.0
  * 新增储存容量计算工具方法
* 2.0.0
  * 集合操作方法调整
  * 新增集合操作工具方法
  * 新增本地进程操作工具方法
* 1.0.0
  * 新增集合操作方法
  * 新增 I18N 操作方法
