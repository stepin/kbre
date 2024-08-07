package name.stepin.utils

object Logger {
    var enabled = false

    fun log(message: Any?) {
        if (!enabled) return
        println(message)
    }
}
