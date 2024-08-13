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
    val notes: String?,
    val verbose: Boolean,
)
