package iexport.helper.logging;


public class DevNullLogger implements Log
{
    @Override
    public void log (LogLevel lvl, Object s)
    {
        // dev/null (-:
    }
}
