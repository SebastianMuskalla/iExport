package iexport.helper.logging;

public class Logger
{
    private static Log instance = new SoutLog();

    static public void log (LogLevel lvl, Object s)
    {
        instance.log(lvl, s);
    }

    static public void log (Object m)
    {
        instance.log(m);
    }
}
