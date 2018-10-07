package tex.base

import tex.TexMarker

@TexMarker
abstract class UnaryTag(
        private val name : String,
        private val argument : String,
        private vararg val attributes : String
) : Element {

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\$name[$argument]${renderAttributes(attributes)}\n")
    }

}

class DocumentClass(
        argument : String,
        vararg attributes: String
) : UnaryTag("documentClass", argument, *attributes)

class UserPackage(
        argument : String,
        vararg attributes: String
) : UnaryTag("userpackage", argument, *attributes)

fun renderAttributes(attributes: Array<out String>) =
    if (attributes.isEmpty()) "" else attributes.joinToString(", ", "{", "}")