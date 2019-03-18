package iexport.helper.logging;

public class Logger
{
    private static Log instance = new SoutLog();

    static public void log (LogLevel lvl, Object message)
    {
        instance.log(lvl, message);
    }

    static public void log (Object message)
    {
        instance.log(LogLevel.ERROR, message);
    }

    public static void log (LogLevel lvl, int indentation, String message)
    {
        String indent = "";
        for (int i = 0; i < indentation; i++)
        {
            indent += "    ";
        }

        instance.log(lvl, indent + message);
    }
}
