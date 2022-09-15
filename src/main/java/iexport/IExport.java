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
import iexport.settings.*;
import iexport.tasks.HelpTask;
import iexport.tasks.Task;
import iexport.tasks.TaskRegistry;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class IExport
{
    /**
     * Strings that indicate that interactive move should be used.
     */
    public static final List<String> INTERACTIVE_MODE_NAMES = List.of("interactive", "", "\"\"", "''", "-i", "--i", "--interactive");

    /**
     * String that indicate that the special help task that prints usage instructions should be executed.
     */
    public static final List<String> HELP_TASK_NAMES = List.of("HELP", "-h", "--h", "-help", "--help", "usage", "man");

    /**
     * Run iExport.
     *
     * @param args we expect {@code args} to have length 0-2
     *             <p>
     *             Length 0: Use default settings & interactive mode.
     *             Length 1: Argument is path to settings.yaml file, which may specify the task to run.
     *             Length 2: First argument is path to settings.yaml file, second argument is task name.
     *             <p>
     *             If one of the arguments is from {@link #HELP_TASK_NAMES}, we always print the help.
     */
    public static void main (String[] args)
    {
        // Initialize the global logger
        // We don't know which log level we should use yet, so we set it to NORMAL
        // to make sure we don't get unnecessary output.
        Logging.getLogger().setLogLevel(LogLevel.NORMAL);

        HelpTask helpTask = TaskRegistry.getHelpTask();

        // An invalid number of arguments has been specified.
        if (args.length > 2)
        {
            // Print usage instructions
            Logging.getLogger().important("Expected 0-2 additional arguments, got " + args.length + ":\n" + Arrays.toString(args));
            helpTask.run(null, null);
            System.exit(1);
        }

        // Check whether one of the arguments is in HELP_TASK_NAMES
        // In this case, we can print usage instructions and don't even have to parse the settings
        if (Arrays.stream(args).anyMatch((s) -> HELP_TASK_NAMES.stream().anyMatch(s::equalsIgnoreCase)))
        {
            helpTask.run(null, null);
            System.exit(0);
        }

        // Construct the settings
        SettingsTriple settingsTriple;
        if (args.length == 0)
        {
            // No argument has been specified, use default settings
            Logging.getLogger().important("No .yaml file provided, using all default settings");
            settingsTriple = new SettingsTriple(new GeneralSettings(), new ParsingSettings(), new HashMap<>());
        }
        else
        {
            // At least one argument has been specified
            // We expect the first argument to be the path to the settings .yaml file
            settingsTriple = parseSettings(args[0]);
        }

        // Get the settings (either parse them if the command line argument is specified, or use default values)
        GeneralSettings generalSettings = settingsTriple.generalSettings();

        // We can now get the log level that should be used for the rest of the execution
        LogLevel logLevel = generalSettings.getLogLevel();

        Logging.getLogger().setLogLevel(logLevel);
        Logging.getLogger().info("Using logLevel " + logLevel);

        // Notify the user of any settings that have been set in the .yaml file but that actually do not exist
        reportPotentialMistakesInSettings(settingsTriple);

        // Check if a task is specified in the settings .yaml file
        String taskName = generalSettings.getTaskName();

        if (args.length > 1)
        {
            // Get the second argument as the task name
            // If present, it overwrites the task from the .yaml file
            taskName = args[1];
        }

        // Check if the task we should execute is simply "help" - In this case, we do not have to parse the library
        if (HELP_TASK_NAMES.stream().anyMatch(taskName::equalsIgnoreCase))
        {
            helpTask.run(null, null);
            System.exit(0);
        }

        // The task we should execute is not help, we should parse the library now
        String libraryXmlFilePathString = settingsTriple.parsingSettings().getLibraryXmlFilePathString();

        Logging.getLogger().message("Trying to parse the iTunes library .xml file at " + libraryXmlFilePathString);

        long startParsing = System.nanoTime();

        File file = new File(libraryXmlFilePathString);
        LibraryParser iTunesLibraryParser = new LibraryParser(file);

        Library library = null;
        try
        {
            library = iTunesLibraryParser.parse();
        }
        catch (ITunesParsingException e)
        {
            e.printStackTrace();
            System.exit(1);
            return;
        }

        long endParsing = System.nanoTime();
        double parsingDurationInSeconds = ((double) ((endParsing - startParsing) / 1000000)) / 1000; // with 3 decimal digits
        Logging.getLogger().message("Successfully parsed the iTunes library");
        Logging.getLogger().info("Parsing took " + parsingDurationInSeconds + "s");

        int iterations = 0;

        boolean startedInInteractiveMode = INTERACTIVE_MODE_NAMES.stream().anyMatch(taskName::equalsIgnoreCase);

        // if the task name matches "interactive", we should query the user for a new task name
        while (INTERACTIVE_MODE_NAMES.stream().anyMatch(taskName::equalsIgnoreCase))
        {
            iterations++;
            if (iterations > 3)
            {
                easterEgg();
            }

            taskName = runInteractiveMode();
        }

        do
        {
            // Try to get the task with the specified name
            Task task = TaskRegistry.getTask(taskName);

            if (task == null)
            {
                // The task with this name does not exist
                Logging.getLogger().important("No task with the name \"" + taskName + "\" exists.");
                if (startedInInteractiveMode)
                {
                    // if the user was in interactive mode, we will give them another chance
                    taskName = runInteractiveMode();
                }
                else
                {
                    // Otherwise we print the list of available tasks once again and crash
                    helpTask.printListOfTasks(false);
                    System.exit(1);
                }
            }
            else
            {
                // The task does exist and we should run it
                long startTask = System.nanoTime();

                // Get the appropriate settings
                RawTaskSettings taskSettings = settingsTriple.taskSettings().get(taskName);

                if (taskSettings == null)
                {
                    // Settings are not set, generate default settings
                    taskSettings = new RawTaskSettings(taskName);
                }

                task.run(library, taskSettings);

                long endTask = System.nanoTime();
                double taskDurationInSeconds = ((double) ((endTask - startTask) / 1000000)) / 1000; // with 3 decimal digits
                Logging.getLogger().message("Successfully executed task " + taskName);
                Logging.getLogger().info("Task took " + taskDurationInSeconds + "s");
                System.exit(0);
            }
        }
        while (startedInInteractiveMode);

    }

    /**
     * Run interactive mode, i.e. print the list of available tasks and let the user decide.
     *
     * @return the task name given by the user
     */
    private static String runInteractiveMode ()
    {
        Logging.getLogger().message("Please specify a task name");
        TaskRegistry.getHelpTask().printListOfTasks(false);
        System.out.print("Type a task name: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    /**
     * Parse the settings .yaml file at the specified location.
     * <p>
     * Note that {@link SettingsParser} will apply {@link Settings#applyUserProfileReplacement(String)},
     * so we can use %USERPROFILE% in the path and it will get expanded,
     * e.g. to C:\Users\YourUserName
     *
     * @param yamlFilePathAsString the path to the settings .yaml file as a String
     * @return the parsed settings
     */
    private static SettingsTriple parseSettings (String yamlFilePathAsString)
    {
        try
        {
            SettingsParser settingsParser = new SettingsParser(yamlFilePathAsString);
            return settingsParser.parse();
        }
        catch (Exception e)
        {
            Logging.getLogger().important("Parsing the .yaml file at the specified location \"" + yamlFilePathAsString + "\" has failed");
            Logging.getLogger().message("Because of: " + e.getMessage());
            System.exit(1);
            return null; // Unreachable
        }
    }

    /**
     * Notify the user if the user-specified settings contain potential mistakes.
     * <p>
     * Notify the user if default settings are used for parsing or in general.
     * <p>
     * Notify the user if the {@link ParsingSettings} or {@link GeneralSettings} contained inside {@code settingsTriple}
     * contain settings that have been specified by the user (via the .yaml settings file),
     * but that are actually not valid iExport settings
     *
     * @param settingsTriple the triple of settings
     */
    private static void reportPotentialMistakesInSettings (SettingsTriple settingsTriple)
    {
        GeneralSettings generalSettings = settingsTriple.generalSettings();
        if (generalSettings.isDefault())
        {
            Logging.getLogger().info("No general settings have been specified in the .yaml file, using all default settings from now on");
        }
        else
        {
            for (String key : generalSettings.unusedSettings())
            {
                Logging.getLogger().info("Settings for key \"" + generalSettings.getYamlPath(key) + "\""
                        + " specified in .yaml file, but it is not used by iExport");
            }
        }

        ParsingSettings parsingSettings = settingsTriple.parsingSettings();
        if (generalSettings.isDefault())
        {
            Logging.getLogger().info("No parsings settings have been specified in the .yaml file (key \"parsing\"), using all default settings from now on");
        }
        else
        {
            for (String key : parsingSettings.unusedSettings())
            {
                Logging.getLogger().info("Settings for key \"" + parsingSettings.getYamlPath(key) + "\""
                        + " specified in .yaml file, but it is not used by iExport");
            }
        }
    }

    /**
     * HMPH, you're a big baka!
     */
    private static void easterEgg ()
    {
        Logging.getLogger().message("########     ###    ##    ##    ###    #### #### ####");
        Logging.getLogger().message("##     ##   ## ##   ##   ##    ## ##   #### #### ####");
        Logging.getLogger().message("##     ##  ##   ##  ##  ##    ##   ##  #### #### ####");
        Logging.getLogger().message("########  ##     ## #####    ##     ##  ##   ##   ## ");
        Logging.getLogger().message("##     ## ######### ##  ##   #########               ");
        Logging.getLogger().message("##     ## ##     ## ##   ##  ##     ## #### #### ####");
        Logging.getLogger().message("########  ##     ## ##    ## ##     ## #### #### ####");
        System.exit(1337 % 167);
    }
}
