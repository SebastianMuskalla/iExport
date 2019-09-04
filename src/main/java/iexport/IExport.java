package iexport;

import com.dd.plist.*;
import iexport.domain.Library;
import iexport.parsing.itunes.IExportParsingException;
import iexport.parsing.itunes.LibraryParser;
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
        File file = new File("C:/Users/Sebastian/Music/iTunes/iTunes Music Library.xml");
        LibraryParser iTunesLibraryParser = new LibraryParser(file);
        Library library = null;

        try
        {
            library = iTunesLibraryParser.parse();
        }
        catch (IExportParsingException e)
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

            Scanner scanner = new Scanner(System.in);
            taskName = scanner.next();
        }

        // TODO WTF
        taskName = "fileexport";

        Task task = tasks.get(taskName);

        if (task == null)
        {
            throw new RuntimeException("No task with name " + taskName);
        }

        System.out.println("Executing task " + task.getShorthand());
        task.run();
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
////                for (java.util.Map.Entry<?, ?> zomfg : map.entrySet())
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

    static void recursivelyPrint (NSObject nsObject)
    {
        recursivelyPrint(nsObject, 0);
    }

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
