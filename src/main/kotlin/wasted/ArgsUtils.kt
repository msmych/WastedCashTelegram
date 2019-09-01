package wasted

fun argPresent(args: Array<String>, arg: String): Boolean {
  return args.any { it == arg }
}

fun findArgEq(args: Array<String>, name: String): String? {
  return args.find { it.startsWith(name) }
    ?.substringAfter('=')
}
