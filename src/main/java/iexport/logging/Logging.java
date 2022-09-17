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

package iexport.logging;

/**
 * A static class holding an instance of {@link Logger} that is essentially a singleton.
 * <p>
 * By default, we use a {@link StdoutLogger} that simply prints to Stdout
 */
public class Logging
{
    /**
     * The singleton logger.
     */
    private static final Logger instance = new StdoutLogger();

    /**
     * @return the singleton logger
     */
    public static Logger getLogger ()
    {
        return instance;
    }

    /**
     * This class should not be instantiated.
     */
    private Logging ()
    {
    }

}
