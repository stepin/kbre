package name.stepin.cli

import com.github.ajalt.clikt.core.CliktCommand

class VersionCmd :
    CliktCommand(
        name = "version",
        help = "Prints version of this tool",
    ) {
    companion object {
        const val VERSION = "SNAPSHOT"
    }

    override fun run() {
        println(VERSION)
    }
}
