package cc.moecraft.icq.pluginmanager.console

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
            val args = input.split(" ".toRegex()).toTypedArray()
            var cmd = args[0]
            if (cmd.startsWith("/")) {
                cmd = args[0].replaceFirst("/", "")
                val command = answers[cmd.toLowerCase()]
                command?.onCommand(input.replaceFirst("/" + cmd + " ".toRegex(), "").split(" ".toRegex()).toTypedArray())
            }

        }
    }

    init {
        if (scanner == null) {
            throw NullPointerException("Null")
        }
    }
}