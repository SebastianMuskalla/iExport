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

/**
 * The collection of settings used by iExport.
 *
 * @param generalSettings general settings,
 *                        corresponds to the root dictionary of the settings .yaml file
 *                        (e.g. "logLevel")
 * @param parsingSettings settings for parsing the library,
 *                        corresponds to the dictionary for the key "parsing",
 *                        (e.g. "parsing.xmlFilePath")
 * @param taskSettings    maps each task name to its settings.
 *                        For each task TASK, the settings will correspond to the dictionary "tasks.TASK"
 */
public record SettingsTriple
        (
                GeneralSettings generalSettings,
                ParsingSettings parsingSettings,
                Map<String, TaskSettings> taskSettings
        )
{

}
