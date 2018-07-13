package cc.moecraft.icq.pluginmanager.plugin;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.pluginmanager.Launcher;
import cc.moecraft.logger.HyLogger;
import cc.moecraft.logger.format.AnsiColor;
import lombok.Data;

import java.io.File;

/**
 * 插件抽象Main类
 *
 * 注意: 一个插件只能有一个继承IcqPlugin的类
 *       必须在plugin.yml里的main项写继承IcqPlugin的类的包路径
 *       还有name项里写上插件名
 *
 * @author Hykilpikonna
 */
@Data
public abstract class IcqPlugin
{
    private HyLogger logger;

    private PicqBotX bot;

    private boolean enabled = false;
    private PluginLoader loader = null;
    private File file = null;
    private PluginYmlProperties description = null;
    private File dataFolder = null;
    private ClassLoader classLoader = null;

    /**
     * 插件属性
     * @return 插件属性对象
     */
    public PluginProperties properties()
    {
        return new PluginProperties(commands(), listeners());
    }

    /**
     * 这个插件要注册的指令
     * @return 要注册的指令实例列表
     */
    public abstract IcqCommand[] commands();

    /**
     * 这个插件要注册的监听器
     * @return 要注册的监听器实例列表
     */
    public abstract IcqListener[] listeners();

    public abstract void onEnable();

    public abstract void onDisable();

    final void init(PluginLoader loader, PicqBotX bot, PluginYmlProperties description, File dataFolder, File file, ClassLoader classLoader)
    {
        this.loader = loader;
        this.bot = bot;
        this.file = file;
        this.description = description;
        this.dataFolder = dataFolder;
        this.classLoader = classLoader;
        this.logger = Launcher.getLoggerInstanceManager().getLoggerInstance(description.getName(), Launcher.isDebug());
    }

    public final void setEnabled(final boolean enabled)
    {
        if (this.enabled == enabled) return;

        this.enabled = enabled;
        if (this.enabled)
        {
            onEnable();

            logger.log(String.format("%s插件 %s 声明了 %s 个指令和 %s 个监听器!",
                    AnsiColor.GREEN,
                    getDescription().getName(),
                    commands().length,
                    listeners().length));

            logger.log(String.format("%s插件 %s 已加载!", AnsiColor.GREEN, description.getName()));
        }
        else
        {
            onDisable();

            logger.log(String.format("%s插件 %s 已卸载!", AnsiColor.GREEN, description.getName()));
        }
    }
}
