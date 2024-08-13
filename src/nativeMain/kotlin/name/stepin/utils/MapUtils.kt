package name.stepin.utils

object MapUtils {

    fun Map<String, String>.uppercaseKeys(): Map<String, String> {
        return entries.associate { (key, value) -> key.uppercase() to value }
    }
}
