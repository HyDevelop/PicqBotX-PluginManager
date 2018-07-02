package cc.moecraft.icq.pluginmanager.exceptions;

/**
 * 此类由 Hykilpikonna 在 2018/06/24 创建!
 * Created by Hykilpikonna on 2018/06/24!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class InvalidPluginYmlException extends Exception
{
    public InvalidPluginYmlException(String reason, Throwable cause)
    {
        super(" 插件的plugin.yml读取失败: " + reason, cause);
    }

    public InvalidPluginYmlException(String reason)
    {
        super(" 插件的plugin.yml读取失败: " + reason);
    }
}
