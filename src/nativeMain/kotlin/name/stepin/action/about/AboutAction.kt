package name.stepin.action.about

import com.github.ajalt.mordant.rendering.TextColors.blue
import com.github.ajalt.mordant.rendering.TextColors.green
import com.github.ajalt.mordant.terminal.Terminal

class AboutAction {
    fun process(version: String) {
        val t = Terminal()
        t.println(blue("Kbre - Kotlin Build Rules Enforcer"))
        t.println()
        t.println(blue("Version: $version"))
        t.println()
        t.println(green("Kbre is a project generator and updater for Gradle and other tools."))
        t.println(green("See ${blue("https://github.com/stepin/kbre")} for more information."))
    }
}
