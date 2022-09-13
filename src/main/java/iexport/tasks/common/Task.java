package iexport.tasks.common;

import iexport.itunes.Library;

public abstract class Task
{
    private final TaskSettings settings;
    private final Library library;

    public Task (Library library, TaskSettings settings)
    {
        this.library = library;
        this.settings = settings;
    }

    abstract public String getShorthand ();

    abstract public void run ();

    public Library getLibrary ()
    {
        return library;
    }
}
