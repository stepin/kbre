package name.stepin.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString

object System {
    @OptIn(ExperimentalForeignApi::class)
    fun getenv(variable: String): String? {
        return platform.posix.getenv(variable)?.toKString()
    }
}
