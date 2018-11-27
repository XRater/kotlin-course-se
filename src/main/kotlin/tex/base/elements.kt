package tex.base

import java.lang.StringBuilder

@DslMarker
annotation class TexMarker

interface Element {

    fun render(builder : StringBuilder, indent : String)

}

interface Header : Element

abstract class TagWithText : Element {

    val children = arrayListOf<Element>()

    operator fun String.unaryPlus() {
        children += TextElement(this)
    }
}

@TexMarker
open class FunctionTag(
        private val name : String,
        private val argument : String,
        private vararg val attributes : String
) : Element {

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\$name${renderAttributes(attributes.toList())}{$argument}\n")
    }

}

open class TextElement(private val text: String) : Element {

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$text\n")
    }

}