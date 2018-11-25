package iexport.helper.logging;

public class SoutLog implements Log
{
    @Override
    public void log (LogLevel lvl, Object s)
    {
        System.out.println(s);
    }
}
