package cc.moecraft.icq.pluginmanager.console

interface ConsoleCommand {
    fun onCommand(args: Array<String?>?)
}