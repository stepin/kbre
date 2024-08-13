package name.stepin.action.update

import okio.Path

data class UpdateConfiguration(
    val templatesPath: Path,
    val targetFolder: Path,
    val new: Boolean,
    val group: String,
    val artifact: String,
    val name: String?,
    val description: String?,
    val preset: String,
    val type: String,
    val extensions: List<String>,
    val variables: Map<String, String>,
    val notes: String?,
    val verbose: Boolean,
)
