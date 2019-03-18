package iexport.tasks.common;

import iexport.domain.Library;

public abstract class Task
{
    public final String shorthand;
    private final TaskSettings settings;
    private final Library library;

    public Task (String shorthand, Library library)
    {
        this.shorthand = shorthand;
        this.library = library;
        this.settings = null;
    }

    public Task (String shorthand, Library library, TaskSettings settings)
    {
        this.shorthand = shorthand;
        this.library = library;
        this.settings = settings;
    }

    abstract public void run ();

    public Library getLibrary ()
    {
        return library;
    }
}
