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


public abstract class TaskSettings implements Settings
{

    /**
     * Has this object been initialized with user-defined settings?
     */
    private final boolean defaultSettings;

    private final RawTaskSettings rawTaskSettings;

    private final String taskName;

    protected TaskSettings (RawTaskSettings rawTaskSettings)
    {
        this.rawTaskSettings = rawTaskSettings;
        this.defaultSettings = rawTaskSettings.isDefault();
        this.taskName = rawTaskSettings.getTaskName();
    }

    @Override
    public String getYamlPath (String key)
    {
        return getYamlPrefix() + key;
    }

    @Override
    abstract public Set<String> unusedSettings ();

    @Override
    public boolean isDefault ()
    {
        return defaultSettings;
    }

    @Override
    public String getYamlPrefix ()
    {
        return "tasks." + taskName + ".";
    }

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

        Object value = rawTaskSettings.getUserSpecifiedValueFor(key);
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
            // TODO
            // settings.put(key, value);
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
        return rawTaskSettings.getUserSpecifiedKeys();
    }

    /**
     * Get the default value for the specified key
     *
     * @param key the key
     * @return the default value for that key
     */
    protected abstract Object getDefaultValueFor (String key);
}
