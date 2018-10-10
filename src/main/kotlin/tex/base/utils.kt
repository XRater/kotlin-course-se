package tex.base

import java.lang.StringBuilder

fun renderAttributes(attributes: List<String>) =
    if (attributes.isEmpty()) "" else attributes.joinToString(", ", "[", "]")

fun renderList(builder: StringBuilder, indent : String, attributes: List<Element>) {
    for (attribute in attributes) {
        attribute.render(builder, indent)
    }
}

