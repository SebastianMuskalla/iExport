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

package iexport.tasks;

import iexport.itunes.Library;
import iexport.logging.Logging;
import iexport.settings.RawTaskSettings;

/**
 * A very simple task that exits iExport with exit code 0.
 */
public class QuitTask implements Task
{
    @Override
    public String getTaskName ()
    {
        return "quit";
    }

    @Override
    public String getDescription ()
    {
        return "exit iExport";
    }

    @Override
    public void run (Library library, RawTaskSettings rawTaskSettings)
    {
        Logging.getLogger().message("Bye!");
        System.exit(0);
    }
}
