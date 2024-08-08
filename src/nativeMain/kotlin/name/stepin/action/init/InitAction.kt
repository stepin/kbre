package name.stepin.action.init

import com.github.ajalt.mordant.input.interactiveMultiSelectList
import com.github.ajalt.mordant.input.interactiveSelectList
import com.github.ajalt.mordant.rendering.TextColors.blue
import com.github.ajalt.mordant.rendering.TextColors.green
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.YesNoPrompt
import name.stepin.config.ConfigFile
import name.stepin.utils.Logger.log

class InitAction {
    private val t = Terminal()

    fun process(cmd: IntCmdDto) {
        log(cmd)

        t.println()
        t.println(green("This command will guide you through creating your ${blue("kbre.yaml")} config."))
        t.println(green("It only covers the most common items, and tries to guess sensible defaults."))
        t.println()

        val configRepository = ConfigRepository(cmd.templatesPath)
        val targetFolderName = cmd.configPath.parent?.name

        val group = t.prompt("Group name", default = cmd.group ?: "com.example")
        val artifact = t.prompt("Artifact", default = cmd.artifact ?: targetFolderName ?: "demo")
        val name = t.prompt("Name", default = cmd.name)
        val description = t.prompt("Description", default = cmd.description)
        val presets = configRepository.presets()
        if (presets.isEmpty()) {
            t.warning("presets list is empty")
            return
        }
        val preset =
            cmd.preset ?: t.interactiveSelectList(
                presets,
                title = "Select a preset",
            )
        if (preset.isNullOrEmpty()) {
            t.warning("preset is not selected")
            return
        }
        val types = configRepository.types(preset)
        if (types.isEmpty()) {
            t.warning("types list is empty")
            return
        }
        val type =
            cmd.type ?: t.interactiveSelectList(
                types,
                title = "Select a type",
            )
        if (type.isNullOrEmpty()) {
            t.warning("type is not selected")
            return
        }
        val availableExtensions = configRepository.extensions(preset)
        val extensions =
            cmd.extensions.ifEmpty {
                if (availableExtensions.isNotEmpty()) {
                    t.interactiveMultiSelectList(
                        availableExtensions,
                        title = "Select extensions",
                    ) ?: emptyList()
                } else {
                    emptyList()
                }
            }
        val variables =
            cmd.variables.ifEmpty {
                val result = mutableMapOf<String, String>()
                do {
                    val variable = t.prompt("Variable value (key=value, empty line to stop)")
                    if (!variable.isNullOrEmpty()) {
                        val parts = variable.split('=').map(String::trim)
                        if (parts.size != 2) {
                            t.warning("variable should be in format NAME=VALUE, $variable")
                        }
                        result[parts[0]] = parts[1]
                    }
                } while (!variable.isNullOrEmpty())
                result
            }

        val configFile =
            ConfigFile(
                group,
                artifact,
                name,
                description,
                type,
                preset,
                extensions,
                variables,
                cmd.libs,
                cmd.imports,
                cmd.plugins,
                cmd.deps,
                cmd.buildDeps,
                cmd.body,
            )

        t.println()
        t.println(green("Generated file:"))
        t.println()
        t.println(configFile.toString())
        t.println()
        t.println()
        val generate = YesNoPrompt("Do you confirm generation?", t).ask()
        if (generate == true) {
            configFile.write(cmd.configPath)
            t.info("Now you can generate files using ${green("kbre new")} command.")
        } else {
            t.println()
            t.danger("Aborted.")
        }
    }
}
