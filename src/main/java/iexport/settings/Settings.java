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

import java.util.Set;

/**
 * A general object that stores a collection of settings (e.g. for parsing).
 * <p>
 * Intuitively, each {@code Settings} object consists of two layers:
 * <ol>
 *     <li> default values that are set in the {@code static} block of each class
 *     and that can be accessed using {@link SettingsImpl#getDefaultValueFor(String)}
 *     <li> user-provided values that have been parsed from the settings .yaml file
 * </ol>
 * {@link SettingsImpl#getValueFor(String)} will prefer user-defined values and fall back on the default values if values are not set
 * <p>
 * This class stores treats settings as objects.
 * Its child classes should provide wrappers around {@link SettingsImpl#getValueFor(String)} that do checked casts.
 */
public interface Settings
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
    static String applyUserProfileReplacement (String pathString)
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
            Logging.getLogger().warning("Applying replacement of " + userProfilePlaceholder + " failed");
            Logging.getLogger().debug(1, "Cause : " + e);
            Logging.getLogger().debug(1, "Message: " + e.getMessage());
        }
        return pathString;
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
    String getYamlPath (String key);

    /**
     * Collects the settings that have been specified by the user (i.e. they are present in the .yaml file),
     * but they are actually not used.
     * <p>
     * The purpose of this function is to be able to notify the user if they have mistyped the name of a key.
     * <p>
     * An implementation of this method will typically compare the user-provided settings {@link SettingsImpl#settings}
     * to the map with the default settings.
     *
     * @return a set of keys corresponding that are invalid/unused
     */
    Set<String> unusedSettings ();

    boolean isDefault ();

    /**
     * Gets the location of the dictionary this settings object represents in the settings .yaml file
     * <p>
     * (e.g.
     *
     * @return the path
     */
    String getYamlPrefix ();

}
