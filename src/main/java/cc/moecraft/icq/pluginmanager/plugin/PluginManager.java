package cc.moecraft.icq.pluginmanager.plugin;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.pluginmanager.exceptions.InvalidPluginException;
import cc.moecraft.icq.pluginmanager.exceptions.InvalidPluginYmlException;
import com.google.common.io.PatternFilenameFilter;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

}
