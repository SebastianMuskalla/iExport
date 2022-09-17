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

import iexport.tasks.fileexport.ExportFilesTask;
import iexport.tasks.generateplaylists.GeneratePlaylistsTask;
import iexport.tasks.printing.PrintLibraryTask;
import iexport.tasks.printing.PrintMultiplyListedTracksTask;
import iexport.tasks.printing.PrintPlaylistsTask;
import iexport.tasks.printing.PrintUnlistedTracksTask;

import java.util.*;

/**
 * A static class in which the available tasks are registered.
 */
public class TaskRegistry
{
    /**
     * A map in which the existing tasks are registered.
     */
    private static final Map<String, Task> tasksByName;

    /**
     * A list in which the existing tasks are registered.
     * <p>
     * We maintain this list in addition to {@link #tasksByName} to maintain the order
     */
    private static final List<Task> tasks;

    /**
     * The special help task that prints usage instructions.
     */
    private static final HelpTask theHelpTask;

    public static HelpTask getHelpTask ()
    {
        return theHelpTask;
    }

    public static Task getTask (String taskName)
    {
        return tasksByName.get(taskName);
    }

    static Collection<Task> getTaskList ()
    {
        return tasks;
    }

    private static void registerTask (Task task)
    {
        tasks.add(task);
        tasksByName.put(task.getTaskName(), task);
    }

    static
    {
        tasksByName = new HashMap<>();
        tasks = new ArrayList<>();

        HelpTask helpTask = new HelpTask();
        registerTask(helpTask);
        theHelpTask = helpTask;

        registerTask(new QuitTask());

        registerTask(new PrintLibraryTask());

        registerTask(new PrintPlaylistsTask());

        registerTask(new PrintUnlistedTracksTask());

        registerTask(new PrintMultiplyListedTracksTask());

        registerTask(new GeneratePlaylistsTask());

        registerTask(new ExportFilesTask());

        registerTask(new QuitTask());
    }
}
