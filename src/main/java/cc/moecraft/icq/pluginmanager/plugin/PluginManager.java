package cc.moecraft.icq.pluginmanager.plugin;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.pluginmanager.Launcher;
import cc.moecraft.logger.HyLogger;
import lombok.Data;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static cc.moecraft.logger.format.AnsiColor.*;

/**
 * 此类由 Hykilpikonna 在 2018/07/02 创建!
 * Created by Hykilpikonna on 2018/07/02!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
@Data
public class PluginManager {
    private static File pluginRootPath;
    @Getter
    public static Map<String, IcqPlugin> enabledPlugins;
    @Getter
    private static Map<Class, IcqPlugin> enabledPluginsTypeIndex;
    @Getter
    public static PluginLoader pluginLoader;

    public PluginManager(File pluginRootPath, PicqBotX bot) {
        PluginManager.pluginRootPath = pluginRootPath;

        enabledPlugins = new HashMap<>();
        enabledPluginsTypeIndex = new HashMap<>();
        pluginLoader = new PluginLoader(bot);
    }

    public static void enableAllPlugins() {
        ArrayList<File> jarFiles = getJarFiles();
        HyLogger logger = Launcher.getLoggerInstanceManager().getLoggerInstance(CYAN + "Launcher", Launcher.isDebug());
        logger.timing.init();

        logger.log(GREEN + "路径下一共有 " + jarFiles.size() + " 个插件JAR");
        logger.log(YELLOW + "开始加载插件 ...");

        for (int i = 0; i < jarFiles.size(); i++) {
            File jarFile = jarFiles.get(i);

            logger.log(YELLOW + "正在加载JAR: " + jarFile.getName() + " (" + RED + i + YELLOW + "/" + jarFiles.size() + YELLOW + ")");

            try
            {
                IcqPlugin plugin = pluginLoader.loadPlugin(jarFile);
                pluginLoader.enablePlugin(plugin);

                enabledPlugins.put(plugin.getDescription().getName(), plugin);
                enabledPluginsTypeIndex.put(plugin.getClass(), plugin);

                logger.log(String.format("%sJAR: %s 加载完成! %s(%s ms)", GREEN, jarFile.getName(), YELLOW,
                        Math.round(logger.timing.getMilliseconds() * 100d) / 100d));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                logger.log(String.format("%sJAR: %s 加载失败! %s(%s ms)", RED, jarFile.getName(), YELLOW,
                    Math.round(logger.timing.getMilliseconds() * 100d) / 100d));
            }

            logger.timing.reset();
        }

        logger.timing.clear();
    }

    public static ArrayList<File> getJarFiles() {
        File[] allFiles = pluginRootPath.listFiles();
        ArrayList<File> jarFiles = new ArrayList<>();

        assert allFiles != null;
        for (File file : allFiles) {
            if (file.getName().endsWith(".jar")) jarFiles.add(file);
        }

        return jarFiles;
    }

    public void registerAllCommands(PicqBotX bot)
    {
        for (IcqPlugin plugin : enabledPlugins.values())
        {
            for (IcqCommand command : plugin.commands())
            {
                bot.getCommandManager().registerCommand(command);
            }
        }
    }

    public void registerAllEvents(PicqBotX bot)
    {
        for (IcqPlugin plugin : enabledPlugins.values())
        {
            for (IcqListener listener : plugin.listeners())
            {
                bot.getEventManager().registerListener(listener);
            }
        }
    }
}
