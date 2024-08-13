package name.stepin.action.update

import name.stepin.utils.FileUtils.listRecursively
import name.stepin.utils.FileUtils.loadIfExists
import name.stepin.utils.WriteTarget
import okio.Path

data class Extension(
    val name: String,
    val variables: Map<String, String>,
    val extra: Map<WriteTarget, Path>,
) {
    companion object {
        private val reservedFiles =
            setOf(
                "gradle/libs.versions.toml",
            )

        fun loadExtension(
            name: String,
            extensionsPath: Path,
            new: Boolean,
        ): Extension {
            val path = extensionsPath / name
            val extra =
                listRecursively(path)
                    .filterKeys {
                        !reservedFiles.contains(it.relativeFilename) && !it.relativeFilename.startsWith("vars/")
                    }
                    .entries.associate { it.key to it.value }

            val newExtra =
                if (new) {
                    listRecursively(extensionsPath / "$name-new")
                        .filterKeys {
                            !reservedFiles.contains(it.relativeFilename) && !it.relativeFilename.startsWith("vars/")
                        }
                        .entries.associate { it.key to it.value }
                } else {
                    emptyMap()
                }

            val libs = loadIfExists(path / "gradle/libs.versions.toml")
            val libsMap = if (libs.isNullOrEmpty()) emptyMap() else mapOf("LIBS" to libs)

            val vars =
                listRecursively(path / "vars")
                    .entries.mapNotNull {
                        val filename = it.key.relativeFilename

                        val content =
                            loadIfExists(path / "vars" / filename)
                                ?: return@mapNotNull null

                        val varName = filename.substringBeforeLast(".").uppercase()

                        varName to content
                    }
                    .fold(mutableMapOf<String, String>()) { map, (k, v) ->
                        map[k] =
                            if (map.containsKey(k)) {
                                map[k] + "\n" + v
                            } else {
                                v
                            }
                        map
                    }

            return Extension(
                name = name,
                variables = vars + libsMap,
                extra = extra + newExtra,
            )
        }
    }
}
