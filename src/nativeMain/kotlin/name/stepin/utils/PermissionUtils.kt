package name.stepin.utils

import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import name.stepin.utils.Logger.log
import okio.Path
import platform.posix.chmod
import platform.posix.mode_t
import platform.posix.stat

/**
 * okio doesn't support permissions. So, work with POSIX permissions here.
 */
object PermissionUtils {
    const val MOD_644: UShort = 33188u

    @OptIn(UnsafeNumber::class)
    fun setPermissions(
        target: Path,
        permissions: UShort?,
    ) {
        log("permissions: $target to $permissions")
        if (permissions == null) return
        chmod(target.toString(), permissions as mode_t)
    }

    @OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, UnsafeNumber::class)
    fun getPermissions(target: Path): UShort? =
        memScoped {
            val result = alloc<stat>()
            if (stat(target.toString(), result.ptr) != 0) {
                return null
            }

            return result.st_mode as UShort?
        }
}
