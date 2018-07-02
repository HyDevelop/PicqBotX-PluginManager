package cc.moecraft.icq.pluginmanager.plugin;

import cc.moecraft.icq.command.interfaces.IcqCommand;
import cc.moecraft.icq.event.IcqListener;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.FileInputStream;

/**
 * 此类由 Hykilpikonna 在 2018/06/17 创建!
 * Created by Hykilpikonna on 2018/06/17!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
@AllArgsConstructor @Data
public class PluginProperties
{
    // 所有要注册的指令
    private IcqCommand[] commandsPackage;

    // 所有要注册的监听器
    private IcqListener[] listeners;
}
