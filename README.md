<br>
<br>
<h1 align="center">
  PicqBotX - Plugin Manager
</h1>
<h4 align="center">
  一个基于 <a href="https://github.com/HyDevelop/PicqBotX">PicqBotX</a> 机器人类库的插件系统!
</h4>
<h5 align="center">
  <a href="#maven">Maven 导入</a>&nbsp;&nbsp;
  <a href="#introduction">介绍</a>&nbsp;&nbsp;
  <a href="#environment">环境</a>&nbsp;&nbsp;
  <a href="#development">开发</a>&nbsp;&nbsp;
  <a href="#license">开源条款</a>
</h5>  

<br>
<br>
<br>

[***查看插件列表***](./markdown/plugin-list.md)

<a name="introduction"></a>
介绍 (v4.15.0.134):
--------

一个基于 PicqBotX QQ 机器人类库的插件系统. <br>
( 和 Bukkit 的插件系统一模一样 Credit to @md_5 <br>
~~插件的插件系统的插件的 API 的插件的插件系统~~

#### 用处:

在开发这个插件管理系统之前, 每个 PicqBotX 机器人必须作为一个单独的应用运行.<br>
但是这样, 如果一个开发者开发了一套功能, 比如信息查询, <br>
然后另一个开发者开发了另一套功能, 比如碰数游戏, <br>
但是如果想让一个机器人实例同时运行这两套功能的话, <br>
就必须手动合并这两个项目. <br>
但是如果这两个开发者把他们的功能写成这个插件系统的插件的话. <br>
运行一个插件管理器, 然后把两个构建好的插件 JAR 都丢到运行目录的 Plugins 文件夹里, <br>
就能在一个机器人实例上同时运行两套功能了! <br>

#### 已实现功能:

* **v1.0.0**
* 动态 URLClassLoader 加载插件 JAR
* 每个插件注册指令和事件监听器
* 每个插件单独的 Logger

#### 待实现(TODO)的功能:

* 发布一个有功能的插件例子
* 写运行教程

<br>

<a name="maven"></a>
Maven 导入:
--------

没有添加 JitPack 的 Repo 的话首先添加 Repo, 在 pom 里面把这些粘贴进去:

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
    <version>4.15.0.134</version>
    <scope>provided</scope>
</dependency>
```

然后 ReImport 之后就导入好了!

<br>

<a name="gradle"></a>
Gradle 导入:
--------

没有添加 JitPack 的 Repo 的话首先添加 Repo, 在 pom 里面把这些粘贴进去:

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
    implementation 'com.github.hydevelop:PicqBotX-PluginManager:4.15.0.134'
}
```

<!-- 每次更新都要手动改这些版本号好烦的_(:з」∠)_... -->

#### [其他导入(SBT / Leiningen)](https://jitpack.io/#HyDevelop/PicqBotX-PluginManager/4.15.0.134)

<br>

<a name="environment"></a>
配置环境:
--------

### 1. [配置 PicqBotX 的环境](https://github.com/HyDevelop/PicqBotX#environment)

### 2. 创建一个运行目录文件夹, 在里面下载构建好的 PicqBotX-PluginManager 的 JAR 文件. (TODO: 这里补个链接)

### 3. 创建启动脚本文件:

#### 3.1. Windows:

基础启动:

```bat
@echo off

title PicqBotX 插件管理服务器
java -jar {JAR文件名}.jar
pause
```

再加一个自动重启和重启次数计数:

```bat
@echo off 
set a = -1

:loop
set /a a += 1

title PicqBotX 插件管理服务器 [重启次数:%a%]
java -jar {JAR 文件名}.jar

echo ##################### 线程已关闭, 5秒后自动重启... #####################
ping localhost -n 5 >NUL
goto loop
```

把这段存到运行目录的 start.bat 文件里就行啦! ( jar 文件名替换成你下载的 jar 文件名)

#### 3.2. Linux:

```sh
while true
do

java -jar {JAR 文件名}.jar

echo "##################### 线程已关闭, 5秒后自动重启... #####################"
sleep 5
done
```

把这段存到运行目录的 start.sh 文件里就行啦! (jar 文件名替换成你下载的 jar 文件名)

### 4. 启动:

#### 4.1. Windows:

* UI 版 Windows 的话直接点开 start.bat 就好了!
* 无 UI 版的话要 cd 到当前目录, 然后执行 start.bat (真的有人用无 UI 的 Windows 么...

注意: 不要问我`Error: Unable to access jarfile {JAR 文件名}.jar`怎么解决, 仔细想想都知道怎么解决嘛...

#### 4.2. Linux:

* 打开控制台
* 执行 cd {运行目录}
* 执行 sudo chmod +x start.sh
* 执行 ./start.sh

### 5. 配置:

启动第一次后, 会自动生成 config.yml 配置文件:

```yml
# ############################ #
# PicqBotX 插件启动器 配置文件 #
#     作者: Hykilpikonna       #
# 对应版本: 4.15.0.134              #
# ############################ #

# 连接设置
ConnectionSettings:

  # 发送地址
  PostURL: '127.0.0.1'

  # 发送地址的端口
  PostPort: 31091

  # 监听端口 (HTTP 服务器端口)
  ListeningPort: 31092

# 指令设置
CommandSettings:

  # 是否启动指令功能
  Enable: true

  # 指令前缀
  Prefixes:
  - 'bot -'
  - '!'
  - '/'

  # 两个插件同时注册一个指令的话怎么办
  #   ENABLE_LAST    : 启用最后注册的
  #   ENABLE_ALL     : 启用所有
  # 如果冲突的话, 可以用 /<插件名>:<指令> 来执行某个插件的指令
  ConflictOperation: ENABLE_ALL

# 插件加载设置
PluginLoaderSettings:

  # 是否启动插件加载
  Enable: true

  # 插件目录
  PluginDir: './plugins/'

# 日志设置
LoggerSettings:

  # 是否输出 Debug 日志
  Debug: false

  # 颜色支持级别
  #   FORCED       : 不传入 Jansi, 强制启用颜色
  #   PASSTHROUGH  : 实际效果和 FORCED 一样, 传入 Jansi 但是不处理
  #   PRESET_ONLY  : 只输出预设颜色, 移除 RGB
  #   DEFAULT      : 默认支持
  #   OS_DEPENDENT : 取决于 OS, 如果是 Linux 就用 DEFAULT, 如果是 Windows 就用 PRESET_ONLY
  ColorSupportLevel: OS_DEPENDENT

  # Log 文件输出目录 (相对目录)
  LogFileRelativePath: logs

  # Log 文件名
  LogFileName: PicqBotX-Log
```

要改的话改完保存然后直接重启就行了.

### 6. 插件管理:

#### 6.1. 添加/移除插件:

* 把构建好的插件 JAR 文件放到 plugins 文件夹里 (如果用外部导入的话必须是 shaded).
* 重启就会自动加载了
* 移除的话先关掉, 然后把 JAR 包删掉, 然后启动就行了...

#### 6.2. 调整优先级:

* 现在还没有自动判断导入调整优先级
* 所以... 调整优先级只能通过调整文件名
* 文件名越靠前就先加载
* 比如说如果 插件B 是 插件A 的类库的话
* 那就要重命名成 `0-插件B.jar` 和 `1-插件A.jar` 来调整加载顺序了

<br>

<a name="development"></a>
开发插件:
--------

### 1. 配置 Maven:

很重要, 如果要用外部导入的话就必须用 Maven.<br>
不会 Maven 的话可以跳过这一步. ( 推荐先去学 Maven<br>

首先设置 SDK 级别, 因为 Picq 是 Java 8 运行的所以推荐插件也用 Java 8 了<br>

```xml
<properties>
    <maven.compiler.target>1.8</maven.compiler.target>
    <maven.compiler.source>1.8</maven.compiler.source>
</properties>
```

然后添加导入([看这里](#maven))<br>
然后添加构建配置:

```xml
<build>
    <sourceDirectory>src/main/java</sourceDirectory>
    <defaultGoal>clean install</defaultGoal>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <filtering>true</filtering>
            <includes>
                <include>plugin.yml</include>
                <include>config.yml</include>
            </includes>
        </resource>
    </resources>

    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <version>2.5</version>
            <configuration>
                <archive>
                    <manifestEntries>
                        <Main-Class>cc.moecraft.icq.pluginmanager.Launcher</Main-Class>
                    </manifestEntries>
                </archive>
            </configuration>
        </plugin>

        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>2.4.1</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <shadedArtifactAttached>true</shadedArtifactAttached>
                <createDependencyReducedPom>false</createDependencyReducedPom>
            </configuration>
        </plugin>
    </plugins>
</build>
```

这个构建配置实现了两件事情: <br>
1. 把`resources`里的`plugin.yml`和`config.yml`放到构建好的JAR里面.<br>
2. 把外部导入 shade 到 JAR 里面.<br>

### 2. 写一个 plugin.yml

* 右键`src/main/resources`路径, 创建一个叫`plugin.yml`的文件. (***必须全小写***
* 写进去需要的信息:

| 字段 | 重要性 | 代表什么 | 例子 |
| :------------: | :------------: | :------------: | :------------: |
| name | 必要 | 插件的名字 | TestPlugin |
| main | 必要 | 插件的主类 | cc.moecraft.icq.plugins.testplugin.Main |

例子:

```yml
name: TestPlugin
main: cc.moecraft.icq.plugins.testplugin.Main
```

### 3. 创建主类:

* 创建一个类
* 注意: 这个类的包位置必须和上面`plugin.yml`里面`main`字段写的一样
* 让这个类继承`IcqPlugin`类(`extends IcqPlugin`).
* 实现抽象方法
* 创建完了!

例子:

```java
package cc.moecraft.icq.plugins.testplugin;

import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.pluginmanager.plugin.IcqPlugin;

public class Main extends IcqPlugin
{
    @Override
    public void onEnable()
    {
        // 加载插件的时候会运行这个方法
        instance = this;
    }

    @Override
    public void onDisable()
    {
        // 卸载插件的时候会运行这个方法
    }

    @Override
    public IcqCommand[] commands()
    {
        return new IcqCommand[]
                {
                };
    }

    @Override
    public IcqListener[] listeners()
    {
        return new IcqListener[]
                {
                };
    }
    
    private static Main instance;
    
    public static Main getInstance()
    {
        return instance;
    }
}
```

### 4. 添加事件监听器:

* 写一个事件监听器类: [看这里](https://github.com/HyDevelop/PicqBotX#%E7%9B%91%E5%90%AC%E4%BA%8B%E4%BB%B6)
* 在 Main 的 listeners() 方法里添加一个实例

例子:<br>

监听器类 (TestListener.java):

```java
public class TestListener extends IcqListener
{
    @EventHandler
    public void onMessageEvent(EventMessage event)
    {
        Main.getInstance().getLogger().log("收到消息事件! 内容 = " + event.toString());
    }
}
```

主类:

```java
    @Override
    public IcqListener[] listeners()
    {
        return new IcqListener[]
                {
                        new TestListener()
                };
    }
```

### 5. 添加指令:

* 写一个指令类: [看这里](https://github.com/HyDevelop/PicqBotX#%E6%8C%87%E4%BB%A4)
* 在 Main 的 commands() 方法里添加一个实例

例子:<br>

指令类 (TestCommand.java):

```java
public class TestCommand implements EverywhereCommand
{
    @Override
    public String run(EventMessage event, User user, String s, ArrayList<String> arrayList)
    {
        event.getBot().getLogger().log("收到测试指令!");
        return "测试指令回复!";
    }

    @Override
    public CommandProperties properties()
    {
        return new CommandProperties("test");
    }
}
```

主类:

```java
    @Override
    public IcqCommand[] commands()
    {
        return new IcqCommand[]
                {
                        new TestCommand()
                };
    }
```

### 6. 使用配置文件:

* 写一个默认配置文件`config.yml`放进`main/src/resources`里 (不然使用getConfig会返回null).
* 然后就可以用了
* 存储位置在`运行目录/plugins/插件名/config.yml`.

例子: 默认配置`config.yml`:

```yml
TestBoolean1: true
TestList1:
- 871674895
- 666666666
TestList2: []
TestKey1:
  TestKey2: TestValue
```

例子: 从程序里访问:

```java
getConfig().getBoolean("TestBoolean1"); // 返回 TestBoolean1 字段的值
getConfig().getStringList("TestList1"); // 返回 TestList1 字段的字符串数组
getConfig().getStringList("TestList2"); // 返回 TestList2 字段的字符串数组
getConfig().getString("TestKey1.TestKey2"); // 返回 TestKey1 下的 TestKey2 字段的值
```

### 7. 构建插件:

* 注意:
* Jar 文件会构建到`target`目录下
* 加载到服务器要用`shaded`版本, 作为API发布可以不用`shaded`
* (不过推荐全都用`shaded`版本, 毕竟万一会有人想直接拿 API 的 jar 加载就抛 class not found 呢...

#### 7.1. 全系统 - IntelliJ IDEA

* 打开右边的`Maven Project`栏
* 展开`Lifecycle`菜单
* 双击`package`运行

#### 7.2. Linux - Bash

* 在项目路径打开任意Bash
* `mvn package`

*(不要问我 bash: mvn: command not found 怎么解决...*

#### 如果有 Bug 的话, 加群 498386389 问吧w

<br>

<a name="license"></a>
[开源条款](https://choosealicense.com/licenses/gpl-3.0/): GNU / GPL
--------

