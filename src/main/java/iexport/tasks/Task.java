/*
 * Copyright 2014-2022 Sebastian Muskalla
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package iexport.tasks;

import iexport.itunes.Library;
import iexport.settings.RawTaskSettings;

/**
 * An interface for tasks that can be executed on the iTunes library.
 */
public abstract class Task
{
    /**
     * The library this task should be executed on.
     */
    protected Library library = null;
    /**
     * The settings (from the setting .yaml file) for this task.
     * <p>
     * An implementation of this class may want to convert the object of class {@link RawTaskSettings}
     * to an object of {@link iexport.settings.TaskSettings} upon initialization.
     */
    protected RawTaskSettings rawTaskSettings;
    /**
     * The internal state of the task.
     */
    private TaskState state = TaskState.UNINITIALIZED;

    /**
     * The name of the task.
     *
     * @return the name
     */
    public abstract String getTaskName ();

    /**
     * A short one-line description of the task.
     *
     * @return the description
     */
    public abstract String getDescription ();

    /**
     * Initialize this task by providing the library and the settings.
     *
     * @param library         the library
     * @param rawTaskSettings the parsed settings
     */
    public void initialize (Library library, RawTaskSettings rawTaskSettings)
    {
        if (state != TaskState.UNINITIALIZED)
        {
            throw new RuntimeException("Task " + this + " has already been initialized");
        }

        this.library = library;
        this.rawTaskSettings = rawTaskSettings;

        // This task is now ready to be executed.
        state = TaskState.READY;
    }

    /**
     * Run the task after checking that it is {@link TaskState#READY}.
     */
    public void execute ()
    {
        switch (state)
        {
            case UNINITIALIZED -> throw new RuntimeException("Task " + this + " has not been inititalized yet");
            case DONE -> throw new RuntimeException("Task " + this + " has already been executed.");
            case READY ->
            {
                // Actually run the task
                run();
                state = TaskState.DONE;
            }
        }
    }

    /**
     * Report problems that exist in the settings
     * (e.g. settings that have been provided by the user but that do not actually exist).
     */
    public void reportProblems ()
    {
        if (state != TaskState.READY)
        {
            throw new RuntimeException("reportProblems() should be called when the task has been initialized, " +
                    "but not yet executed");
        }
    }

    /**
     * Internal method that runs the task.
     * <p>
     * This should be called via the {@link #execute()} wrapper
     * to make sure that the task is {@link TaskState#READY} to run.
     */
    abstract protected void run ();

}
