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

package iexport.settings;

import iexport.IExport;
import iexport.logging.LogLevel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * General settings for iExport that do not just affect parsing or a specific task.
 * <p>
 * These settings correspond to the root dictionary of the settings .yaml file
 * minus the keys "parsing" and tasks.
 */
public class GeneralSettings extends SettingsImpl
{
    /**
     * Map holding the default general settings
     */
    protected static final Map<String, Object> GENERAL_DEFAULT_SETTINGS = new HashMap<>();

    /**
     * Setting for the log level.
     */
    private static final String SETTING_LOG_LEVEL = "logLevel";

    /**
     * Setting for the task.
     */
    private static final String SETTING_TASK = "task";

    /**
     * Default value for the setting task, which is "interactive"
     */
    private static final String SETTING_TASK_DEFAULT_VALUE = IExport.INTERACTIVE_MODE_NAMES.get(0);

    /**
     * Values for the setting "logLevel" that are associated to each log level;
     */
    private static final Map<LogLevel, List<String>> SETTING_LOG_LEVEL_STRINGS;

    /**
     * Default value for log level: {@link iexport.logging.LogLevel#NORMAL}, taken from {@link #SETTING_LOG_LEVEL_STRINGS};
     */
    private static final String SETTING_LOG_LEVEL_DEFAULT_VALUE;

    static
    {
        // Initialize SETTING_LOG_LEVEL_STRINGS
        SETTING_LOG_LEVEL_STRINGS = new HashMap<>();
        SETTING_LOG_LEVEL_STRINGS.put(LogLevel.IMPORTANT, List.of("IMPORTANT", "QUIET", "ERROR", "WARNING"));
        SETTING_LOG_LEVEL_STRINGS.put(LogLevel.NORMAL, List.of("NORMAL"));
        SETTING_LOG_LEVEL_STRINGS.put(LogLevel.INFO, List.of("INFO", "VERBOSE"));
        SETTING_LOG_LEVEL_STRINGS.put(LogLevel.DEBUG, List.of("DEBUG", "VERYVERBOSE"));

        // we can now get the default value for the log Level
        SETTING_LOG_LEVEL_DEFAULT_VALUE = SETTING_LOG_LEVEL_STRINGS.get(LogLevel.NORMAL).get(0);

        // add the default settings
        GENERAL_DEFAULT_SETTINGS.put(SETTING_LOG_LEVEL, SETTING_LOG_LEVEL_DEFAULT_VALUE);
        GENERAL_DEFAULT_SETTINGS.put(SETTING_TASK, SETTING_TASK_DEFAULT_VALUE);
    }

    /**
     * Memoize the log level
     */
    private LogLevel logLevel = null;

    /**
     * Constructor for {@code GeneralSettings} that uses default values for all settings
     */
    public GeneralSettings ()
    {
        super();
    }

    /**
     * Constructor that initializes {@link Settings#settings} with user-provided values.
     *
     * @param settings the user-provided values
     */
    public GeneralSettings (Map<String, Object> settings)
    {
        super(settings);
    }

    public String getTaskName ()
    {
        Object result = getValueFor(SETTING_TASK);
        try
        {
            return (String) result;
        }
        catch (ClassCastException e)
        {
            throw new RuntimeException(this.getClass().getSimpleName() + ": invalid entry for " + getYamlPath(SETTING_TASK)
                    + ", expected a string, but got " + result.getClass().getSimpleName());
        }
    }

    /**
     * Get the log level from the "logLevel" key (or the default value)
     *
     * @return the log level
     */
    public LogLevel getLogLevel ()
    {
        // We have already memoized this value
        if (logLevel != null)
        {
            return logLevel;
        }

        Object logLevelObject = getValueFor(SETTING_LOG_LEVEL);
        try
        {
            String logLevelString = (String) logLevelObject;

            for (var entry : SETTING_LOG_LEVEL_STRINGS.entrySet())
            {
                if (entry.getValue().contains(logLevelString))
                {
                    LogLevel logLevel = entry.getKey();
                    this.logLevel = logLevel;
                    return logLevel;
                }
            }

            throw new RuntimeException(this.getClass().getSimpleName() + ": invalid entry for " + getYamlPath(SETTING_LOG_LEVEL)
                    + ": " + logLevelString + ". Expected one of " + SETTING_LOG_LEVEL_STRINGS);
        }
        catch (ClassCastException e)
        {
            throw new RuntimeException(this.getClass().getSimpleName() + ": invalid entry for " + getYamlPath(SETTING_LOG_LEVEL)
                    + ", expected a string, but got " + logLevelObject.getClass().getSimpleName());
        }
    }

    @Override
    public Set<String> unusedSettings ()
    {
        return getUserSpecifiedKeys().stream().filter(Predicate.not(GENERAL_DEFAULT_SETTINGS::containsKey)).collect(Collectors.toSet());
    }

    @Override
    public String getYamlPrefix ()
    {
        return "";
    }

    @Override
    protected Object getDefaultValueFor (String key)
    {
        return GENERAL_DEFAULT_SETTINGS.get(key);
    }
}
