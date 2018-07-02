package cc.moecraft.icq.pluginmanager.plugin;

import cc.moecraft.icq.pluginmanager.exceptions.InvalidPluginYmlException;
import lombok.Data;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * 此类由 Hykilpikonna 在 2018/07/01 创建!
 * Created by Hykilpikonna on 2018/07/01!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
@Data
public class PluginYmlProperties
{
    // 插件名
    private String name;

    // 主类路径
    private String mainPath;

    /**
     * 从文件输入读取YML内容
     * @param inputStream 文件输入
     * @throws InvalidPluginYmlException Plugin.yml无效
     */
    public PluginYmlProperties(InputStream inputStream) throws InvalidPluginYmlException
    {
        Map<String, String> values = new Yaml().load(inputStream);

        if (!values.containsKey("main")) throw new InvalidPluginYmlException("Plugin.yml内没有定义main类路径");
        if (!values.containsKey("name")) throw new InvalidPluginYmlException("Plugin.yml内没有定义插件名");

        mainPath = values.get("main");
        name = values.get("name");
    }
}
