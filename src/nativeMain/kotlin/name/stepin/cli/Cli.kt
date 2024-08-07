package name.stepin.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option

class Cli :
    CliktCommand(
        name = "kbre",
        printHelpOnEmptyArgs = true,
        help = "Automates generation and update of Gradle config files in JS/Python way.",
    ) {
    private val verbose by option("-v", "--verbose", help = "More details in output").flag()

    override fun run() {
        currentContext.obj = GlobalOptions(verbose)
    }
}
