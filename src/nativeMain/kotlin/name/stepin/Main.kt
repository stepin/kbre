package name.stepin

import com.github.ajalt.clikt.core.subcommands
import name.stepin.cli.Cli
import name.stepin.cli.NewCmd
import name.stepin.cli.UpdateCmd
import name.stepin.cli.VersionCmd

fun main(args: Array<String>) {
    Cli()
        .subcommands(
            VersionCmd(),
            NewCmd(),
            UpdateCmd(),
        ).main(args)
}
