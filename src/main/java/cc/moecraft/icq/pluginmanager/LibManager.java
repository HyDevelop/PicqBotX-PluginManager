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

    /**
     * 将一个对象注册成一个库
     * @param libClass 库类 (使用这个库的插件会用来查找的)
     * @param api 库对象
     * @param <T> 库类型
     */
    public <T> void registerLib(Class<T> libClass, T api)
    {
        registeredLibTypeIndex.put(libClass, api);
    }

    /**
     * 获取一个库对象
     * @param libClass 库类
     * @param <T> 库类型
     * @return 库对象
     * @throws LibNotFoundException 未找到
     */
    @SuppressWarnings("unchecked")
    public <T> T getLib(Class<T> libClass) throws LibNotFoundException
    {
        if (!registeredLibTypeIndex.containsKey(libClass)) throw new LibNotFoundException(libClass);
        return (T) registeredLibTypeIndex.get(libClass);
    }
}
