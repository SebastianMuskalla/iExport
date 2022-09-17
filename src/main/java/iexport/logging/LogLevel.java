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
 * The importance of a log message.
 * <p>
 * A logger may treat messages different depending on their LogLevel.
 */
public enum LogLevel
{
    /**
     * Debug messages - Stuff you don't need to see unless you try to fix a problem.
     */
    DEBUG(40),

    /**
     * Normal output.
     */
    NORMAL(30),

    /**
     * Warnings that you should so.
     */
    WARNING(20),

    /**
     * Error messages that are important.
     */
    ERROR(10);

    /**
     * An integer value for each log level that can be used for comparison.
     * <p>
     * Convention: Lower values are more important.
     */
    private final int value;

    LogLevel (int value)
    {
        this.value = value;
    }

    /**
     * @return the internal value
     */
    public int getValue ()
    {
        return value;
    }
}
