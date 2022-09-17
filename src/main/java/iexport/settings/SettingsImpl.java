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
 * A semi-abstract implementation of {@link Settings} that has methods to access settings via their keys.
 * <p>
 * The default values for the settings can be accessed using {@link #getDefaultValueFor(String)}.
 * Any non-abstract child class should implement this method
 * e.g. by accessing the default values stored in a static member.
 * <p>
 * It is also highly recommend to provide wrapper methods for type safety.
 */
public abstract class SettingsImpl implements Settings
{
    /**
     * Has this object been initialized with user-defined settings?
     */
    private final boolean useAllDefaultSettings;

    /**
     * Stores for each key the settings.
     */
    private final Map<String, Object> settings;

    /**
     * Default constructor that sets {@link #settings} to the empty map.
     * This means the default values will be used for all settings.
     */
    protected SettingsImpl ()
    {
        useAllDefaultSettings = true;
        settings = new HashMap<>();
    }

    /**
     * Constructor that initializes {@link #settings} with user-provided values.
     *
     * @param settings the user-provided values
     */
    protected SettingsImpl (Map<String, Object> settings)
    {
        useAllDefaultSettings = false;
        this.settings = settings;
    }

    @Override
    public String getYamlPath (String key)
    {
        return (getYamlPrefix() == null || getYamlPrefix().isEmpty()) ? key : getYamlPrefix() + "." + key;
    }

    @Override
    public boolean isDefault ()
    {
        return useAllDefaultSettings;
    }

    /**
     * Access the setting value for the given key provided by the user
     *
     * @param key the key
     * @return the stored setting value
     */
    protected Object getUserSpecifiedValueFor (String key)
    {
        return settings.get(key);
    }

    /**
     * Tries to obtain the user-defined setting for a given {@code key}, and otherwise falls back to the default value.
     * <p>
     * If no default value exists in the latter case, we throw a {@link RuntimeException}.
     * <p>
     * We notify the user the first time we use a default-value instead of a user-provided one.
     *
     * @param key the key
     * @return the setting for the key as defined by the user or its default value
     */
    protected Object getValueFor (String key)
    {
        Object value = getUserSpecifiedValueFor(key);
        if (value != null)
        {
            // Value has been specified by the user.
            return value;
        }

        // Value has not been specified by the user, fall back to the default.
        value = getDefaultValueFor(key);
        if (value != null)
        {
            // We should notify the user that we use this default setting
            // unless this whole object just contains default settings.
            if (!isDefault())
            {
                Logging.getLogger().warning("No user-provided setting for key \"" + getYamlPath(key) + "\", using default value \"" + value + "\" from now on");
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
    protected abstract Object getDefaultValueFor (String key);

}
