package name.stepin.action.init

import okio.Path

data class IntCmdDto(
    val configPath: Path,
    val templatesPath: Path,
    val group: String?,
    val artifact: String?,
    val name: String?,
    val description: String?,
    val preset: String?,
    val type: String?,
    val extensions: List<String>,
    val variables: Map<String, String>,
    val libs: String?,
    val imports: String?,
    val plugins: String?,
    val deps: String?,
    val buildDeps: String?,
    val body: String?,
    val verbose: Boolean,
)
