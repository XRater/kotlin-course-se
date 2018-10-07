package tex

import java.lang.StringBuilder

@DslMarker
annotation class TexMarker

interface Element {

    fun render(builder : StringBuilder, indent : String)

}

class TextElement(val text: String) : Element {

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$text\n")
    }

}

@TexMarker
abstract class Tag(val name: String) : Element {

    val children = arrayListOf<Element>()

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\begin{$name}\n")
        for (child in children) {
            child.render(builder, "$indent  ")
        }
        builder.append("$indent\\end{$name}\n")
    }

}

abstract class TagWithText(name: String) : Tag(name) {
    operator fun String.unaryPlus() {
        children.add(TextElement(this))
    }
}

class Document : TagWithText("document")

fun document(init: Document.() -> Unit): Document {
    val document = Document()
    document.init()
    return document
}

fun main(args: Array<String>) {
    val sb = StringBuilder()
    document {
    }.render(sb, "")
    println(sb.toString())
}