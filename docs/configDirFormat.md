# Configuration dir description

Top-level folders are `presets`. It's like template when you create new project in IDE. Currently, there are 3:

- `cli` for Kotlin console apps
- `spring` for Kotlin web apps
- `script` for Kotlin scripts

## Type

Next, `type` should be selected. It's main template files. Default is `root`. It's for root project. There is
idea that in some cases variations of main template files will be needed (with the same extensions).

Files with extensions `.jar` and `.bat` are considered binary files, and they will be copied without variables substitution.
All other files are considered template files and variable substitution will be done.

## Extension

`Extension` is a most interesting concept. First of all, you can just copy any files. Variables will be substituted.
There is also special meaning for following files:

- `gradle/libs.versions.toml` -- it will be merged with `gradle/libs.versions.toml` from `type`
- `vars/vAr_NAME.EXTENSION` -- it will be added as `%VAR_NAME%` variable. Extension is ignored, it's for editors.
Case is ignored, it will be uppercase as variable. Values of variable is content of files.

Please, note that `project` extension name is reserved for data from `kbre.yaml` file.

Files with extensions `.jar` and `.bat` are considered binary files, and they will be copied without variables substitution.
All other files are considered template files and variable substitution will be done.

## Variable

`Variable` is key/value. They are defined in kbre.yaml file, and in `vars` folders in extensions.

Pre-defined variables from `kbre.yaml` file:

- `%PRESET%` -- preset
- `%TYPE%` -- type
- `%GROUP%` -- maven's group
- `%ARTIFACT%` -- in a lot of places used as binary name
- `%NAME%` -- some human app name
- `%DESCRIPTION%` -- human app description
- `%NOTES%` -- instructions what to do after generation
