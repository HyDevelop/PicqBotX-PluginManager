package cc.moecraft.icq.pluginmanager.plugin;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.logger.AnsiColor;
import cc.moecraft.logger.DebugLogger;
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
    private DebugLogger logger;

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
    public abstract PluginProperties properties();

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

    public void onEnable()
    {
        logger.log(String.format("%s插件 %s 已加载!", AnsiColor.GREEN, description.getName()));
    }

    public void onDisable()
    {
        logger.log(String.format("%s插件 %s 已卸载!", AnsiColor.YELLOW, description.getName()));
    }

    final void init(PluginLoader loader, PicqBotX bot, PluginYmlProperties description, File dataFolder, File file, ClassLoader classLoader)
    {
        this.loader = loader;
        this.bot = bot;
        this.file = file;
        this.description = description;
        this.dataFolder = dataFolder;
        this.classLoader = classLoader;
        this.logger = getBot().getLogger();
    }
}
