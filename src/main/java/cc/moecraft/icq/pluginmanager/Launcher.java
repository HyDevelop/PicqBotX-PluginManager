package cc.moecraft.icq.pluginmanager;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.PicqConfig;
import cc.moecraft.icq.accounts.BotAccount;
import cc.moecraft.icq.pluginmanager.plugin.PluginManager;
import cc.moecraft.logger.HyLogger;
import cc.moecraft.logger.LoggerInstanceManager;
import cc.moecraft.logger.environments.ColorSupportLevel;
import cc.moecraft.logger.format.AnsiColor;
import cc.moecraft.utils.FileUtils;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    private static LauncherConfig config;

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

    public static void main(String[] args) throws Exception
    {
        initializeConfig();

        debug = config.getBoolean("LoggerSettings.Debug");
        /*
        bot = new PicqBotX(
                config.getInt("ConnectionSettings.ListeningPort"),
                debug, ColorSupportLevel.valueOf(config.getString("LoggerSettings.ColorSupportLevel")),
                config.getString("LoggerSettings.LogFileRelativePath"),
                config.getString("LoggerSettings.LogFileName"));

         */


        PicqConfig botConfig = new PicqConfig(config.getInt("ConnectionSettings.ListeningPort"));
        botConfig.setUseAsyncCommands(config.getBoolean("CommandSettings.Async", true));
        botConfig.setApiAsync(config.getBoolean("CommandSettings.Async", true));
        botConfig.setAccessToken(config.getString("Access Token"));
        botConfig.setSecret(config.getString("Secret"));
        bot = new PicqBotX(botConfig);


        bot.setUniversalHyExpSupport(config.getBoolean("OtherSettings.HyExpression.Resolve", false),
                config.getBoolean("OtherSettings.HyExpression.SafeMode", true));



        loggerInstanceManager = bot.getLoggerInstanceManager();
        logger = loggerInstanceManager.getLoggerInstance("Launcher", debug);
        libManager = new LibManager();

        // 账号设置
        try
        {
            for (String key : config.getKeys("Accounts")) bot.getAccountManager().addAccount(new BotAccount(key, bot, config.getString("Accounts." + key + ".PostURL"), config.getInt("Accounts." + key + ".PostPort")));
        }
        catch (NullPointerException e)
        {
            logger.error("配置读取失败: " + e);
            Thread.sleep(5);
            e.printStackTrace();
            return;
        }

        if (config.getBoolean("CommandSettings.Enable"))
            bot.enableCommandManager(config.getStringList("CommandSettings.Prefixes").toArray(new String[0]));
        // 注册插件
        if (config.getBoolean("PluginLoaderSettings.Enable")) initializePlugins(bot);

        bot.startBot();
    }

    private static void initializeConfig()
    {
        config = new LauncherConfig();

        if (!config.getConfigFile().exists())
        {
            try
            {
                InputStream resourceAsStream = Launcher.class.getClassLoader().getResourceAsStream("plugin-manager-default-config.yml");
                File configFile = config.getConfigFile();
                FileUtils.createDir(configFile.getParent());
                Files.copy(resourceAsStream, Paths.get(configFile.getAbsolutePath()));
            }
            catch (IOException e)
            {
                throw new RuntimeException("错误: JAR包已损坏或运行环境错误, 无法找到yml文件");
            }
        }

        config.initialize();
    }

    private static void initializePlugins(PicqBotX bot)
    {
        logger.timing.init();
        logger.log(AnsiColor.YELLOW + "开始初始化插件加载器 ...");

        File pluginRootDir = new File(config.getString("PluginLoaderSettings.PluginDir"));

        logger.log(AnsiColor.GREEN + "已找到插件存储路径: " + pluginRootDir.getAbsolutePath());

        if (!pluginRootDir.isDirectory())
        {
            FileUtils.createDir(pluginRootDir.getPath());
            logger.log(AnsiColor.RED + "插件路径不存在, 已自动创建");
        }

        pluginManager = new PluginManager(pluginRootDir, bot);
        pluginManager.enableAllPlugins();

        // 注册事件和指令
        if (config.getBoolean("CommandSettings.Enable")) pluginManager.registerAllCommands(bot);
        pluginManager.registerAllEvents(bot);

        logger.log(String.format("%s插件全部加载完成! %s(总 %s ms)", AnsiColor.GREEN, AnsiColor.YELLOW, Math.round(logger.timing.getMilliseconds() * 100d) / 100d));
        logger.timing.clear();
    }

}
