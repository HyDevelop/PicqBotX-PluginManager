package cc.moecraft.icq.pluginmanager;

import cc.moecraft.yaml.Config;

/**
 * 此类由 Hykilpikonna 在 2018/07/02 创建!
 * Created by Hykilpikonna on 2018/07/02!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class LauncherConfig extends Config
{
    public LauncherConfig()
    {
        super("./", "config", "yml", false, false, false);
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
