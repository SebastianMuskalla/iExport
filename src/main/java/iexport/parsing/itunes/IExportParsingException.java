package iexport.parsing.itunes;

public class IExportParsingException extends Exception
{
    public IExportParsingException ()
    {
    }

    public IExportParsingException (Exception cause)
    {
        super(cause);
    }

    public IExportParsingException (String errorMessage)
    {
        super(errorMessage);

    }

    public IExportParsingException (String errorMessage, Exception cause)
    {
        super(errorMessage, cause);
    }
}
