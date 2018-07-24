package cc.moecraft.icq.pluginmanager;

import cc.moecraft.icq.pluginmanager.plugin.IcqPlugin;
import cc.moecraft.icq.pluginmanager.plugin.PluginManager;
import cc.moecraft.logger.LoggerInstanceManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类由 Hykilpikonna 在 2018/07/15 创建!
 * Created by Hykilpikonna on 2018/07/15!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class PicqPM
{
    /**
     * 获取一个插件的运行实例
     * @param pluginMain 插件的主类
     * @param <T> 插件的类型
     * @return 插件的主类实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getPlugin(Class<T> pluginMain)
    {
        return (T) Launcher.getPluginManager().getEnabledPluginsTypeIndex().get(pluginMain);
    }

    /**
     * 获取一个插件的运行实例
     * @param pluginName 插件名
     * @return 插件的运行实例
     */
    public static IcqPlugin getPlugin(String pluginName)
    {
        return Launcher.getPluginManager().getEnabledPlugins().get(pluginName);
    }

    public static LoggerInstanceManager getLoggerInstanceManager()
    {
        return Launcher.getLoggerInstanceManager();
    }

    public static PluginManager getPluginManager()
    {
        return Launcher.getPluginManager();
    }

}
