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

import java.util.HashSet;
import java.util.Set;

/**
 * A class for the settings used by each task.
 * <p>
 * For some task named TASK, these settings correspond to the dictionary
 * under the key "tasks.TASK" in the root dictionary of the .yaml file.
 * <p>
 * In contrast to an object of class {@link RawTaskSettings}, this object will provide default vlaues
 * and type-safe getters.
 * <p>
 * However, this means {@link RawTaskSettings} have to explicitly converted into {@link TaskSettings}.
 */
public abstract class TaskSettings implements Settings
{

    /**
     * Has this object been initialized with user-defined settings?
     */
    private final boolean useAllDefaultSettings;

    /**
     * The underlying raw task settings that contain the data parsed from the settings .yaml file.
     */
    private final RawTaskSettings rawTaskSettings;

    /**
     * Store the settings that have been used.
     * <p>
     * This is simply so that we don't notify the user twice if a default value is used.
     */
    private final Set<String> notifyBuffer = new HashSet<>();

    /**
     * Constructor that converts {@link RawTaskSettings} into {@link TaskSettings}.
     *
     * @param rawTaskSettings containing the settings parsed from the .yaml file
     */
    protected TaskSettings (RawTaskSettings rawTaskSettings)
    {
        this.rawTaskSettings = rawTaskSettings;
        this.useAllDefaultSettings = rawTaskSettings.isDefault();
    }

    @Override
    public String getYamlPrefix ()
    {
        return rawTaskSettings.getYamlPrefix();
    }

    @Override
    public String getYamlPath (String key)
    {
        return rawTaskSettings.getYamlPath(key);
    }

    @Override
    public boolean isDefault ()
    {
        return useAllDefaultSettings;
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
                // We only want to give the notification once
                if (!notifyBuffer.contains(key))
                {
                    Logging.getLogger().warning("No user-provided setting for key \"" + getYamlPath(key) + "\", using default value \"" + value + "\" from now on");
                }
            }
            notifyBuffer.add(key);
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
