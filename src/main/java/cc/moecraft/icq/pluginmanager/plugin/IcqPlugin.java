package cc.moecraft.icq.pluginmanager.plugin;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.pluginmanager.Launcher;
import cc.moecraft.icq.pluginmanager.console.ConsoleCommandManager;
import cc.moecraft.logger.HyLogger;
import cc.moecraft.logger.format.AnsiColor;
import cc.moecraft.utils.FileUtils;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

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
    // 插件加载必要的部分
    private boolean enabled = false;
    private PluginLoader loader = null;
    private File file = null;
    private PluginYmlProperties description = null;
    private File dataFolder = null;
    private ClassLoader classLoader = null;

    // 优化开发效率的部分
    private HyLogger logger;
    private PicqBotX bot;
    private PluginConfig config;
    //Console
    private ConsoleCommandManager consoleCommandManager;

    /**
     * 插件属性
     *
     * @return 插件属性对象
     */
    public PluginProperties properties() {
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
        this.consoleCommandManager = Launcher.getConsoleCommandManager();
        this.config = new PluginConfig(this);
        initConfig(classLoader);
    }

    private void initConfig(ClassLoader classLoader)
    {
        if (!config.getConfigFile().exists())
        {
            try
            {
                InputStream resourceAsStream = classLoader.getResourceAsStream("config.yml");
                File configFile = config.getConfigFile();
                FileUtils.createDir(configFile.getParent());
                Files.copy(resourceAsStream, Paths.get(configFile.getAbsolutePath()));
            }
            catch (NoSuchFileException | NullPointerException e)
            {
                return;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return;
            }
        }
        config.initialize();
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

    public InputStream getResource(String filename)
    {
        if (filename == null)
        {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        try
        {
            URL url = getClassLoader().getResource(filename);

            if (url == null)
            {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        }
        catch (IOException ex)
        {
            return null;
        }
    }
}
