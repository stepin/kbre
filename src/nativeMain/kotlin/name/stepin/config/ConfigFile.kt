package name.stepin.config

import kotlinx.serialization.Serializable
import name.stepin.utils.FileUtils
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
    val notes: String? = null,
) {
    companion object {
        fun read(path: Path): ConfigFile {
            val fileContent = load(path)

            return Yaml.decodeFromString(serializer(), fileContent)
        }
    }

    override fun toString(): String {
        return Yaml.encodeToString(serializer(), this)
    }

    fun write(path: Path) {
        FileUtils.write(path, this.toString())
    }
}
