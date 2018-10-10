package tex.base

fun renderAttributes(attributes: List<String>) =
    if (attributes.isEmpty()) "" else attributes.joinToString(", ", "[", "]")

infix fun String.to(value : String) : String {
    return "$this=$value"
}