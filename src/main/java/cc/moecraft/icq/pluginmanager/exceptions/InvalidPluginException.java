package cc.moecraft.icq.pluginmanager.exceptions;

import lombok.NoArgsConstructor;

/**
 * 此类由 Hykilpikonna 在 2018/06/24 创建!
 * Created by Hykilpikonna on 2018/06/24!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
@NoArgsConstructor
public class InvalidPluginException extends Exception
{
    public InvalidPluginException(final Throwable cause)
    {
        super(cause);
    }
    public InvalidPluginException(final String message)
    {
        super(message);
    }
    public InvalidPluginException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
