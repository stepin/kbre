# Kotlin Build Rules Enforcer

Kbre automates generation and update of project files for Gradle and other tools.

➡️ See https://kbre.stepin.name/ for documentation on [installation](https://kbre.stepin.name/installation/),
[configuration](https://kbre.stepin.name/configDirFormat/), and [commands](https://kbre.stepin.name/usage/).

## Status

Current status: it works for me. I use it for all my Kotlin projects.

Feel free to file bug reports. If I will be able to reproduce it I will fix it.

About new features: it's better to send PRs, not just ideas (as I already have ideas but limited time).

Some ideas:
- recheck how it should work with subprojects (like added one more special case for `libs.versions.toml`)
- add variables to the path / filenames -- it should be easy, but it looks unnecessary
- create krbe.yaml file on `krbe new` command?
- add web version?

## License

Kbre is released under the [MIT License](LICENSE).
