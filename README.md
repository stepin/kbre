# Kotlin Build Rules Enforcer

## Why

If you try to use https://start.spring.io/ , https://kmp.jetbrains.com/ , https://start.ktor.io/ ,
or https://code.quarkus.io/ you will find out that:

1. it's not enough -- a lot of things needs to be configured after download
2. it's hard to update: update is done only manually

It's clear that for each company / team / individual templates should be different.
That's why https://github.com/stepin/kbre-default-config is done as starting point,
but you need to adapt it according to your preferences.

kbre tool uses your templates to create and update repos.

kbre started as Gradle macros tool, but now it doesn't depend on Gradle at all and can be used with any language.

## How to use

Configuration (it's even better to fork that repository and use your own from the beginning):
```shell
cd ~
git clone https://github.com/stepin/kbre-default-config .krbe
```

Create new Spring app:
```shell
mkdir my-app
cd my-app

cat > kbre.yaml << \EOF
group: name.stepin
artifact: myapp
name: My app
description: Some description
preset: spring
type: root
extensions:
  - graphql
  - postgres
  - flyway
  - jooq
  - dokka
  - jib
  - local-dev
  - systemd-deployment
variables:
  REPO: http://localhost:3000/stepin/kotlin-bootstrap-app/src/branch/main/src/main/kotlin
  SONAR_HOST_URL: http://localhost:9000
  SONAR_PROJECT_KEY: kotlin-bootstrap-app
  SONAR_PROJECT_NAME: kotlin-bootstrap-app
  SONAR_TOKEN: sqp_821b1d3209761625bdd29259674237d429bce626
EOF

alias kbre='docker run --rm -it -v $PWD:/data -w /data --user "$(id -u)" stepin/kbre'
kbre version

kbre new
```

Update (when config repo or kbre.yaml are changed):
```shell
cd my-app

kbre update
```

## Configuration dir description

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
- `body.kts` -- it will be added to `%BODY%` variable

`Variable` is key/value. They are defined in kbre.yaml file and there are some pre-defined variable. In addition
to variables from extensions following variables are defined:
- `%PRESET%` -- preset
- `%TYPE%` -- type
- `%GROUP%` -- maven's group
- `%ARTIFACT%` -- in a lot of places used as binary name
- `%NAME%` -- some human app name
- `%DESCRIPTION%` -- human app description


## Status

Current status: works for me. I use it for all projects.

Feel free to file bug reports. If I will be able to reproduce it I will fix it.

About new features: it's better to send PRs, not just ideas (as I already have ideas but limited time).

Some ideas:
- add variables to the path / filenames -- it should be easy, I just don't need it
- create krbe.yaml file on `krbe new` command
- add `krbe init` action for interactive creation of project (like in Poetry)
- add web version
- add brew tap


## Limitations

- it's unclear how to set up unit tests for Kotlin Native properly
- maybe support of `libs.versions.toml` has problems in some corner cases: it's interesting to have examples
- it's unclear how to build Kotlin Native in Docker: in all cases it's not working for me (but maybe it's ram limitation, unclear). There is ticket about it: https://youtrack.jetbrains.com/issue/KT-39895/Missing-Documentation-On-Using-Kotlin-Native-With-Docker .
So, for now, binaries are just added to Docker image.
- binary files detection is really simple now
- Alpine is not supported as Kotlin compiler don't work with `musl libc`. Even `gcompat` package don't fix it (most probably some detection problem).
- Kotlin Native compiler is not available for Linux ARM, only for macOS ARM. So, no ARM docker images. Missing file: https://repo.maven.apache.org/maven2/org/jetbrains/kotlin/kotlin-native-prebuilt/2.0.0/kotlin-native-prebuilt-2.0.0-linux-aarch64.tar.gz
It means that we need to compile binaries on 1 supported arch, then add them into docker images.
