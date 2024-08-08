package name.stepin.action.update

import name.stepin.utils.FileUtils.listRecursively
import name.stepin.utils.FileUtils.loadIfExists
import name.stepin.utils.WriteTarget
import okio.Path

data class Extension(
    val name: String,
    val libs: String?,
    val imports: String?,
    val plugins: String?,
    val deps: String?,
    val buildDeps: String?,
    val body: String?,
    val extra: Map<WriteTarget, Path>,
) {
    companion object {
        private val reservedFiles =
            setOf(
                "gradle/libs.versions.toml",
                "imports.kts",
                "plugins.kts",
                "deps.kts",
                "buildDeps.kts",
                "body.kts",
            )

        fun loadExtension(
            name: String,
            extensionsPath: Path,
            new: Boolean,
        ): Extension {
            val path = extensionsPath / name
            val extra =
                listRecursively(path)
                    .filterKeys { !reservedFiles.contains(it.relativeFilename) }
                    .entries.associate { it.key to it.value }

            val newExtra =
                if (new) {
                    listRecursively(extensionsPath / "$name-new")
                        .filterKeys { !reservedFiles.contains(it.relativeFilename) }
                        .entries.associate { it.key to it.value }
                } else {
                    emptyMap()
                }

            return Extension(
                name = name,
                libs = loadIfExists(path, "gradle/libs.versions.toml"),
                imports = loadIfExists(path, "imports.kts"),
                plugins = loadIfExists(path, "plugins.kts"),
                deps = loadIfExists(path, "deps.kts"),
                buildDeps = loadIfExists(path, "buildDeps.kts"),
                body = loadIfExists(path, "body.kts"),
                extra = extra + newExtra,
            )
        }
    }
}
