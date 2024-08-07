package name.stepin.cli

import com.github.ajalt.clikt.core.CliktCommand

class VersionCmd :
    CliktCommand(
        name = "version",
        help = "Prints version of this tool",
    ) {
    override fun run() {
        val version = "SNAPSHOT"
        println(version)
    }
}
