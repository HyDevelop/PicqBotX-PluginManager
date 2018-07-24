package cc.moecraft.icq.pluginmanager;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.exceptions.HttpServerStartFailedException;
import cc.moecraft.icq.exceptions.InvalidSendingURLException;
import cc.moecraft.icq.exceptions.VersionIncorrectException;
import cc.moecraft.icq.pluginmanager.plugin.PluginManager;
import cc.moecraft.logger.HyLogger;
import cc.moecraft.logger.LoggerInstanceManager;
import cc.moecraft.logger.environments.ColorSupportLevel;
import cc.moecraft.logger.format.AnsiColor;
import cc.moecraft.utils.FileUtils;
import com.xiaoleilu.hutool.http.HttpException;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 此类由 Hykilpikonna 在 2018/06/21 创建!
 * Created by Hykilpikonna on 2018/06/21!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class Launcher
{
    @Getter
    private static LauncherConfig launcherConfig;

    @Getter
    private static PicqBotX bot;

    @Getter
    private static LoggerInstanceManager loggerInstanceManager;

    @Getter
    public static HyLogger logger;

    @Getter
    private static PluginManager pluginManager;

    @Getter
    private static LibManager libManager;

    @Getter
    private static boolean debug;

    public static void main(String[] args) throws IllegalAccessException, InstantiationException
    {
        initializeConfig();

        debug = launcherConfig.getBoolean("LoggerSettings.Debug");

        bot = new PicqBotX(
                launcherConfig.getString("ConnectionSettings.PostURL"),
                launcherConfig.getInt("ConnectionSettings.PostPort"),
                launcherConfig.getInt("ConnectionSettings.ListeningPort"),
                debug,
                ColorSupportLevel.valueOf(launcherConfig.getString("LoggerSettings.ColorSupportLevel")),
                launcherConfig.getString("LoggerSettings.LogFileRelativePath"),
                launcherConfig.getString("LoggerSettings.LogFileName")
        );

        loggerInstanceManager = bot.getLoggerInstanceManager();

        logger = loggerInstanceManager.getLoggerInstance("Launcher", debug);

        libManager = new LibManager();

        if (launcherConfig.getBoolean("CommandSettings.Enable"))
            bot.enableCommandManager(false, launcherConfig.getStringList("CommandSettings.Prefixes").toArray(new String[0]));

        // 注册插件
        if (launcherConfig.getBoolean("PluginLoaderSettings.Enable")) initializePlugins(bot);

        try
        {
            bot.startBot();
        }
        catch (HttpException e)
        {
            logger.error("HTTP版本验证请求失败.");
            logger.error("可能是因为你没有开酷Q.");
            logger.error("也可能是config.yml配置文件里HTTP发送地址写错了");
        }
        catch (HttpServerStartFailedException | VersionIncorrectException | InvalidSendingURLException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean initializeConfig()
    {
        launcherConfig = new LauncherConfig();

        if (!launcherConfig.getConfigFile().exists())
        {
            try
            {
                InputStream resourceAsStream = Launcher.class.getClassLoader().getResourceAsStream("plugin-manager-default-config.yml");
                File configFile = launcherConfig.getConfigFile();
                FileUtils.createDir(configFile.getParent());
                Files.copy(resourceAsStream, Paths.get(configFile.getAbsolutePath()), new CopyOption[0]);
            }
            catch (IOException e)
            {
                logger.error("错误: JAR包已损坏或运行环境错误, 无法找到yml文件");
                return false;
            }
        }

        launcherConfig.initialize();

        return true;
    }

    public static boolean initializePlugins(PicqBotX bot)
    {
        logger.timing.init();
        logger.log(AnsiColor.YELLOW + "开始初始化插件加载器 ...");

        File pluginRootDir = new File(launcherConfig.getString("PluginLoaderSettings.PluginDir"));

        logger.log(AnsiColor.GREEN + "已找到插件存储路径: " + pluginRootDir.getAbsolutePath());

        if (!pluginRootDir.isDirectory())
        {
            FileUtils.createDir(pluginRootDir.getPath());
            logger.log(AnsiColor.RED + "插件路径不存在, 已自动创建");
        }

        pluginManager = new PluginManager(pluginRootDir, bot);
        pluginManager.enableAllPlugins();

        // 注册事件和指令
        if (launcherConfig.getBoolean("CommandSettings.Enable")) pluginManager.registerAllCommands(bot);
        pluginManager.registerAllEvents(bot);

        logger.log(String.format("%s插件全部加载完成! %s(总 %s ms)", AnsiColor.GREEN, AnsiColor.YELLOW, Math.round(logger.timing.getMilliseconds() * 100d) / 100d));
        logger.timing.clear();

        return true;
    }
}
