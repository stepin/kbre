package name.stepin.action.update

import name.stepin.utils.FileUtils.createBaseFolder
import name.stepin.utils.FileUtils.write
import name.stepin.utils.Logger.log
import name.stepin.utils.PermissionUtils.setPermissions
import name.stepin.utils.WriteTarget
import okio.FileSystem
import okio.Path

data class FilesGenerator(
    val targetFolder: Path,
    // filename to file content
    val templateFiles: Map<WriteTarget, String>,
    // filename to file path
    val copyFiles: Map<WriteTarget, Path>,
    // variable name to value
    val variables: Map<String, String>,
) {
    companion object {
        private val varRegEx = Regex("%[a-zA-Z0-9\\-_]*%")

        fun substituteVariables(
            template: String,
            variables: Map<String, String>,
        ): String {
            var content = template

            // replace defined variables
            variables.entries.forEach {
                content = content.replace("%${it.key}%", it.value)
            }

            // replace undefined variables
            content.replace(varRegEx, "")

            return content
        }
    }

    fun process() {
        log(this)
        copyFiles()
        generateFiles()
    }

    private fun copyFiles() {
        log("copying files")
        copyFiles.entries.forEach {
            val target = targetFolder / it.key.relativeFilename
            log("${it.value} -> $target")
            createBaseFolder(target)
            FileSystem.SYSTEM.copy(it.value, target)
            setPermissions(it.value, it.key.permissions)
        }
    }

    private fun generateFiles() {
        log("generating files")
        templateFiles.entries.forEach {
            val target = targetFolder / it.key.relativeFilename
            log("generating $target")
            val content = substituteVariables(it.value, variables)
            createBaseFolder(target)
            write(target, content)
            setPermissions(target, it.key.permissions)
        }
    }
}
