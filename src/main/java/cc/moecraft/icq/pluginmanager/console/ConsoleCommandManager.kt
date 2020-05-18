package cc.moecraft.icq.pluginmanager.console

import cc.moecraft.icq.pluginmanager.Launcher
import java.util.*


class ConsoleCommandManager(var scanner: Scanner?) {
    var answers = HashMap<String, ConsoleCommand>()

    /**
     * @param cmd Command Name
     * @param command Console Command Class
     */
    fun addCommand(cmd: String, command: ConsoleCommand) {
        answers[cmd.toLowerCase()] = command
    }

    /**
     * @param cmd Command Name
     * @param command Console Command Class
     */
    fun removeCommand(cmd: String, command: ConsoleCommand) {
        answers.remove(cmd, command)
    }

    /**
     * @param cmd Command Name
     * @param command Console Command Class
     */
    fun replaceCommand(cmd: String, command: ConsoleCommand): ConsoleCommand? {
        return answers.replace(cmd, command)
    }

    fun listenInNewThread() {
        val t: Thread = object : Thread() {
            override fun run() {
                listen()
            }
        }
        t.start()
    }

    fun listen() {
        while (true) {
            val line: String = try {
                scanner!!.nextLine()
            } catch (ignored: NoSuchElementException) {
                ""
            }
            val input = line.replace("[\\s]+".toRegex(), " ")
            dispatchCommand(input)
        }
    }

    /**
     * @param command_input Run Command
     */

    fun dispatchCommand(command_input: String) {
        val args = command_input.split(" ").toTypedArray()
        var cmd_start = args[0]
        if (cmd_start.startsWith("/")) {
            Launcher.logger.log("Console issued command: $command_input")
            val cmd = cmd_start.replaceFirst("/", "")
            val command = answers[cmd.toLowerCase()]
            if (command != null) {
                val arg: Array<String> = command_input.replaceFirst("/$cmd_start", "").split(" ").toTypedArray()
                command.onCommand(arg)
            }
        }

    }

    init {
        if (scanner == null) {
            throw NullPointerException("Null")
        }
    }
}