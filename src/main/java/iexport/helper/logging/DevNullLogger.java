package iexport.helper.logging;

/**
 * @author Sebastian Muskalla
 */
public class DevNullLogger implements Log
{
    @Override
    public void log (LogLevel lvl, Object s)
    {
        // dev/null (-:
    }
}
