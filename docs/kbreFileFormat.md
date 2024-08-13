# Kbre.yaml File Format

Internally, kbre.yaml provides `project` extension.

Top-level keys:

- group (string) -- artifact group like `com.example`
- artifact (string) -- artifact id, used in many places, like `my-project`
- name (optional, string) -- human-readable project name, defaults to artifact
- description (optional, string) -- human-readable project description
- preset (string) -- folder inside templates directory
- type (string) -- folder inside preset directory
- extensions (array of strings) -- folders inside preset/extensions directory
- variables (optional, map of strings to strings) -- variables that will be used in templates or extensions
- notes (optional, string) -- message that should be shown after successful `new` or `update` action

All params in the file are optional but if param is not optional and it's missing in the file it should be defined
using cli parameter.
