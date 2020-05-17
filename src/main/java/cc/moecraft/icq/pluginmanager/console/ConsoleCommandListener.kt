package cc.moecraft.icq.pluginmanager.console;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleCommandListener {
    HashMap<String, ConsoleCommand> answers = new HashMap<>();
    Scanner scanner;


    public void addCommand(String cmd, ConsoleCommand command) {
        answers.put(cmd.toLowerCase(), command);
    }


    public ConsoleCommandListener(Scanner scanner) {
        this.scanner = scanner;

        if (scanner == null) {
            throw new NullPointerException("Null");
        }
    }

    public void removeCommand(String cmd, ConsoleCommand command) {
        answers.remove(cmd, command);
    }

    public ConsoleCommand replaceCommand(String cmd, ConsoleCommand command) {
        return answers.replace(cmd, command);
    }

    public void listenInNewThread() {
        Thread t = new Thread() {
            public void run() {
                listen();
            }
        };
        t.start();
    }


    public void listen() {
        while (true) {
            String line;
            try {
                 line = scanner.nextLine();
            } catch (NoSuchElementException ignored) {
                line = "";
            }

            String input = line.replaceAll("[\\s]+", " ");

            String[] args = input.split(" ");
            String cmd = args[0];

            ConsoleCommand command = answers.get(cmd.toLowerCase());
            if (command != null) {
                command.onCommand(input.replaceFirst(cmd + " ", "").split(" "));
            }

        }
    }

}