package iexport.helper.logging;

/**
 * @author Sebastian
 */
public interface Log
{
    public void log (LogLevel lvl, Object s);

    default public void log (Object m)
    {
        log(LogLevel.VERBOSE, m);
    }
}
