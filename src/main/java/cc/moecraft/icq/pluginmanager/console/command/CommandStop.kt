package cc.moecraft.icq.pluginmanager.console.command

import cc.moecraft.icq.pluginmanager.Launcher
import cc.moecraft.icq.pluginmanager.console.ConsoleCommand
import kotlin.system.exitProcess

class CommandStop : ConsoleCommand {
    override fun onCommand(args: Array<String>) {
        Launcher.logger.warning("退出 PicqBotX 中。。。。。")
        exitProcess(0)
    }
}