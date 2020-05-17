package cc.moecraft.icq.pluginmanager.console.command;

import cc.moecraft.icq.pluginmanager.Launcher;
import cc.moecraft.icq.pluginmanager.console.ConsoleCommand;

public class CommandStop implements ConsoleCommand {
    @Override
    public void onCommand(String[] args) {
        Launcher.logger.warning("退出 PicqBotX 中。。。。。");
        System.exit(0);
    }
}
