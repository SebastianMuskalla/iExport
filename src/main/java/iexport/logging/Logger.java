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
 * A generic logger that logs messages with a specified logLevel and a specified amount of indentation.
 * <p>
 * How these log level and indentation are treated is up to the implementation;
 */
public abstract class Logger
{

    /**
     * Convenience method for logging something with a specified log level and no indentation,
     * see {@link #log}.
     *
     * @param logLevel the log level
     * @param message  the message
     */
    public void log (LogLevel logLevel, String message)
    {
        log(logLevel, 0, message);
    }

    public abstract void log (LogLevel logLevel, int indentation, String message);

    /**
     * Set the maximum log level that this locker should track.
     * <p>
     * The expectation is that the logger will discard all messages that are more verbose than specified by {@code logLevel},
     * but the details are up to the implemenation.
     *
     * @param logLevel the log level
     */
    public abstract void setLogLevel (LogLevel logLevel);

    /**
     * Convenience method for logging something with {@link LogLevel#IMPORTANT},
     * see {@link #log}.
     *
     * @param indentation the indentation with which the message shall be printed
     * @param message     the message
     */
    public void important (int indentation, String message)
    {
        log(LogLevel.IMPORTANT, indentation, message);
    }

    /**
     * Convenience method for logging something with {@link LogLevel#IMPORTANT} with no indentation,
     * see {@link #log}.
     *
     * @param message the message
     */
    public void important (String message)
    {
        log(LogLevel.IMPORTANT, 0, message);
    }

    /**
     * Convenience method for logging something with {@link LogLevel#NORMAL},
     * see {@link #log}.
     *
     * @param indentation the indentation with which the message shall be printed
     * @param message     the message
     */
    public void message (int indentation, String message)
    {
        log(LogLevel.NORMAL, indentation, message);
    }

    /**
     * Convenience method for logging something with {@link LogLevel#NORMAL} with no indentation,
     * see {@link #log}.
     *
     * @param message the message
     */
    public void message (String message)
    {
        log(LogLevel.NORMAL, 0, message);
    }

    /**
     * Convenience method for logging something with {@link LogLevel#INFO},
     * see {@link #log}.
     *
     * @param indentation the indentation with which the message shall be printed
     * @param message     the message
     */
    public void info (int indentation, String message)
    {
        log(LogLevel.INFO, indentation, message);
    }

    /**
     * Convenience method for logging something with {@link LogLevel#INFO} with no indentation,
     * see {@link #log}.
     *
     * @param message the message
     */
    public void info (String message)
    {
        log(LogLevel.INFO, 0, message);
    }

    /**
     * Convenience method for logging something with {@link LogLevel#DEBUG},
     * see {@link #log}.
     *
     * @param indentation the indentation with which the message shall be printed
     * @param message     the message
     */
    public void debug (int indentation, String message)
    {
        log(LogLevel.DEBUG, indentation, message);
    }

    /**
     * Convenience method for logging something with {@link LogLevel#DEBUG} with no indentation,
     * see {@link #log}.
     *
     * @param message the message
     */
    public void debug (String message)
    {
        log(LogLevel.DEBUG, 0, message);
    }

}
