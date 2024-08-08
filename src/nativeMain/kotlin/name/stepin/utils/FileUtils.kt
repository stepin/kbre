package name.stepin.utils

import name.stepin.utils.Logger.log
import name.stepin.utils.PermissionUtils.getPermissions
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

object FileUtils {
    fun workingDir(filename: String?): Path {
        log("workingDir $filename")
        if (filename == null) {
            return cwd()
        }
        return FileSystem.SYSTEM.canonicalize(filename.toPath())
    }

    private fun cwd(): Path = FileSystem.SYSTEM.canonicalize(".".toPath())

    fun checkIfExists(
        path: Path,
        filename: String,
    ): Path? {
        val filepath = path / filename
        val exists = FileSystem.SYSTEM.exists(filepath)
        if (!exists) return null
        return filepath
    }

    fun createBaseFolder(path: Path) {
        log("creating base folder $path")
        val folder =
            path.parent
                ?: return
        if (!FileSystem.SYSTEM.exists(folder)) {
            FileSystem.SYSTEM.createDirectories(folder)
        }
    }

    fun load(path: Path): String {
        log("loading $path")
        return FileSystem.SYSTEM.read(path) {
            readUtf8()
        }
    }

    fun listRecursively(path: Path): Map<WriteTarget, Path> {
        log("listing $path")
        if (!FileSystem.SYSTEM.exists(path)) {
            log("missing $path")
            return emptyMap()
        }
        return FileSystem.SYSTEM.listRecursively(path)
            .filter { !FileSystem.SYSTEM.metadata(it).isDirectory }
            .associate {
                val filename = it.relativeTo(path).toString()
                val permissions = getPermissions(it)
                WriteTarget(filename, permissions) to path / it
            }
    }

    fun listDirs(path: Path): List<String> {
        return FileSystem.SYSTEM.list(path)
            .filter { FileSystem.SYSTEM.metadata(it).isDirectory }
            .map {
                it.relativeTo(path).toString()
            }
    }

    fun write(
        path: Path,
        content: String,
    ) {
        log("writing $path")
        FileSystem.SYSTEM.write(path) {
            writeUtf8(content)
        }
    }

    fun loadIfExists(
        path: Path,
        filename: String,
    ): String? {
        val filepath = path / filename
        log("loadIfExists $filepath")
        val exists = FileSystem.SYSTEM.exists(filepath)
        if (!exists) return null
        return load(filepath)
    }
}
