package cc.moecraft.icq.pluginmanager.plugin;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.pluginmanager.exceptions.InvalidPluginException;
import cc.moecraft.icq.pluginmanager.exceptions.InvalidPluginYmlException;
import cc.moecraft.logger.format.AnsiColor;
import lombok.Data;
import org.apache.commons.lang3.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * 此类由 Hykilpikonna 在 2018/06/17 创建!
 * Created by Hykilpikonna on 2018/06/17!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
@Data
public class PluginLoader {
    private final Pattern[] fileFilters = new Pattern[]{Pattern.compile("\\.jar$"),};
    private static final Map<String, Class<?>> classes = new HashMap<>();
    private static final List<PluginClassLoader> loaders = new CopyOnWriteArrayList<>();

    private PicqBotX bot;

    public PluginLoader(PicqBotX bot) {
        this.bot = bot;
    }

    public IcqPlugin loadPlugin(final File file) throws InvalidPluginException, InvalidPluginYmlException {
        Validate.notNull(file, "插件文件对象未指定");

        if (!file.exists()) throw new InvalidPluginException(new FileNotFoundException("文件 " + file.getPath() + " 不存在!"));

        // 从插件resource内的plugin.yml读取插件名和主类名
        final PluginYmlProperties pluginYmlProperties = getPluginYmlProperties(file);

        final File parentFile = file.getParentFile();
        final File dataFolder = new File(parentFile, pluginYmlProperties.getName());
        if (dataFolder.exists() && !dataFolder.isDirectory()) throw new InvalidPluginException(String.format(
                    "插件 %s (%s) 的数据文件夹 %s 存在而且不是路径", pluginYmlProperties.getName(), file, dataFolder));

        final PluginClassLoader loader;

        try
        {
            // 丢给PluginClassLoader加载
            loader = new PluginClassLoader(this, getClass().getClassLoader(), pluginYmlProperties, dataFolder, file);
        }
        catch (InvalidPluginException ex)
        {
            throw ex;
        }
        catch (Throwable ex)
        {
            throw new InvalidPluginException(ex);
        }

        loaders.add(loader);

        return loader.plugin;
    }

    public PluginYmlProperties getPluginYmlProperties(File file) throws InvalidPluginYmlException
    {
        Validate.notNull(file, "插件文件对象未指定");

        JarFile jarFile = null;
        InputStream inputStream = null;

        try
        {
            jarFile = new JarFile(file);
            JarEntry entry = jarFile.getJarEntry("plugin.yml");

            if (entry == null)
            {
                throw new InvalidPluginYmlException("插件里没有找到 plugin.yml",
                        new FileNotFoundException("插件里没有找到 plugin.yml, 如果你是开发者, 请看这里: [教程链接还没写ww]")); //TODO: 写教程链接
            }

            inputStream = jarFile.getInputStream(entry);

            return new PluginYmlProperties(inputStream);
        }
        catch (IOException | YAMLException ex)
        {
            throw new InvalidPluginYmlException("无法读取文件或者YML非法", ex);
        }
        finally
        {
            if (jarFile != null)
            {
                try
                {
                    jarFile.close();
                }
                catch (IOException ignored) {}
            }
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException ignored) {}
            }
        }
    }

    public Pattern[] getPluginFileFilters() {
        return fileFilters.clone();
    }

    public Class<?> getClassByName(final String name)
    {
        Class<?> cachedClass = classes.get(name);

        if (cachedClass != null) return cachedClass;
        else for (PluginClassLoader loader : loaders)
        {
            try
            {
                cachedClass = loader.findClass(name, false);
            }
            catch (ClassNotFoundException ignored) {}
            if (cachedClass != null) return cachedClass;
        }
        return null;
    }

    public void setClass(final String name, final Class<?> clazz)
    {
        if (!classes.containsKey(name))
        {
            classes.put(name, clazz);

            if (ConfigurationSerializable.class.isAssignableFrom(clazz))
            {
                Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
                ConfigurationSerialization.registerClass(serializable);
            }
        }
    }

    private static void removeClass(String name) {
        Class<?> clazz = classes.remove(name);

        try {
            if ((clazz != null) && (ConfigurationSerializable.class.isAssignableFrom(clazz))) {
                Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
                ConfigurationSerialization.unregisterClass(serializable);
            }
        }
        catch (NullPointerException ex)
        {
            // Boggle!
            // (Native methods throwing NPEs is not fun when you can't stop it before-hand)
        }
    }

    public void enablePlugin(IcqPlugin plugin)
    {
        Validate.isTrue(plugin != null, "传入插件为null");

        if (!plugin.isEnabled())
        {
            plugin.getLogger().log(AnsiColor.YELLOW + "插件 " + plugin.getDescription().getName() + " 正在加载 ...");

            PluginClassLoader pluginLoader = (PluginClassLoader) plugin.getClassLoader();

            if (!loaders.contains(pluginLoader))
            {
                loaders.add(pluginLoader);
                plugin.getLogger().error("正在用一个未注册的插件加载器加载插件: " + plugin.getDescription().getName());
            }

            try
            {
                plugin.setEnabled(true);
            }
            catch (Throwable ex)
            {
                plugin.getLogger().error("加载插件 " + plugin.getDescription().getName() + " 时发生错误: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public static void disablePlugin(IcqPlugin plugin) {
        Validate.isTrue(plugin != null, "传入插件为null");

        if (plugin.isEnabled()) {
            String message = String.format("%s正在卸载 %s 插件 ...", AnsiColor.YELLOW, plugin.getDescription().getName());
            plugin.getLogger().log(message);

            ClassLoader classLoader = plugin.getClassLoader();

            try
            {
                plugin.setEnabled(false);
            }
            catch (Throwable ex)
            {
                plugin.getLogger().error("卸载插件 " + plugin.getDescription().getName() + " 时发生错误: " + ex.getMessage());
                ex.printStackTrace();
            }

            if (classLoader instanceof PluginClassLoader)
            {
                PluginClassLoader loader = (PluginClassLoader) classLoader;
                loaders.remove(loader);

                Set<String> names = loader.getClasses();

                for (String name : names) removeClass(name);
            }
        }
    }
}
