package iexport;

import com.dd.plist.*;
import iexport.parsing.itunes.ITunesLibraryParser;
import iexport.parsing.itunes.LibraryParsingException;

import java.io.File;
import java.util.Map;

public class main
{
    public static void main (String[] args)
    {
        File file = new File("C:/Users/Sebastian/Music/iTunes/iTunes Music Library.xml");
        ITunesLibraryParser iTunesLibraryParser = new ITunesLibraryParser(file);
        try
        {
            iTunesLibraryParser.parse();
        }
        catch (LibraryParsingException e)
        {
            e.printStackTrace();
        }
    }

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

    static class RecursiveExplorer
    {
        Map<String, Class> classes;


    }
}
