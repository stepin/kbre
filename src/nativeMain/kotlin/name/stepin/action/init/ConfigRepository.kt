package name.stepin.action.init

import name.stepin.utils.FileUtils.listDirs
import name.stepin.utils.Logger.log
import okio.FileSystem
import okio.Path

class ConfigRepository(
    private val templatesPath: Path,
) {
    fun presets(): List<String> {
        log("presets")
        if (!FileSystem.SYSTEM.exists(templatesPath)) {
            log("missing $templatesPath")
            return emptyList()
        }

        return listDirs(templatesPath)
            .filter { !it.startsWith('.') }
    }

    fun types(preset: String): List<String> {
        log("types $preset")
        val path = templatesPath / preset
        if (!FileSystem.SYSTEM.exists(path)) {
            log("missing $path")
            return emptyList()
        }

        return listDirs(path)
            .filter { it != "extensions" && !it.endsWith("-new") }
    }

    fun extensions(preset: String): List<String> {
        log("extensions $preset")
        val path = templatesPath / preset / "extensions"
        if (!FileSystem.SYSTEM.exists(path)) {
            log("missing $path")
            return emptyList()
        }

        return listDirs(path)
            .filter { !it.endsWith("-new") }
    }
}
