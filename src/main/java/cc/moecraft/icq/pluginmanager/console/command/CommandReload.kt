package cc.moecraft.icq.pluginmanager.console.command

import cc.moecraft.icq.pluginmanager.Launcher
import cc.moecraft.icq.pluginmanager.console.ConsoleCommand
import cc.moecraft.icq.pluginmanager.plugin.PluginLoader
import cc.moecraft.icq.pluginmanager.plugin.PluginManager

class CommandReload : ConsoleCommand {
    override fun onCommand(args: Array<String>) {
        Launcher.logger.warning("重启所有插件中。。。")
        for (plugin in PluginManager.enabledPlugins.values) {
            PluginLoader.disablePlugin(plugin)
        }
        PluginManager.enableAllPlugins()
    }
}