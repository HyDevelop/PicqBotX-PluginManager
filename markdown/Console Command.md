# Console Command

## Java

## 新的Console Command

```java
import cc.moecraft.icq.pluginmanager.console.ConsoleCommand;

public class MyConsoleCommand implements ConsoleCommand {
    @Override
  	//传入 args
  	//如果输入为 /print a b c
  	// args = ["a", "b", "c"]
    public void onCommand(String[] args) {
        if (args.length == 0) {
            MyPlugin.logger.log("没有参数");
        } else {
            // 打印第一个参数
            MyPlugin.logger.log("Print " + args[0]);
        }
    }
}

```

## 主类注册

```java
import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.pluginmanager.plugin.IcqPlugin;
import cc.moecraft.logger.HyLogger;
import jdk.nashorn.internal.objects.annotations.Getter;

public class MyPlugin extends IcqPlugin {

    public static HyLogger logger;

    @Override
    public IcqCommand[] commands() {
        return new IcqCommand[0];
    }

    @Override
    public IcqListener[] listeners() {
        return new IcqListener[0];
    }

    @Override
    public void onEnable() {
        logger = this.getLogger();
      	//"print" 为指令的名字, MyConsoleCommand() 为我们刚刚创建的
      	this.getConsoleCommandManager().addCommand("print", new MyConsoleCommand());
    }

    @Override
    public void onDisable() {

    }
}
```

