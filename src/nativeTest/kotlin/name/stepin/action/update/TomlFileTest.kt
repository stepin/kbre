package name.stepin.action.update

import kotlin.test.Test
import kotlin.test.assertEquals

class TomlFileTest {
    companion object {
        val tomlFileContent1 =
            """
            [versions]
            kotlinxSerialization = "2.0.0"
            yamlkt = "0.13.0"

            [plugins]
            kotlinx-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlinxSerialization" }

            [libraries]
            yamlkt = { module = "net.mamoe.yamlkt:yamlkt", version.ref = "yamlkt" }
            """.trimIndent()
        val tomlFileContent2 =
            """
            [versions]
            okio = "3.9.0"

            [libraries]
            okio = { module = "com.squareup.okio:okio", version.ref = "okio" }
            """.trimIndent()
    }

    @Test
    fun `main case`() {
        val expected =
            """
            [versions]
            kotlinxSerialization = "2.0.0"
            yamlkt = "0.13.0"
            okio = "3.9.0"

            [plugins]
            kotlinx-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlinxSerialization" }

            [libraries]
            yamlkt = { module = "net.mamoe.yamlkt:yamlkt", version.ref = "yamlkt" }
            okio = { module = "com.squareup.okio:okio", version.ref = "okio" }

            """.trimIndent()
        val toml1 = TomlFile.parse(tomlFileContent1)
        val toml2 = TomlFile.parse(tomlFileContent2)

        toml1.combine(toml2)
        val content = toml1.toString()

        assertEquals(expected, content)
    }
}
