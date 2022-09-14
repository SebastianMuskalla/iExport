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

package iexport;

import iexport.itunes.Library;
import iexport.logging.LogLevel;
import iexport.logging.Logging;
import iexport.parsing.ITunesParsingException;
import iexport.parsing.LibraryParser;
import iexport.settings.SettingsParser;
import iexport.settings.SettingsTriple;
import iexport.tasks.common.Task;
import iexport.tasks.common.TaskSettings;
import iexport.tasks.export.FileExportTask;
import iexport.tasks.playlistgen.PlaylistGenTask;
import iexport.tasks.print.LibraryPrinter;

import java.io.File;
import java.util.*;

public class IExport
{
    public static void main (String[] args)
    {
        // Initialize the global logger
        // We don't know which log level we should use yet, so we set it to IMPORTANT
        // to make sure we don't get unnecessary output.
        Logging.getLogger().setLogLevel(LogLevel.IMPORTANT);

        Logging.getLogger().important("ARGS");
        Logging.getLogger().important(Arrays.toString(args));

        // An invalid number of arguments has been specified.
        if (args.length > 2)
        {
            Logging.getLogger().important("Expected 0-2 additional arguments, got " + args.length + ":\n" + Arrays.toString(args));
            printUsage();
            System.exit(1);
        }
        else
        {
            Logging.getLogger().important("LOOKS GOOD");

            printUsage();

        }

        // Get the settings
        SettingsTriple settingsTriple = null;

        // At least one argument has been specified.
        // We expect this argument to be the path to the Settings file
        if (args.length > 0)
        {
            String yamlFilePathAsString = args[0];
            try
            {
                SettingsParser settingsParser = new SettingsParser(yamlFilePathAsString);
                settingsTriple = settingsParser.parse();
            }
            catch (Exception e)
            {
                Logging.getLogger().important("Parsing the .yaml file at the specified location \"" + yamlFilePathAsString + "\" has failed");
                Logging.getLogger().important("Reason: " + e.getMessage());
                System.exit(1);
            }
        }


        Logging.getLogger().important(settingsTriple.toString());


        String userprofile = System.getenv("USERPROFILE");
        String xmlPathString = userprofile + "\\Desktop\\iTunes Music Library.xml";

        Logging.getLogger().important(xmlPathString);

        File file = new File(xmlPathString);

        LibraryParser iTunesLibraryParser = new LibraryParser(file);
        Library library = null;

        // String yamlPathString = userprofile + "\\Desktop\\test.yaml";

//
//        Map<String, Object> map = (Map<String, Object>) load.loadFromString(jsonFileFileContent);
//
//        System.out.println(map);


        try
        {
            library = iTunesLibraryParser.parse();
        }
        catch (ITunesParsingException e)
        {
            e.printStackTrace();
        }

        TaskSettings taskSettings = new TaskSettings();


        Map<String, Task> tasks = new HashMap<>();

        Set<Task> taskList = new HashSet<>();

        taskList.add(new FileExportTask(library, taskSettings));
        taskList.add(new PlaylistGenTask(library, taskSettings));
        taskList.add(new LibraryPrinter(library, taskSettings));

        for (Task task : taskList)
        {
            tasks.put(task.getShorthand(), task);
        }

        String taskName;

        if (args.length > 0)
        {
            taskName = args[0];
        }
        else
        {
            Logging.getLogger().important("Interactive mode: Enter a task to execute. Pick among:");
            for (Task task : taskList)
            {
                System.out.print(" " + task.getShorthand());
            }
            Logging.getLogger().important("");

//            Scanner scanner = new Scanner(System.in);
//            taskName = scanner.next();
        }

        // TODO WTF
//         taskName = "fileexport";
        taskName = "print";

        Task task = tasks.get(taskName);

        if (task == null)
        {
            throw new RuntimeException("No task with name " + taskName);
        }

        Logging.getLogger().important("Executing task " + task.getShorthand());
        task.run();
    }

    private static void printUsage ()
    {
        // TODO -q -console plain
        Logging.getLogger().important("Usage: run --args=\"[PATH_TO_YAML_FILE] [TASK]\"");
        Logging.getLogger().important(1, "where");
        Logging.getLogger().important(1, "[PATH_TO_YAML_FILE] is the path to a YAML file with the settings that should be used");
        Logging.getLogger().important(2, "(e.g. '%USERPROFILE%\\Desktop\\iExportSettings.yaml')");
        Logging.getLogger().important(2, "See the provided iExportDefaultSettings.yaml for the available settings");
        Logging.getLogger().important(2, "If omitted, default values will be used.");
        Logging.getLogger().important(1, "[TASK] is the task that should be performed after parsing the library");
        Logging.getLogger().important(2, "If specified, it will overwrite the \"task\" field from the settings.");
        Logging.getLogger().important(2, "If not specified at all, interactive mode will be used.");
        Logging.getLogger().important(1, "Available tasks:");
        Logging.getLogger().important(2, "interactive - Select a task via STDIN");
        Logging.getLogger().important(2, "none - Simply check if parsing the library succeeds");
    }


}
