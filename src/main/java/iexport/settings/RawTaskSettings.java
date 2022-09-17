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

import java.util.Map;
import java.util.Set;

/**
 * A class for the settings used by each task.
 * <p>
 * For some task named TASK, these settings correspond to the dictionary
 * under the key "tasks.TASK" in the root dictionary of the .yaml file.
 * <p>
 * An object of this class should be later converted into an actual task settings object
 * for the task at hand that provides type-safe accessors.
 */
public class RawTaskSettings extends SettingsImpl
{
    /**
     * The name of the task these settings are for.
     */
    private final String taskName;

    /**
     * Constructs {©code TaskSettings} for the specified task name with the user-provided values
     *
     * @param taskName the name of the task these settings are for
     * @param taskMap  the user-provided values
     */
    public RawTaskSettings (String taskName, Map<String, Object> taskMap)
    {
        super(taskMap);
        this.taskName = taskName;
    }

    /**
     * Constructs {©code TaskSettings} for the specified task name with the default values
     *
     * @param taskName the name of the task these settings are for
     */
    public RawTaskSettings (String taskName)
    {
        super();
        this.taskName = taskName;
    }

    public String getTaskName ()
    {
        return taskName;
    }

    @Override
    public String getYamlPrefix ()
    {
        return "tasks" + "." + getTaskName();
    }

    @Override
    public Set<String> unusedSettings ()
    {
        return Set.of();
    }

    @Override
    protected Object getDefaultValueFor (String key)
    {
        throw new RuntimeException("You should not call getDefaultValueFor on RawTaskSettings (for task " + taskName + ")");
    }
}
