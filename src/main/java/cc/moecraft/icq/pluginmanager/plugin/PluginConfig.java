package cc.moecraft.icq.pluginmanager.plugin;

import cc.moecraft.yaml.Config;

import java.io.File;

/**
 * 此类由 Hykilpikonna 在 2018/07/15 创建!
 * Created by Hykilpikonna on 2018/07/15!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class PluginConfig extends Config
{
    public PluginConfig(IcqPlugin plugin)
    {
        super(plugin.getDataFolder().getParentFile().getName() + File.separator + plugin.getDataFolder().getName(),
                "config", "yml", false, false, true);
    }

    @Override
    public void readConfig()
    {

    }

    @Override
    public void writeDefaultConfig()
    {

    }
}
