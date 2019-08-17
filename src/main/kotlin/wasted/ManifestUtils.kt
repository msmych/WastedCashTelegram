package wasted

object ManifestUtils {

  fun manifestValue(key: String): String? {
    return ManifestUtils::class.java.classLoader.getResource("META-INF/MANIFEST.MF")
      .readText()
      .split("\r\n")
      .find { it.startsWith(key) }
      ?.substringAfter(": ")
  }
}
