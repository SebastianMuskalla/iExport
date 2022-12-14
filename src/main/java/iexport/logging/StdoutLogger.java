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

import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * A logger that logs all messages up to a specified log level to STDOUT.
 */
public class StdoutLogger extends Logger
{
    /**
     * Should output messages be prefixes with their log level?
     */
    private static final boolean USE_PREFIXES = true;

    /**
     * A version of {@code System.out.println} that uses UTF8.
     */
    private static final PrintStream OUT = new PrintStream(System.out, true, UTF_8);

    /**
     * Pattern for newline (either \n or \r\n).
     */
    private static final Pattern NEWLINE_PATTERN = Pattern.compile("\\R");

    /**
     * How much indentation to use for each level of indentation?
     * <p>
     * If the {@code indentation} parameter of {@link #log} is set to {@code n},
     * this string will be repeated {@code n} times.
     */
    private static final String BASE_INDENTATION = "    "; // 4 spaces

    /**
     * A list of prefixes that will be prepended to every message depending on it's log level.
     */
    private static final Map<LogLevel, String> PREFIXES = new HashMap<>();

    /**
     * The default log level.
     * <p>
     * Note that setting this to anything other than {@link LogLevel#DEBUG} may hide information during start up.
     */
    private static final LogLevel DEFAULT_LOGLEVEL = LogLevel.NORMAL;

    static
    {
        PREFIXES.put(LogLevel.DEBUG, "DEBUG: ");
        PREFIXES.put(LogLevel.NORMAL, "");
        PREFIXES.put(LogLevel.WARNING, "WARNING: ");
        PREFIXES.put(LogLevel.ERROR, "ERROR: ");
    }

    /**
     * The messages of which log level should we accept?
     */
    private LogLevel verbosity = DEFAULT_LOGLEVEL;

    /**
     * Log a message with the specified log level and the specified amount of indentation.
     * <p>
     * If the {@link #verbosity} of the logger is set to more than {@code logLevel}, the message will be discarded.
     * <p>
     * Otherwise, it will be split into lines,
     * each line will be prefixed by a prefix depending on the log level (see {@link #PREFIXES})
     * and the specified amount of indentation ({@code indentation} many copies of {@link #BASE_INDENTATION}),
     * and then printed to STDOUT.
     *
     * @param logLevel    the log level of the message.
     * @param indentation the amount of indentation to use.
     * @param message     the message.
     */
    @Override
    public void log (LogLevel logLevel, int indentation, String message)
    {
        // We only need to do something if we actually accept messages of this type
        if (accepts(logLevel))
        {
            if (USE_PREFIXES)
            {
                final String prefix = PREFIXES.get(logLevel) == null ? "" : PREFIXES.get(logLevel);

                NEWLINE_PATTERN
                        .splitAsStream(message)
                        .map((s) -> prefix + s)
                        .map((s) -> String.join("", Collections.nCopies(indentation, BASE_INDENTATION)) + s)
                        .forEach(OUT::println);
            }
            else
            {
                // Just add indentation.
                NEWLINE_PATTERN
                        .splitAsStream(message)
                        .map((s) -> BASE_INDENTATION.repeat(indentation) + s)
                        .forEach(OUT::println);
            }
        }

    }

    @Override
    public LogLevel getLogLevel ()
    {
        return verbosity;
    }

    @Override
    public void setLogLevel (LogLevel logLevel)
    {
        verbosity = logLevel;
    }

    /**
     * Does this logger currently accept messages of the specified log level?
     *
     * @param logLevel the log level
     * @return true iff it accepts messages with this log level
     */
    private boolean accepts (LogLevel logLevel)
    {
        return logLevel.lessVerbose(verbosity);
    }
}
