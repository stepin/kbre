package name.stepin.action

/**
 * Format of libs.versions.toml file is quite complex.
 * I tried ktoml to parse and serialize it, but it doesn't work good enough.
 *
 * So, I decided that custom parser will work for most cases (I don't know cases when it will not work).
 */
data class TomlFile(
    val tables: MutableMap<String, MutableList<String>>,
) {
    companion object {
        fun combineFiles(tomlFiles: List<String>): String {
            val combined: TomlFile? =
                tomlFiles.map { parse(it) }
                    .fold(null as TomlFile?) { acc, value ->
                        if (acc == null) {
                            value
                        } else {
                            acc.combine(value)
                            acc
                        }
                    }
            return combined?.toString() ?: ""
        }

        fun parse(fileContent: String): TomlFile {
            val tables = HashMap<String, MutableList<String>>()
            val lines = fileContent.split("\n")
            var rootTable = true
            var currentTable = mutableListOf<String>()
            lines.forEach { line ->
                if (line.isEmpty()) return@forEach
                val isNewTable = line.startsWith("[")
                if (isNewTable) {
                    if (tables.containsKey(line)) {
                        throw RuntimeException("Duplicate tables found in $line")
                    }

                    if (rootTable && currentTable.isNotEmpty()) {
                        tables[""] = currentTable
                    }
                    rootTable = false

                    currentTable = mutableListOf()
                    tables[line] = currentTable
                } else {
                    currentTable.add(line)
                }
            }

            return TomlFile(tables)
        }
    }

    fun combine(other: TomlFile) {
        other.tables.entries.forEach {
            if (this.tables.containsKey(it.key)) {
                this.tables[it.key]!!.addAll(it.value)
            } else {
                this.tables[it.key] = it.value
            }
        }
    }

    override fun toString(): String {
        val out = StringBuilder()
        tables.forEach { table ->
            out.append(table.key).append('\n')
            table.value.forEach { value ->
                out.append(value).append('\n')
            }
            out.append('\n')
        }
        // remove second \n
        out.deleteAt(out.length - 1)
        return out.toString()
    }
}
