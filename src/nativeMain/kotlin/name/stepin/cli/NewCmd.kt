package name.stepin.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import name.stepin.action.UpdateAction
import name.stepin.action.UpdateConfiguration
import name.stepin.config.ConfigFile
import name.stepin.utils.FileUtils.checkIfExists
import name.stepin.utils.FileUtils.workingDir
import name.stepin.utils.System
import okio.Path.Companion.toPath

class NewCmd :
    CliktCommand(
        name = "new",
        help = "Generates new project. New is an alias for update with option --new.",
    ) {
    private val globalOptions by requireObject<GlobalOptions>()

    private val targetPath by option(
        "--target-path",
        help = "Target folder (optional, otherwise current folder is used)",
    )
    private val configFile by option(
        "-c",
        "--config",
        help = "Configuration file (optional, otherwise file kbre.yaml is checked in target folder)",
    )
    private val templatesPath by option(
        "--templates-path",
        help = "Path to templates.",
    ).default("${System.getenv("HOME") ?: ""}/.kbre")

    private val group by option(
        "-g",
        "--group",
        help = "Group name",
    )
    private val artifact by option(
        "-a",
        "--artifact",
        help = "Artifact name",
    )
    private val name by option(
        "-n",
        "--name",
        help = "Project name",
    )
    private val description by option(
        "--description",
        help = "Description",
    )
    private val preset by option(
        "-p",
        "--preset",
        help = "Preset",
    )
    private val type by option(
        "-t",
        "--type",
        help = "Type of project",
    )
    private val extensions by option(
        "-e",
        "--extension",
        help = "Extension to be used",
    ).multiple()
    private val variables by option(
        "--variable",
        help = "Project-specific variables",
    ).multiple()
    private val libs by option(
        "--libs",
        help = "Project-specific libs (libs.versions.toml file format)",
    )
    private val imports by option(
        "-i",
        "--imports",
        help = "Project-specific imports",
    )
    private val plugins by option(
        "--plugins",
        help = "Project-specific plugins",
    )
    private val deps by option(
        "-d",
        "--deps",
        help = "Project-specific deps",
    )
    private val buildDeps by option(
        "--build-deps",
        help = "Project-specific buildscript deps",
    )
    private val body by option(
        "-b",
        "--body",
        help = "Project-specific body",
    )

    override fun run() {
        val variablesMap =
            variables.associate {
                val parts = it.split('=').map(String::trim)
                if (parts.size != 2) {
                    throw RuntimeException("variable should be in format NAME=VALUE, $it")
                }
                parts[0] to parts[1]
            }

        val currentDir = workingDir(targetPath)
        val configPath = configFile?.toPath() ?: checkIfExists(currentDir, "kbre.yaml")
        val updateConfiguration =
            if (configPath != null) {
                val file = ConfigFile.read(configPath)
                UpdateConfiguration(
                    templatesPath = templatesPath.toPath(),
                    targetFolder = currentDir,
                    new = true,
                    group = group ?: file.group ?: throw RuntimeException("group should be specified"),
                    artifact = artifact ?: file.artifact ?: throw RuntimeException("artifact should be specified"),
                    name = name ?: file.name,
                    description = description ?: file.description,
                    preset = preset ?: file.preset ?: throw RuntimeException("preset should be specified"),
                    type = type ?: file.type ?: throw RuntimeException("type should be specified"),
                    extensions = extensions + file.extensions,
                    variables = variablesMap + file.variables,
                    libs = libs ?: file.libs,
                    imports = imports ?: file.imports,
                    plugins = plugins ?: file.plugins,
                    deps = deps ?: file.deps,
                    buildDeps = buildDeps,
                    body = body ?: file.body,
                    verbose = globalOptions.verbose,
                )
            } else {
                UpdateConfiguration(
                    templatesPath = templatesPath.toPath(),
                    targetFolder = currentDir,
                    new = true,
                    group = group ?: throw RuntimeException("group should be specified"),
                    artifact = artifact ?: throw RuntimeException("artifact should be specified"),
                    name = name,
                    description = description,
                    preset = preset ?: throw RuntimeException("preset should be specified"),
                    type = type ?: throw RuntimeException("type should be specified"),
                    extensions = extensions,
                    variables = variablesMap,
                    libs = libs,
                    imports = imports,
                    plugins = plugins,
                    deps = deps,
                    buildDeps = buildDeps,
                    body = body,
                    verbose = globalOptions.verbose,
                )
            }

        UpdateAction().process(updateConfiguration)
    }
}
