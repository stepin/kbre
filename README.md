# Kotlin Build Rules Enforcer

Kbre automates generation and update of project files for Gradle and other tools.

➡️ See https://kbre.stepin.name/ for documentation on [installation](https://kbre.stepin.name/installation/),
[configuration](https://kbre.stepin.name/configDirFormat/), and [commands](https://kbre.stepin.name/usage/).

## Status

Current status: it works for me. I use it for all my Kotlin projects.

Feel free to file bug reports. If I will be able to reproduce it I will fix it.

About new features: it's better to send PRs, not just ideas (as I already have ideas but limited time).

Some ideas:
- add yaml files merge (for `application.yml` cases: to compose it from different sources)
- add variables to the path / filenames -- it should be easy, but it looks unnecessary
- add `vars.yaml` for types and extensions as addition to `vars` folder for short values cases?
- allow to update list of extensions during next runs of init command (requires changes in mordant library to specify
initially selected values)
- create/update `krbe.yaml` file on `krbe new` command?
- add web version?

## License

Kbre is released under the [MIT License](LICENSE).
