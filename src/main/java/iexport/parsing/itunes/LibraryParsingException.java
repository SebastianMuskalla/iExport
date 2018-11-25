package iexport.parsing.itunes;

public class LibraryParsingException extends Exception
{
    public LibraryParsingException ()
    {
    }

    ;

    public LibraryParsingException (Exception cause)
    {
        super(cause);
    }

    public LibraryParsingException (String errorMessage)
    {
        super(errorMessage);

    }

    public LibraryParsingException (String errorMessage, Exception cause)
    {
        super(errorMessage, cause);
    }
}
