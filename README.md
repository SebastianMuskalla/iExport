iExport
=======

Tool for parsing and processing the iTunes library
------------------------------------------------

iExport is a tool that parses the iTunes music library based on the file `iTunes Music Library.xml`.
It then allows the user to execute various tasks including

* printing information about the library.
* exporting the iTunes playlists in the form of `.m3u` files.
* exporting the iTunes playlists as folders that contain the tracks as files.

The latter two tasks can be used to migrate the iTunes music library to another system.

iExport is written in Java and can be built and run using Gradle.

TL;DR:
------

* Enable the *Share iTunes Library XML with other applications* setting in iTunes.
* Install the *Java JDK* (version 18+) and *Gradle* (version 7.5.1+).
* Clone or download this repository and open a *Powershell*.
* Run iExport using
  ```sh
  gradle :run --quiet --console=plain --args="defaultSettingsCompact.yaml interactive"
  ```
* Select and run a task.

Contents
--------

* [License](#license)
* [Build instructions](#build-instructions)
* [Usage](#usage)
* [Tasks](#tasks)
* [Settings](#settings)
* [Shortcomings: Sorting](#shortcomings-sorting)
* [JavaDoc](#javadoc)
* [Dependencies](#dependencies)

License
-------

Copyright 2014-2022 Sebastian Muskalla

iExport is free and open-source software, licensed under the GPLv3 (GNU General Public License Version 3), see [LICENSE](LICENSE).

Build instructions
------------------

Install Java and Gradle.

Verify that both `java` and `gradle` are on your path, e.g. when you open a *Powershell*, the commands `java --version` and `gradle --version` should both work.

When you run iExport (see [Usage](#usage)) the first time, Gradle will automatically download the [Dependencies](#dependencies) and build the application.

iExport was tested using OpenJDK version 18 (<https://openjdk.org/>) and Gradle version 7.5.1 (<https://gradle.org/>) on Windows 10 21H2.

Usage
-----

* Before using iExport, make sure that the setting *"Share iTunes Library XML with other applications"* is enabled in iTunes.

  This setting can be found under `Edit > Preferences > Advanced`.

  You may need to close and reopen iTunes to make sure that `iTunes Music Library.xml` has been generated.

* Download or clone a copy of this repository.

* Open a *Powershell* and run iExport as follows:

```sh
gradle :run --quiet --console=plain --args="[PATH_TO_YAML_FILE] [TASK]"
```

where

* `[PATH_TO_YAML_FILE]` is a path to a `.yaml` file containing the settings used by iExport (see [Settings](#settings)).

  The path may contain the placeholder `%USERPROFILE%`, which will typically get resolved as `C:\Users\<YourWindowsUserName>`.

  If the path contains spaces, please additionally escape it in single quotes.

  If no argument is provided, the default settings are used (*not recommended!)*.

* `[TASK]` is the name of the task that iExport should execute after parsing the library.

  `[TASK]` is optional and will overwrite the `task` setting from the `.yaml` file.

  Possible tasks include `help` (for usage instructions) and `interactive` (specify a task name via `STDIN`).
  See [Tasks](#tasks) for a list of all supported tasks.

(Additionally, `:run` tells Gradle to run the project after it has been built.
The arguments `quiet` and `--console=plain` suppress unnecessary output by Gradle.)

A typical call to iExport may be of the following shape:

```sh
gradle :run --quiet --console=plain --args="'my settings file.yaml' printPlaylists"
```

Tasks
-----

iExport supports the following advanced tasks:

* **generatePlaylists**

  This task will export the playlists in iTunes in the form of `.m3u` files.
  For each playlist, it will create such a file in a designated output folder.

  These files are then available to other music players (e.g. *VLC*, *Poweramp*).

  This task is highly configurable via the [Settings](#settings).
  For example, the playlists files can be configured to use relative paths for the track locations, and the user can specify playlists that should be ignored.

* **exportFiles**

  This task will export the playlists in iTunes in the form of folders.
  Each such folder will contain file copies of the tracks that are in the iTunes playlist.

  These folders can then be moved e.g. to a phone, flash drive, or SD card.

  This task is highly configurable via the [Settings](#settings).
  For example, the folders and tracks can be numbered, the names can be normalized to only use ASCII characters, and the user can specify playlists that should be ignored.

Additionally, iExport supports the following basic tasks:

* **interactive**

  Ask the user to specify a task on `STDIN`.

* **help**

  Show usage instructions.

* **printLibrary**

  Prints the whole library.

* **printPlaylists**

  Prints playlists (folders as well as actual playlists)

* **printUnlistedTracks**

  Prints tracks that are not contained in any playlist.

* **printMultiplyListedTracks**

  Prints tracks that are contained in multiple playlists.

* **quit**

  Exit iExport.

Settings
--------

iExport is highly configurable via a `.yaml` file containing the settings.

### Settings & default Values

[defaultSettingsCompact.yaml](defaultSettingsCompact.yaml)

This file provides a list of all settings and their default values.

```yaml
---

# iExport Settings

logLevel: NORMAL # DEBUG, NORMAL, WARNING, ERROR

task:     interactive # see a list of tasks below


parsing:
    xmlFilePath:                  "%USERPROFILE%\\Music\\iTunes\\iTunes Music Library.xml"
    ignoreEmptyPlaylists:         true
    ignoreMaster:                 true
    ignoreNonMusicPlaylists:      true
    ignoreDistinguishedPlaylists: false
    ignorePlaylistsByName:
    # - SomePlaylistName

tasks:
    interactive:  # Ask the user to provide a task name on STDIN and execute this task.
    # (this task has no settings)

    help: # Print usage instructions on STDOUT, including a list of all tasks.
    # (this task has no settings)

    quit: # Exit iExport. Bye!
    # (this task has no settings)

    printLibrary: # Print information about the library, including the playlists and tracks.
    # (this task has no settings)

    printPlaylists: # Print all playlists (folders and actual playlists) in the iTunes library.
    # (this task has no settings)

    printUnlistedTracks: # Print the list of tracks that are not contained in any playlist.
        ignorePlaylists:
        # - SomePlaylistName

    printMultiplyListedTracks: # Print the list of tracks that are contained in more than one playlist.
        ignorePlaylists:
        #    - SomePlaylistName

    generatePlaylists: # Export the iTunes playlists by generating .m3u files at a specified location.
        outputFolder:                 "%USERPROFILE%\\Desktop\\iExport\\Playlists"
        deleteFolder:                 false
        organizeInFolders:            true
        hierarchicalNames:            true
        onlyActualPlaylists:          false
        ignoreDistinguishedPlaylists: false
        ignorePlaylists:
          # - SomePlaylistName
        playlistExtension:            ".m3u8"
        useRelativePaths:             false
        warnSquareBrackets:           true
        slashAsSeparator:             false
        trackVerification:            true
        showContinuousProgress:       true

    exportFiles: # Export the iTunes playlists by creating folders that contain the tracks as files
        outputFolder:                 "%USERPROFILE%\\Desktop\\iExport\\Files"
        deleteFolder:                 false
        hierarchicalNames:            true
        onlyActualPlaylists:          true
        ignoreDistinguishedPlaylists: true
        ignorePlaylists:
          # - SomePlaylistName
        toRootFolder:
          # - SomeOtherPlaylistName
        folderNumbering:              true
        padFolderNumbers:             true
        initialNumber:                1
        trackNumbering:               true
        padTrackNumbers:              true
        normalize:                    true
        showContinuousProgress:       true

...

```

### Documentation

[defaultSettings.yaml](defaultSettings.yaml)

This file provides extensive documentation for all settings.

```yaml
---

# iExport Settings

  # This file contents the default settings for iExport
  # You can make a copy of this file, modify the settings,
  # and pass the location of the file as an argument to iExport.

  # (Note that the default settings are not actually loaded from this file,
  #  they are hardcoded in the Java source code.)

  {

    # General settings
    # ================

    # logLevel
    # --------
    # How much output to do you want?
    # Options:
    #   "DEBUG"   - full output for debugging
    #   "NORMAL"  - regular output
    #   "WARNING" - hide regular output, but show warnings
    #   "ERROR"   - hide warnings, only show error messages
    # Default value: "NORMAL"
        "logLevel": "NORMAL",

    # task
    # ----
    # Which task should be executed after parsing the library?
    # Options: see list of tasks below.
    # If a task is specified as a command-line argument, it will override this settings.
    # Default value: "interactive" (ask user for a task name on STDIN).
        "task":     "interactive",


    # Parsing settings
    # ================

        "parsing":
          {

            # parsing.xmlFilePath
            # -------------------
            # Path to "iTunes Music Library.xml".
            #   Supports the %USERPROFILE% placeholder, which will typically get replaced by "C:\Users\<USERNAME>".
            #   Note that backslashes ("\") need to be escaped as "\\".
            # Default value: %USERPROFILE%\\Music\\iTunes\\iTunes Music Library.xml"
                "xmlFilePath":                  "%USERPROFILE%\\Music\\iTunes\\iTunes Music Library.xml",

            # Settings for ignoring playlists
            # -------------------------------
            # Playlists that get ignored will not be parsed and will not be available to any task.
            #   Note that if an ignored playlists is a folder, its contents will be ignored too.

            # parsing.ignoreEmptyPlaylists
            # ----------------------------
            # Set to true to ignore playlists that contain no tracks
            # Default value: true
                "ignoreEmptyPlaylists":         true,

            # parsing.ignoreNonMusicPlaylists
            # -------------------------------
            # Set to true to ignore playlists for movies, tvShows, audiobooks.
            # Default value: true
                "ignoreNonMusicPlaylists":      true,

            # parsing.ignoreDistinguishedPlaylists
            # ------------------------------------
            # Set to true to ignore distinguished playlists.
            #   This includes playlists like "Music" (the entire music library), "Downloaded", and non-music libraries.
            # Default value: false
                "ignoreDistinguishedPlaylists": false,

            # parsing.ignoreMaster
            # --------------------
            # Set to true to ignore the playlist "Library" with the "master" flag
            # Default value: false
                "ignoreMaster":                 true,

            # parsing.ignorePlaylistsByName
            # -----------------------------
            # Specify an array of playlist names that should be ignored
            # Default value: [] (empty list)
                "ignorePlaylistsByName":
                  [
                    # "SomePlaylistName",
                  ],
          }, # end of parsing


    # Tasks and their settings
    # ========================

        "tasks":
          {

                "interactive":
                  # Ask the user to provide a task name on STDIN and execute this task.
                  {
                    # (this task has no settings)
                  },

                "help":
                  # Print usage instructions on STDOUT, including a list of all tasks.
                  {
                    # (this task has no settings)
                  },

                "quit":
                  # Exit iExport. Bye!
                  {
                    # (this task has no settings)
                  },

                "printLibrary":
                  # Print information about the library, including the playlists and tracks.
                  {
                    # (this task has no settings)
                  },

                "printPlaylists":
                  # Print all playlists (folders and actual playlists) in the iTunes library.
                  {
                    # (this task has no settings)
                  },

                "printUnlistedTracks":
                  # Print the list of tracks that are not contained in any playlist.
                  # We ignore the master playlist ("Library"), all distinguished playlists ("Music", ...),
                  #   and playlists whose name is specified below.
                  {
                    # tasks.printUnlistedTracks.ignorePlaylists
                    # -----------------------------------------
                    # Specify playlists that should be ignored,
                    #   i.e. a track will be counted as unlisted even if it is contained in one of these playlists.
                    # Default value: []
                        "ignorePlaylists":
                          [
                            # "SomePlaylistName",
                          ],
                  }, # end of tasks.printUnlistedTracks

                "printMultiplyListedTracks":
                  # Print the list of tracks that are contained in more than one playlist.
                  # We ignore the master playlist ("Library"), all distinguished playlists ("Music", ...),
                  #   playlist folders, and playlists whose name is specified below.
                  {
                    # tasks.printMultiplyListedTracks.ignorePlaylists
                    # -----------------------------------------------
                    # Specify playlist names that should be ignored,
                    #   i.e. they will not be counted towards the number of playlists a track is in.
                    # Default value: []
                        "ignorePlaylists":
                          [
                            # "SomePlaylistName",
                          ],
                  }, # end of tasks.printMultiplyListedTracks

                "generatePlaylists":
                  # Export the iTunes playlists by generating .m3u files at a specified location.
                  {
                    # tasks.generatePlaylists.outputFolder
                    # ------------------------------------
                    # Folder at which the playlist files should be generated
                    #   Supports the %USERPROFILE% placeholder, which will typically get replaced by "C:\Users\<USERNAME>".
                    #   Note that backslashes ("\") need to be escaped as "\\".
                    # Default value: "%USERPROFILE%\\Desktop\\iExport\\Playlists"
                        "outputFolder":                 "%USERPROFILE%\\Desktop\\iExport\\Playlists",

                    # tasks.generatePlaylists.deleteFolder
                    # ------------------------------------
                    # If set to false, iExport will only proceed if outputFolder does not exist yet.
                    # If set to true, the folder will be deleted if it already exists.
                    # Default value: false
                        "deleteFolder":                 false,

                    # tasks.generatePlaylists.organizeInFolders
                    # -----------------------------------------
                    # If set to true, the playlists will be organized in folders.
                    #   e.g. an iTunes folder "POP" containing a playlist "80s" will lead to the file "POP/80s.m3u"
                    # If set to false, all playlists will be exported into the same folder.
                    #   Note that file name conflicts may arise if multiple playlists of the same name exist.
                    # Default value: true
                        "organizeInFolders":            true,

                    # tasks.generatePlaylists.hierarchicalNames
                    # -----------------------------------------
                    # Consider an iTunes folder "POP" containing a playlist "80s".
                    # If set to false, the playlist file will be called "80s.m3u".
                    # If set to true, the playlist file will be called "POP - 80s.m3u".
                    #   This setting may avoid naming conflicts if organizeInFolders is set to false.
                    # Default value: true
                        "hierarchicalNames":            true,

                    # tasks.generatePlaylists.onlyActualPlaylists
                    # -------------------------------------------
                    # If set to true, we will only export "actual" playlists (but not folders).
                    # If set to false, we will export all types of playlists including folders.
                    # Default value: false
                        "onlyActualPlaylists":          false,

                    # tasks.generatePlaylists.ignoreDistinguishedPlaylists
                    # ------------------------------------
                    # Set to true to not export distinguished playlists.
                    #   This includes playlists like "Music" (the entire music library), "Downloaded", and non-music libraries.
                    # Default value: false
                        "ignoreDistinguishedPlaylists": false,

                    # tasks.generatePlaylists.ignorePlaylists
                    # ---------------------------------------
                    # Specify playlist names that should be ignored, i.e. they will not be exported.
                    #   Note that if a folder is ignored, the playlists in it will still be exported.
                    # Default value: []
                        "ignorePlaylists":
                          [
                            # "SomePlaylistName",
                          ],

                    # tasks.generatePlaylists.playlistExtension
                    # -----------------------------------------
                    # The extension (e.g. ".m3u", ".m3u8") of the generated playlist files.
                    #   We recommend using ".m3u8" to signal that the paths inside the playlist may contain unicode.
                    # Default value: ".m3u8"
                        "playlistExtension":            ".m3u8",

                    # tasks.generatePlaylists.useRelativePaths
                    # ----------------------------------------
                    # If set to false, the generated playlist files will contain absolute paths to the tracks.
                    # If set to false, the generated playlist files will contain relative paths.
                    #   Consider a track stored at "C:\Music\Tracks\Track.mp3"
                    #   and assume we export the playlists to "C:\Music\Playlists\" (outputFolder).
                    #   If relative paths are enabled, a playlist containing that track will
                    #   contain "..\Tracks\Track.mp3" instead of "C:\Music\Tracks\Track.mp3".
                    # Advantage: The whole folder containing tracks and playlists can be copied to
                    #   another device (e.g. an android phone) and it will still work.
                    # Disadvantage: If the folder containing the playlists is moved,
                    #   they will stop working.
                    # Default value: false
                        "useRelativePaths":             false,

                    # tasks.generatePlaylists.warnSquareBrackets
                    # ------------------------------------------
                    # If set to true, iExport will print a warning if a file path inside a playlist
                    # contains square brackets ('[' or ']').
                    #   This is useful because some players require these to be escaped (e.g. VLC)
                    #   while others cannot deal with escaped brackets (e.g. Poweramp).
                    # Default value: true
                        "warnSquareBrackets":           true,

                    # tasks.generatePlaylists.slashAsSeparator
                    # ----------------------------------------------
                    # If set to false, use Backslash ('\', Windows notation) in file paths inside the playlist files.
                    # If set to true, use Slash ('/', Unix/Linux/Mac notation) in file paths inside the playlist files.
                    #   Note that this will not replace the first backslash in absolute Windows paths (e.g. C:\)
                    # Default value: false
                        "slashAsSeparator":             false,

                    # tasks.generatePlaylists.trackVerification
                    # ----------------------------------------------
                    # If set to true, we verify for each track that the corresponding file actually exists.
                    #   This may substantially slow down this task.
                    # Default value: true
                        "trackVerification":            true,


                    # tasks.generatePlaylists.showContinuousProgress
                    # ----------------------------------------------
                    # Whether to show a continuously updating progress bar while exporting
                    # Default value: true
                        "showContinuousProgress":       true,


                  }, # end of tasks.generatePlaylists

                "exportFiles":
                  # Export the iTunes playlists by creating folders that contain the tracks as files.
                  {
                    # tasks.exportFiles.outputFolder
                    # ------------------------------------
                    # Folder at which the playlist files should be generated
                    #   Supports the %USERPROFILE% placeholder, which will typically get replaced by "C:\Users\<USERNAME>".
                    #   Note that backslashes ("\") need to be escaped as "\\".
                    # Default value: "%USERPROFILE%\\Desktop\\iExport\\Files"
                        "outputFolder":                 "%USERPROFILE%\\Desktop\\iExport\\Files",

                    # tasks.exportFiles.deleteFolder
                    # ------------------------------------
                    # If set to false, iExport will only proceed if outputFolder does not exist yet.
                    # If set to true, the folder will be deleted if it already exists.
                    # Default value: false
                        "deleteFolder":                 false,

                    # tasks.exportFiles.hierarchicalNames
                    # -----------------------------------------
                    # Consider an iTunes folder "POP" containing a playlist "80s".
                    # If set to true, the playlist will be exported to the folder "POP - 80s".
                    # If set to false, the playlist will be exported to the folder "80s".
                    #   Note that if this is set to false,
                    #   name conflicts may arise if multiple playlists of the same name exist.
                    # Default value: true
                        "hierarchicalNames":            true,

                    # tasks.generatePlaylists.onlyActualPlaylists
                    # -------------------------------------------
                    # If set to true, we will only export "actual" playlists (but not folders).
                    # If set to false, we will export all types of playlists including folders.
                    # Default value: true
                        "onlyActualPlaylists":          true,

                    # tasks.exportFiles.ignoreDistinguishedPlaylists
                    # ------------------------------------
                    # Set to true to not export distinguished playlists.
                    #   This includes playlists like "Music" (the entire music library), "Downloaded", and non-music libraries.
                    # Default value: true
                        "ignoreDistinguishedPlaylists": true,

                    # tasks.exportFiles.ignorePlaylists
                    # ---------------------------------------
                    # Specify playlist names that should be ignored, i.e. they will not be exported.
                    #   Note that if a folder is ignored, the playlists in it will still be exported.
                    # Default value: []
                        "ignorePlaylists":
                          [
                            # "SomePlaylistName",
                          ],

                    # tasks.exportFiles.toRootFolder
                    # ---------------------------------------
                    # Specify playlist (via their name) that should not be exported into their own folder,
                    # but into the root folder (tasks.exportFiles.outputFolder) instead.
                    # Default value: []
                        "toRootFolder":
                          [
                            # "SomePlaylistName",
                          ],

                    # tasks.exportFiles.folderNumbering
                    # ----------------------------------------------
                    # Whether to number the folders.
                    #   If this is set to true, the folders will be called e.g. "1 POP", "2 ROCK", "3 ...".
                    #   If this is set to false, the folders will be called e..g. "POP", "ROCK", ...
                    # Default value: true
                        "folderNumbering":              true,

                    # tasks.exportFiles.padFolderNumbers
                    # -----------------------------------
                    # If both this and tasks.exportFiles.folderNumbering are set to true,
                    # the number at the beginning of each folder  name will be padded with leading zeros so that
                    # the numbers for all folder have the same length.
                    #   E.g. the first folder in a library with 10+ playlists 01.
                    # Default value: true
                        "padFolderNumbers":             true,

                    # tasks.exportFiles.initialNumber
                    # ----------------------------------------------
                    # The number of the first folder, see tasks.exportFiles.folderNumbering.
                    #   This is useful for e.g. car radios that show the root folder as 1.
                    # Default value: 1
                        "initialNumber":                1,

                    # tasks.exportFiles.trackNumbering
                    # --------------------------------
                    # Whether to number the tracks inside each folder.
                    #   If this is set to true, the file name for each track will be prefixes with "NUMBER - ".
                    # Default value: true
                        "trackNumbering":               true,

                    # tasks.exportFiles.padTrackNumbers
                    # -----------------------------------
                    # If both this and tasks.exportFiles.trackNumbering are set to true,
                    # the number at the beginning of each file name will be padded with leading zeros so that
                    # the numbers for all tracks in that folder have the same length.
                    #   E.g. the first track in a playlist with 100+ tracks will be numbered with 001.
                    # Default value: true
                        "padTrackNumbers":              true,

                    # tasks.exportFiles.normalize
                    # -----------------------------------
                    # If this is set to true, the folder and file names
                    #   will be normalized so that it only uses ASCII characters.
                    #   This may avoid compatibility problems with e.g. car radios.
                    # Default value: true
                        "normalize":                    true,

                    # tasks.exportFiles.showContinuousProgress
                    # ----------------------------------------------
                    # Whether to show a continuously updating progress bar while exporting
                    # Default value: true
                        "showContinuousProgress":       true,

                  }, # end of tasks.exportFiles

          }, # end of tasks

  } # end of root dictionary

...


```

Shortcomings: Sorting
---------------------

Unfortunately, iTunes does not seem to export the order of tracks within each playlist in the `iTunes Music Library.xml` file.
This means that the order of tracks that you have selected in iTunes is not available to iExport.

Instead, iExport sorts all tracks within a playlist using the following criteria:

* Prioritize tracks by their album artist (using the "sort as" field if available) or Artist (using the "sort as" field if available)
* Prioritize tracks with the same (album) artist by release year (earlier years first).
* Prioritize tracks with the same release year by album (using the "sort as" field if available)
* Prioritize tracks with the same album by disc number
* Prioritize tracks with the same disc number by track number
* Prioritize tracks with the same track number by their name (using the "sort as" field if available)

At this point in time, this sorting mechanism is not user-configurable; it is hardcoded in
[TrackComparator.java](src/main/java/iexport/parsing/sorting/TrackComparator.java).

JavaDoc
-------

You can generate JavaDoc documentation by running.

```sh
gradle :javadoc
```

The documentation will typically be generated in the folder `build/docs/javadoc`.

Dependencies
------------

iExport relies on *com.dd.plist* <https://github.com/3breadt/dd-plist> for parsing property list files.

iExport relies on *snakeyaml-engine* <https://bitbucket.org/snakeyaml/snakeyaml-engine/src/master/> for parsing YAML 1.2 files.

*Gradle* will automatically download and build these dependencies for you.
