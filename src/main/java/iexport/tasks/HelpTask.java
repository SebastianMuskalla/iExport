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

import iexport.IExport;
import iexport.itunes.Library;
import iexport.logging.Logging;
import iexport.settings.RawTaskSettings;

import java.util.Collection;

/**
 * A simple task that just prints usage instructions for iExport
 */
public class HelpTask implements Task
{
    @Override
    public String getTaskName ()
    {
        return "help";
    }

    @Override
    public String getDescription ()
    {
        return "show usage instructions";
    }

    @Override
    public void run (Library library, RawTaskSettings rawTaskSettings)
    {
        Logging.getLogger().message("Usage: gradle run --args=\"[PATH_TO_YAML_FILE] [TASK]\" run -q --console plain");
        Logging.getLogger().message(1, "where");
        Logging.getLogger().message(1, "[PATH_TO_YAML_FILE] is the path to a YAML file with the settings that should be used");
        Logging.getLogger().message(2, "(e.g. '%USERPROFILE%\\Desktop\\iExportSettings.yaml')");
        Logging.getLogger().message(2, "See the provided iExportDefaultSettings.yaml for the available settings");
        Logging.getLogger().message(2, "If omitted, default values will be used.");
        Logging.getLogger().message(1, "[TASK] is the task that should be performed after parsing the library");
        Logging.getLogger().message(2, "If specified, it will overwrite the \"task\" field from the settings.");
        Logging.getLogger().message(2, "If not specified at all, interactive mode will be used.");
        printListOfTasks(true);
    }

    /**
     * Prints the list of tasks registered in the {@link TaskRegistry}.
     *
     * @param includeInteractive whether "interactive" should be included in that list
     */
    public void printListOfTasks (boolean includeInteractive)
    {
        final int INDENTATION = 1;

        Logging.getLogger().message("Available tasks:");

        Collection<Task> tasks = TaskRegistry.getTaskList();

        // Find the longest task name, which we need for formatting
        int maxTaskNameLength = tasks.stream().map(Task::getTaskName).mapToInt(String::length).max().orElse(0);

        // If includeInteractive is set, we also print a line for interactive
        if (includeInteractive)
        {
            String interactiveString = IExport.INTERACTIVE_MODE_NAMES.get(0);
            maxTaskNameLength = Integer.max(maxTaskNameLength, interactiveString.length());
            Logging.getLogger().message(INDENTATION,
                    padWithSpaces(interactiveString, maxTaskNameLength) + " - " + "specify task on STDIN");
        }

        // Print name and description for each task
        for (Task task : tasks)
        {
            Logging.getLogger().message(INDENTATION,
                    padWithSpaces(task.getTaskName(), maxTaskNameLength) + " - " + task.getDescription());
        }
    }

    /**
     * Pads a given string to the desired length using spaces.
     * <p>
     * If the string already has or exceeds the desired length, it remains unchanged.
     *
     * @param string        the string that should be padded
     * @param desiredLength the length to which it should be padded
     * @return the padded string
     */
    private String padWithSpaces (String string, int desiredLength)
    {
        int repeats = desiredLength - string.length();
        if (repeats < 0)
        {
            repeats = 0;
        }
        return string + " ".repeat(repeats);
    }
}
