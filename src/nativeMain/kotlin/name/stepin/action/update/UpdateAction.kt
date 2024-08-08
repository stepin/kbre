package name.stepin.action.update

import name.stepin.action.update.FilesGenerator.Companion.getContent
import name.stepin.utils.FileUtils.listRecursively
import name.stepin.utils.FileUtils.load
import name.stepin.utils.Logger
import name.stepin.utils.Logger.log
import name.stepin.utils.PermissionUtils.MOD_644
import name.stepin.utils.WriteTarget
import okio.FileSystem
import okio.Path

class UpdateAction {
    fun process(configuration: UpdateConfiguration) {
        Logger.enabled = configuration.verbose
        if (configuration.verbose) {
            log(configuration)
        }

        val presetPath = configuration.templatesPath / configuration.preset
        if (!FileSystem.SYSTEM.exists(presetPath)) {
            throw RuntimeException("Preset does not exist: $presetPath")
        }

        val extensions = prepareExtensions(presetPath, configuration)

        val basicVariables = prepareVariables(configuration)
        val variables = basicVariables + extensionsToVariables(extensions, basicVariables)

        val extraFiles = extensions.flatMap { it.extra.entries }.associate { it.key to it.value }
        val (extraCopyFiles, extraTemplateFiles) = prepareExtraFiles(extraFiles)

        val (copyFiles, templateFiles) = prepareTemplate(presetPath / configuration.type)
        val (newCopyFiles, newTemplateFiles) =
            if (configuration.new) {
                val newTypePath = presetPath / "${configuration.type}-new"
                if (FileSystem.SYSTEM.exists(newTypePath)) {
                    prepareTemplate(newTypePath)
                } else {
                    Pair(emptyMap(), emptyMap())
                }
            } else {
                Pair(emptyMap(), emptyMap())
            }

        val libsTemplateFile = prepareLibsVersionsTemplate(templateFiles, extensions)

        FilesGenerator(
            targetFolder = configuration.targetFolder,
            templateFiles = templateFiles + newTemplateFiles + libsTemplateFile + extraTemplateFiles,
            copyFiles = copyFiles + newCopyFiles + extraCopyFiles,
            variables = variables,
        ).process()
    }

    private fun prepareExtensions(
        presetPath: Path,
        configuration: UpdateConfiguration,
    ): List<Extension> {
        val extensionsPath = presetPath / "extensions"
        val extensions: MutableList<Extension> =
            configuration.extensions.map { extension ->
                Extension.loadExtension(extension, extensionsPath, configuration.new)
            }.toMutableList()
        extensions.add(projectExtension(configuration))
        return extensions
    }

    private fun projectExtension(configuration: UpdateConfiguration) =
        Extension(
            name = "project",
            libs = configuration.libs,
            imports = configuration.imports,
            plugins = configuration.plugins,
            deps = configuration.deps,
            buildDeps = configuration.buildDeps,
            body = configuration.body,
            extra = emptyMap(),
        )

    private fun extensionsToVariables(
        extensions: List<Extension>,
        variables: Map<String, String>,
    ): Map<String, String> {
        return mapOf(
            "BODY" to
                extensions
                    .filter { it.body != null }
                    .joinToString(separator = "\n") { getContent(it.body ?: "", variables) },
            "DEPS" to
                extensions
                    .filter { it.deps != null }
                    .joinToString(separator = "\n") { getContent(it.deps ?: "", variables) },
            "BUILD_DEPS" to
                extensions
                    .filter { it.deps != null }
                    .joinToString(separator = "\n") { getContent(it.buildDeps ?: "", variables) },
            "IMPORTS" to
                extensions
                    .filter { it.imports != null }
                    .joinToString(separator = "\n") { getContent(it.imports ?: "", variables) },
            "PLUGINS" to
                extensions
                    .filter { it.plugins != null }
                    .joinToString(separator = "\n") { getContent(it.plugins ?: "", variables) },
        )
    }

    private fun prepareVariables(configuration: UpdateConfiguration): Map<String, String> {
        return configuration.variables +
            mapOf(
                "GROUP" to configuration.group,
                "ARTIFACT" to configuration.artifact,
                "NAME" to (configuration.name ?: configuration.artifact),
                "PRESET" to configuration.preset,
                "TYPE" to configuration.type,
                "DESCRIPTION" to (configuration.description ?: ""),
            )
    }

    private fun prepareTemplate(path: Path): Pair<Map<WriteTarget, Path>, Map<WriteTarget, String>> {
        if (!FileSystem.SYSTEM.exists(path)) {
            throw RuntimeException("Type folder $path does not exist")
        }

        val files = listRecursively(path)
        val copyFiles = files.filter(isBinary())
        val templateFiles =
            (files - copyFiles.keys).entries.associate {
                it.key to load(it.value)
            }

        return Pair(copyFiles, templateFiles)
    }

    private fun prepareExtraFiles(
        files: Map<WriteTarget, Path>,
    ): Pair<Map<WriteTarget, Path>, Map<WriteTarget, String>> {
        val copyFiles = files.filter(isBinary())
        val templateFiles =
            (files - copyFiles.keys).entries.associate {
                it.key to load(it.value)
            }

        return Pair(copyFiles, templateFiles)
    }

    private fun isBinary(): (Map.Entry<WriteTarget, Any>) -> Boolean =
        {
            it.key.relativeFilename.endsWith(".jar")
        }

    private fun prepareLibsVersionsTemplate(
        templateFiles: Map<WriteTarget, String>,
        extensions: List<Extension>,
    ): Map<WriteTarget, String> {
        val libsVersionsPath = "gradle/libs.versions.toml"
        val tomlFromTemplate = templateFiles.entries.find { it.key.relativeFilename == libsVersionsPath }
        val libsVersionsTomls = listOfNotNull(tomlFromTemplate?.value) + extensions.mapNotNull { it.libs }
        val libsVersionsToml = TomlFile.combineFiles(libsVersionsTomls)
        if (libsVersionsToml.isEmpty()) return emptyMap()
        return mapOf(
            WriteTarget(libsVersionsPath, MOD_644) to libsVersionsToml,
        )
    }
}
