# Configuration dir description

Top-level folders are `presets`. It's like template when you create new project in IDE. Currently, there are 3:

- `cli` for Kotlin console apps
- `spring` for Kotlin web apps
- `script` for Kotlin scripts

Next, `type` should be selected. It's main template files. Default is `root`. It's for root project. If you have
subprojects most probably you will need other type for it.

`Extension` is most powerful concept. First of all, you can just copy with replaced variables any files. There is also
special meaning for following files:

- `gradle/libs.versions.toml` -- it will be merged with `gradle/libs.versions.toml` from `type`
- `imports.kts` -- it will be added to `%IMPORTS%` variable
- `plugins.kts` -- it will be added to `%PLUGINS%` variable
- `deps.kts` -- it will be added to `%DEPS%` variable
- `buildDeps.kts` -- it will be added to `%BUILD_DEPS%` variable (buildscript dependencies)
- `body.kts` -- it will be added to `%BODY%` variable

`Variable` is key/value. They are defined in kbre.yaml file and there are some pre-defined variable. In addition
to variables from extensions following variables are defined:

- `%PRESET%` -- preset
- `%TYPE%` -- type
- `%GROUP%` -- maven's group
- `%ARTIFACT%` -- in a lot of places used as binary name
- `%NAME%` -- some human app name
- `%DESCRIPTION%` -- human app description
