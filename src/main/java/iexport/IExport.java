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

import com.dd.plist.*;
import iexport.itunes.Library;
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
        System.out.println("ARGS");
        System.out.println(Arrays.toString(args));

        // An invalid number of arguments has been specified.
        if (args.length > 2)
        {
            System.out.println("Expected 0-2 additional arguments, got " + args.length + ":\n" + Arrays.toString(args));
            printUsage();
            System.exit(1);
        }
        else
        {
            System.out.println("LOOKS GOOD");

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
                System.out.println("Parsing the .yaml file at the specified location \"" + yamlFilePathAsString + "\" has failed");
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }


        System.out.println(settingsTriple);


        String userprofile = System.getenv("USERPROFILE");
        String xmlPathString = userprofile + "\\Desktop\\iTunes Music Library.xml";

        System.out.println(xmlPathString);

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
            System.out.print("Interactive mode: Enter a task to execute. Pick among:");
            for (Task task : taskList)
            {
                System.out.print(" " + task.getShorthand());
            }
            System.out.println("");

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

        System.out.println("Executing task " + task.getShorthand());
        task.run();
    }

    static void recursivelyPrint (NSObject nsObject)
    {
        recursivelyPrint(nsObject, 0);
    }

//    public static void main (String[] args)
//    {
////        File file = new File("C:/Users/Sebastian/Music/iTunes/iTunes Music Library.xml");
////        LibraryParser iTunesLibraryParser = new LibraryParser(file);
////        try
////        {
////            Library library = iTunesLibraryParser.parse();
////            var printer = new LibraryPrinter(library);
////            printer.run();
////        }
////        catch (IExportParsingException e)
////        {
////            e.printStackTrace();
////        }
//
//
//        try
//        {
//
//            LoadSettings settings = new LoadSettingsBuilder().build();
//            Load load = new Load(settings);
//
//            String filename = "settings.yaml";
//
//            URL ressource = ClassLoader.getSystemResource(filename); // System.class.getResource("settings.yaml");
//            URI ressourceUri = ressource.toURI();
//            File f = new File(ressourceUri);
//            FileInputStream fis = new FileInputStream(f);
//            Iterable<Object> yaml = load.loadAllFromInputStream(fis);
//            descent(yaml);
//
//
////
////            for (var rofl : wtf)
////            {
////                System.out.println(rofl.getClass());
////                System.out.println("ROFL:");
////                System.out.println(rofl);
////
////                LinkedHashMap map = (LinkedHashMap) rofl;
////                for (java.iexport.util.Map.Entry<?, ?> zomfg : map.entrySet())
////                {
////                    System.out.println("ZOMFG:");
////                    System.out.println(zomfg.getClass());
////                    System.out.println(zomfg);
////                }
////            }
//
//
////            var obj = wtf.iterator().next();
////            System.out.println(obj);
////            Map<String, Object> hashmap = (Map<String, Object>) obj;
////            for (var pair : hashmap.entrySet())
////            {
////                System.out.println(pair);
////            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    static void recursivelyPrint (NSObject nsObject, int indentation)
    {
        int i = 0;
        while (i < indentation)
        {
            System.out.print("    ");
            i++;
        }

        if (nsObject.getClass().equals(NSNumber.class))
        {
            System.out.println(nsObject);
            return;
        }

        if (nsObject.getClass().equals(NSString.class))
        {
            System.out.println(nsObject);
            return;
        }

        if (nsObject.getClass().equals(NSDate.class))
        {
            System.out.println(nsObject);
            return;
        }

        if (nsObject.getClass().equals(NSData.class))
        {
            System.out.println(nsObject);
            return;
        }


        if (nsObject.getClass().equals(NSDictionary.class))
        {
            NSDictionary dictionary = (NSDictionary) nsObject;

            System.out.println("IS DICTIONARY, #ENTRIES = " + dictionary.count());
            for (var pair : dictionary.entrySet())
            {
                int j = 0;
                while (j < indentation + 1)
                {
                    System.out.print("    ");
                    j++;
                }
                System.out.println(pair.getKey());
                recursivelyPrint(pair.getValue(), indentation + 2);
            }

            return;
        }

        if (nsObject.getClass().equals(NSArray.class))
        {
            NSArray array = (NSArray) nsObject;

            System.out.println("IS ARRAY, #ENTRIES = " + array.count());
            for (var entry : array.getArray())
            {
                int j = 0;
                while (j < indentation + 1)
                {
                    System.out.print("    ");
                    j++;
                }
                System.out.println("ENTRY:");
                recursivelyPrint(entry, indentation + 2);
            }

            return;
        }


        System.out.println("EXCEPTION - actually it is class " + nsObject.getClass().toString());
        System.exit(1337);

    }

    private static void printUsage ()
    {
        // TODO -q -console plain
        System.out.println("Usage: run --args=\"[PATH_TO_YAML_FILE] [TASK]\"");
        System.out.println("  where");
        System.out.println("    [PATH_TO_YAML_FILE] is the path to a YAML file with the settings that should be used");
        System.out.println("      (e.g. %USERPROFILE%\\Desktop\\iExportSettings.yaml )");
        System.out.println("      See the provided iExportDefaultSettings.yaml for the available settings");
        System.out.println("      If omitted, default values will be used.");
        System.out.println("    [TASK] is the task that should be performed after parsing the library");
        System.out.println("      If specified, it will overwrite the \"task\" field from the settings.");
        System.out.println("      If not specified at all, interactive mode will be used.");
        System.out.println("  Available tasks:");
        System.out.println("      interactive - Select a task via STDIN");
        System.out.println("      none - Simply check if parsing the library succeeds");
    }

    private static void descent (Object o)
    {
        descent(o, 0);
    }


    private static void descent (Iterable yaml, int depth)
    {
        for (Object member : yaml)
        {
            System.out.println("MEMBER");
            System.out.println(member.getClass());
            System.out.println(member);

            descent(member, depth + 1);
        }
    }

    private static void descent (LinkedHashMap map, int depth)
    {

        for (Object key : map.keySet())
        {
            for (int i = 0; i < depth; i++)
            {
                System.out.print("    ");
            }
            System.out.println("KEY: " + key);
            for (int i = 0; i < depth; i++)
            {
                System.out.print("    ");
            }
            descent(map.get(key), depth + 1);
        }
    }

    private static void descent (Object o, int depth)
    {
        try
        {
            LinkedHashMap map = (LinkedHashMap) o;
            descent(map, depth);
        }
        catch (ClassCastException e)
        {
            try
            {
                Iterable itr = (Iterable) o;
                descent(itr, depth);
            }
            catch (ClassCastException e2)
            {
                for (int i = 0; i < depth; i++)
                {
                    System.out.print("    ");
                }
//                System.out.println("LEAF at LEVEL " + depth);
                System.out.println(o.getClass());
                for (int i = 0; i < depth; i++)
                {
                    System.out.print("    ");
                }
                System.out.println(o);
            }
        }

    }

    static class RecursiveExplorer
    {
        Map<String, Class> classes;


    }
}
