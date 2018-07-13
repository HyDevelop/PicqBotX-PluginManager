package cc.moecraft.icq.pluginmanager.plugin;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.pluginmanager.Launcher;
import cc.moecraft.icq.pluginmanager.exceptions.InvalidPluginException;
import cc.moecraft.icq.pluginmanager.exceptions.InvalidPluginYmlException;
import cc.moecraft.logger.HyLogger;
import cc.moecraft.logger.format.AnsiColor;
import com.google.common.io.PatternFilenameFilter;
import lombok.Data;

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
public class PluginManager
{
    private final File pluginRootPath;
    private Map<String, IcqPlugin> enabledPlugins;
    private PluginLoader pluginLoader;

    public PluginManager(File pluginRootPath, PicqBotX bot)
    {
        this.pluginRootPath = pluginRootPath;

        enabledPlugins = new HashMap<>();
        pluginLoader = new PluginLoader(bot);
    }

    public void enableAllPlugins()
    {
        ArrayList<File> jarFiles = getJarFiles();
        HyLogger logger = Launcher.getLoggerInstanceManager().getLoggerInstance(CYAN + "Launcher", Launcher.isDebug());
        logger.timing.init();

        for (File jarFile : jarFiles)
        {
            try
            {
                IcqPlugin plugin = pluginLoader.loadPlugin(jarFile);
                pluginLoader.enablePlugin(plugin);

                enabledPlugins.put(plugin.getDescription().getName(), plugin);

                System.out.println(plugin);
            }
            catch (InvalidPluginException e)
            {
                e.printStackTrace();
            }
            catch (InvalidPluginYmlException e)
            {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<File> getJarFiles()
    {
        File[] allFiles = pluginRootPath.listFiles();
        ArrayList<File> jarFiles = new ArrayList<>();

        assert allFiles != null;
        for (File file : allFiles)
        {
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
