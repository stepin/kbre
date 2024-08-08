package name.stepin.cli

import com.github.ajalt.clikt.core.CliktCommand
import name.stepin.action.about.AboutAction

class AboutCmd :
    CliktCommand(
        name = "about",
        help = "Shows information about kbre",
    ) {
    override fun run() {
        AboutAction().process(VersionCmd.VERSION)
    }
}
