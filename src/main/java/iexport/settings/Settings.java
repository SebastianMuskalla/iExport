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

import iexport.logging.Logging;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A general object that stores a collection of settings (e.g. for parsing).
 * <p>
 * Intuitively, each {@code Settings} object consists of two layers:
 * <ol>
 *     <li> default values that are set in the {@code static} block of each class
 *     and that can be accessed using {@link #getDefaultValueFor(String)}
 *     <li> user-provided values that have been parsed from the settings .yaml file
 * </ol>
 * {@link #getValueFor(String)} will prefer user-defined values and fall back on the default values if values are not set
 * <p>
 * This class stores treats settings as objects.
 * Its child classes should provide wrappers around {@link #getValueFor(String)} that do checked casts.
 */
public abstract class Settings
{
    /**
     * Replaces %USERPROFILE% in a string by the value of the environment variable USERPROFILE.
     * <p>
     * Under Windows, this is typically set to "C:\Users\YourUserName".
     * <p>
     * Depending on security settings and the environment, getting the value of the environment variable may fail.
     * In this case, the method will generate log output and return an unmodified string,
     * but it will not throw an exception.
     *
     * @param pathString the string in which %USERPROFILE% should be replaced
     * @return the string with the replacement applied (if successful) or {@code pathString}
     */
    protected static String applyUserProfileReplacement (String pathString)
    {
        final String userProfileEnvironmentVariable = "USERPROFILE";
        final String userProfilePlaceholder = "%USERPROFILE%";

        try
        {
            String userProfileValue = System.getenv(userProfileEnvironmentVariable);
            if (userProfileValue != null)
            {
                pathString = pathString.replace(userProfilePlaceholder, userProfileValue);
            }
        }
        catch (Exception e)
        {
            Logging.getLogger().important("Applying replacement of " + userProfilePlaceholder + " failed");
            Logging.getLogger().info(1, "Cause : " + e);
            Logging.getLogger().info(1, "Message: " + e.getMessage());
        }
        return pathString;
    }

    /**
     * Has this object been initialized with user-defined settings?
     */
    private final boolean defaultSettings;

    /**
     * Stores for each key the settings.
     */
    private final Map<String, Object> settings;

    /**
     * Default constructor that sets {@link #settings} to the empty map.
     * This means the default values will be used for all settings.
     */
    protected Settings ()
    {
        defaultSettings = true;
        settings = new HashMap<>();
    }

    /**
     * Constructor that initializes {@link #settings} with user-provided values.
     *
     * @param settings the user-provided values
     */
    protected Settings (Map<String, Object> settings)
    {
        defaultSettings = false;
        this.settings = settings;
    }

    /**
     * Returns the .yaml path for a specific setting.
     * <p>
     * e.g. called on {@link ParsingSettings} for {@code key = "xmlFilePath"}, this should return {@code "parsing.xmlFilePath"}.
     * <p>
     * e.g. called on {@link GeneralSettings} for {@code key = "logLevel"}, this should return {@code "logLevel"}.
     *
     * @param key the key for this setting
     * @return the path
     */
    public String getYamlPath (String key)
    {
        return getYamlPrefix() + key;
    }

    /**
     * Collects the settings that have been specified by the user (i.e. they are present in the .yaml file),
     * but they are actually not used.
     * <p>
     * The purpose of this function is to be able to notify the user if they have mistyped the name of a key.
     * <p>
     * An implementation of this method will typically compare the user-provided settings {@link #settings}
     * to the map with the default settings.
     *
     * @return a set of keys corresponding that are invalid/unused
     */
    abstract public Set<String> unusedSettings ();

    public boolean isDefault ()
    {
        return defaultSettings;
    }

    /**
     * Gets the location of the dictionary this settings object represents in the settings .yaml file
     * <p>
     * (e.g.
     *
     * @return the path
     */

    protected abstract String getYamlPrefix ();

    /**
     * Tries to obtain the user-defined setting for a given {@code key}, and otherwise falls back to the default value.
     * <p>
     * If no default value exists in the latter case, we throw a {@link RuntimeException}.
     *
     * @param key the key
     * @return the setting for the key as defined by the user or its default value
     */
    protected Object getValueFor (String key)
    {
        Object value = settings.get(key);
        if (value != null)
        {
            // Value has been specified by the user
            return value;
        }

        // Value has not been specified by the user, fall back to the default
        value = getDefaultValueFor(key);
        if (value != null)
        {
            // We should notify the user that we use this default setting (unless this whole object just contains default Settings)
            if (!isDefault())
            {
                Logging.getLogger().info("No user-provided setting for key \"" + getYamlPath(key) + "\", using default value \"" + value + "\" from now on");
            }

            settings.put(key, value);
            return value;
        }
        else
        {
            // No default value for this setting exists
            throw new RuntimeException(this.getClass().getSimpleName() + ": Not a valid key: " + getYamlPath(key));
        }

    }

    /**
     * Get the keys for which settings have been specified by the user in the .yaml file
     *
     * @return the list of keys
     */
    protected Set<String> getUserSpecifiedKeys ()
    {
        return settings.keySet();
    }

    /**
     * Get the default value for the specified key
     *
     * @param key the key
     * @return the default value for that key
     */
    abstract Object getDefaultValueFor (String key);

}
