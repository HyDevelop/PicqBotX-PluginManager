package cc.moecraft.icq.pluginmanager.console

interface ConsoleCommand {
    /**
     * @param args Arguments
     */
    fun onCommand(args: Array<String?>?)
}