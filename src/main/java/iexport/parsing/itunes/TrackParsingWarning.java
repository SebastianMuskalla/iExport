package iexport.parsing.itunes;

public class TrackParsingWarning extends Exception
{
    public TrackParsingWarning (String errorMessage)
    {
        super(errorMessage);
    }
}
