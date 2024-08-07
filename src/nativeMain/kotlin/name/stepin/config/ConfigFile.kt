package name.stepin.config

import kotlinx.serialization.Serializable
import name.stepin.utils.FileUtils.load
import net.mamoe.yamlkt.Yaml
import okio.Path

@Serializable
data class ConfigFile(
    val group: String? = null,
    val artifact: String? = null,
    val name: String? = null,
    val description: String? = null,
    val type: String? = null,
    val preset: String? = null,
    val extensions: List<String> = emptyList(),
    val variables: Map<String, String> = emptyMap(),
    val libs: String? = null,
    val imports: String? = null,
    val plugins: String? = null,
    val deps: String? = null,
    val body: String? = null,
) {
    companion object {
        fun read(path: Path): ConfigFile {
            val fileContent = load(path)

            return Yaml.decodeFromString(serializer(), fileContent)
        }
    }
}
