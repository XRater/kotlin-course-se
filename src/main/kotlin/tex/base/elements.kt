package tex.base

import java.lang.StringBuilder

@DslMarker
annotation class TexMarker

interface Element {

    fun render(builder : StringBuilder, indent : String)

}

abstract class TagWithText(val name: String) : Element {

    val children = arrayListOf<Element>()

    operator fun String.unaryPlus() {
        children.add(TextElement(this))
    }
}

@TexMarker
abstract class FunctionTag(
        private val name : String,
        private val argument : String,
        private vararg val attributes : String
) : Element {

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\$name[$argument]${renderAttributes(attributes)}\n")
    }

}

class TextElement(val text: String) : Element {

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$text\n")
    }

}