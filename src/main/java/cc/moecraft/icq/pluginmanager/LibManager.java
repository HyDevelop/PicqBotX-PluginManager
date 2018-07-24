package cc.moecraft.icq.pluginmanager;

import cc.moecraft.icq.pluginmanager.exceptions.LibNotFoundException;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类由 Hykilpikonna 在 2018/07/15 创建!
 * Created by Hykilpikonna on 2018/07/15!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class LibManager
{
    @Getter
    private Map<Class<?>, Object> registeredLibTypeIndex = new HashMap<>();
}
