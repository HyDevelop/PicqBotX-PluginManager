<br>
<br>
<h1 align="center">
  PicqBotX - Plugin Manager
</h1>
<h4 align="center">
  一个基于 <a href="https://github.com/HyDevelop/PicqBotX">PicqBotX</a> 机器人类库的插件系统!
</h4>
<h5 align="center">
  <a href="#maven">Maven导入</a>&nbsp;&nbsp;
  <a href="#introduction">介绍</a>&nbsp;&nbsp;
  <a href="#development">开发</a>&nbsp;&nbsp;
  <a href="#license">开源条款</a>
</h5>

<br>

<a name="introduction"></a>
介绍 (v1.0.0):
--------

一个基于PicqBotX的插件管理系统. <br>
( 和Bukkit的插件系统一模一样 Credit to @md_5 <br>
~~插件的插件系统的插件的API的插件的插件系统~~

#### 用处:

在开发这个插件管理系统之前, 每个PicqBotX机器人必须作为一个单独的应用运行.<br>
但是这样, 如果一个开发者开发了一套功能, 比如信息查询, <br>
然后另一个开发者开发了另一套功能, 比如碰数游戏, <br>
但是如果想让一个机器人实例同时运行这两套功能的话, <br>
就必须手动合并这两个项目. <br><br>
但是如果这两个开发者把他们的功能写成这个插件系统的插件的话. <br>
运行一个插件管理器, 然后把两个构建好的插件JAR都丢到运行目录的Plugins文件夹里, <br>
就能在一个机器人实例上同时运行两套功能了! <br>

#### 已实现功能:

* **v1.0.0**
* 动态URLClassLoader加载插件JAR
* 每个插件注册指令和事件监听器
* 每个插件单独的Logger

#### 待实现(TODO)的功能:

* 发布一个有功能的插件例子
* 写运行教程

<br>

<a name="maven"></a>
Maven 导入:
--------

没有添加JitPack的Repo的话首先添加Repo, 在pom里面把这些粘贴进去:

```xml
<repositories>
    <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
    </repository>
</repositories>
```

然后添加这个库:

```xml
<dependency>
    <groupId>com.github.hydevelop</groupId>
    <artifactId>PicqBotX-PluginManager</artifactId>
    <version>1.0.0</version>
</dependency>
```

然后ReImport之后就导入好了!

<br>

<a name="gradle"></a>
Gradle 导入:
--------

没有添加JitPack的Repo的话首先添加Repo, 在pom里面把这些粘贴进去:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

然后添加这个库:

```gradle
dependencies {
    implementation 'com.github.hydevelop:PicqBotX-PluginManager:1.0.0'
}
```

<!-- 每次更新都要手动改这些版本号好烦的_(:з」∠)_... -->

#### [其他导入(SBT / Leiningen)](https://jitpack.io/#HyDevelop/PicqBotX-PluginManager/1.0.0)

<br>
