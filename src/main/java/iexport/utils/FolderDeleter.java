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

package iexport.utils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * A static class for deleting a folder in the file system.
 */
public class FolderDeleter
{

    /**
     * Delete a folder recursively, including all its subdirectories and files.
     *
     * @param pathToFile the path to the folder that should be deleted
     * @throws IOException if deleting the folder failed
     */
    public static void recursiveDelete (Path pathToFile)
            throws
            IOException
    {
        if (pathToFile == null || !Files.exists(pathToFile))
        {
            return;
        }

        // Implement the recursion
        Files.walkFileTree(pathToFile,
                new SimpleFileVisitor<>()
                {
                    @Override
                    public FileVisitResult visitFile (Path file, BasicFileAttributes attrs)
                            throws
                            IOException
                    {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory (Path dir, IOException e)
                            throws
                            IOException
                    {
                        if (e == null)
                        {
                            // No error
                            Files.delete(dir);
                            return FileVisitResult.CONTINUE;
                        }
                        else
                        {
                            // Got an error
                            throw e;
                        }
                    }
                }
        );

    }

    /**
     * This class should not be instantiated.
     */
    private FolderDeleter ()
    {

    }

}
