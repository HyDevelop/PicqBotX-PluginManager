package cc.moecraft.icq.pluginmanager.plugin;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.pluginmanager.exceptions.InvalidPluginException;
import com.google.common.io.ByteStreams;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
final class PluginClassLoader extends URLClassLoader
{
    private final PluginLoader loader;
    private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
    private final PluginYmlProperties pluginYmlProperties;
    private final File dataFolder;
    private final File file;
    private final JarFile jar;
    private final Manifest manifest;
    private final URL url;
    final IcqPlugin plugin;
    private IcqPlugin pluginInit;
    private IllegalStateException pluginState;

    PluginClassLoader(PluginLoader loader, ClassLoader parent, PluginYmlProperties pluginYmlProperties, File dataFolder, File file)
            throws IOException, InvalidPluginException
    {
        super(new URL[] {file.toURI().toURL()}, parent);
        Validate.notNull(loader, "Loader未传入");

        this.loader = loader;
        this.pluginYmlProperties = pluginYmlProperties;
        this.dataFolder = dataFolder;
        this.file = file;
        this.jar = new JarFile(file);
        this.manifest = jar.getManifest();
        this.url = file.toURI().toURL();

        try
        {
            Class<?> jarClass;

            try
            {
                jarClass = Class.forName(pluginYmlProperties.getMainPath(), true, this);
            }
            catch (ClassNotFoundException ex)
            {
                throw new InvalidPluginException("Cannot find main class `" + pluginYmlProperties.getMainPath() + "'", ex);
            }

            Class<? extends IcqPlugin> pluginClass;

            try
            {
                pluginClass = jarClass.asSubclass(IcqPlugin.class);
            }
            catch (ClassCastException ex)
            {
                throw new InvalidPluginException("Main class `" + pluginYmlProperties.getMainPath() + "' does not extend IcqPlugin", ex);
            }

            plugin = pluginClass.newInstance();

            plugin.setLoader(loader);
            plugin.setBot(loader.getBot());
            plugin.setDescription(pluginYmlProperties);
            plugin.setDataFolder(dataFolder);
            plugin.setFile(file);
            plugin.setClassLoader(this);
            plugin.setLogger(loader.getBot().getLogger());
        }
        catch (IllegalAccessException ex)
        {
            throw new InvalidPluginException("No public constructor", ex);
        }
        catch (InstantiationException ex)
        {
            throw new InvalidPluginException("Abnormal plugin type", ex);
        }
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException
    {
        return findClass(name, true);
    }

    public Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException
    {
        Class<?> result = classes.get(name);

        if (result == null)
        {
            if (checkGlobal) result = loader.getClassByName(name);

            if (result == null)
            {
                String path = name.replace('.', '/').concat(".class");
                JarEntry entry = jar.getJarEntry(path);

                if (entry != null)
                {
                    byte[] classBytes;

                    try (InputStream is = jar.getInputStream(entry))
                    {
                        classBytes = ByteStreams.toByteArray(is);
                    }
                    catch (IOException ex)
                    {
                        throw new ClassNotFoundException(name, ex);
                    }

                    int dot = name.lastIndexOf('.');

                    if (dot != -1)
                    {
                        String pkgName = name.substring(0, dot);

                        if (getPackage(pkgName) == null)
                        {
                            try
                            {
                                if (manifest != null) definePackage(pkgName, manifest, url);
                                else definePackage(pkgName, null, null, null, null, null, null, null);
                            }
                            catch (IllegalArgumentException ex)
                            {
                                if (getPackage(pkgName) == null) throw new IllegalStateException("Cannot find package " + pkgName);
                            }
                        }
                    }

                    CodeSigner[] signers = entry.getCodeSigners();
                    CodeSource source = new CodeSource(url, signers);

                    result = defineClass(name, classBytes, 0, classBytes.length, source);
                }

                if (result == null) result = super.findClass(name);
                if (result != null) loader.setClass(name, result);
            }

            classes.put(name, result);
        }

        return result;
    }

    @Override
    public void close() throws IOException
    {
        try
        {
            super.close();
        }
        finally
        {
            jar.close();
        }
    }

    public Set<String> getClasses()
    {
        return classes.keySet();
    }

    synchronized void initialize(IcqPlugin IcqPlugin)
    {
        Validate.notNull(IcqPlugin, "Initializing plugin cannot be null");
        Validate.isTrue(IcqPlugin.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");

        if (this.plugin != null || this.pluginInit != null) throw new IllegalArgumentException("插件已经加载!", pluginState);

        pluginState = new IllegalStateException("Initial initialization");
        this.pluginInit = IcqPlugin;

        IcqPlugin.init(loader, loader.getBot(), pluginYmlProperties, dataFolder, file, this);
    }
}
