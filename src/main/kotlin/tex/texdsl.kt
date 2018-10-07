package tex

import tex.base.Element
import tex.base.DocumentClass
import tex.base.TextElement
import tex.base.UserPackage
import java.lang.StringBuilder

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

class Document : TagWithText("document") {

    fun documentClass(argument : String, vararg attributes: String)
            = children.add(DocumentClass(argument, *attributes))

    fun userpackage(argument : String, vararg attributes: String)
            = children.add(UserPackage(argument, *attributes))

}

fun document(init: Document.() -> Unit): Document {
    val document = Document()
    document.init()
    return document
}

fun main(args: Array<String>) {
    val sb = StringBuilder()
    document {
        documentClass("foo", "1", "2")
        documentClass("foo")

        userpackage("foo", "1", "2")
        userpackage("foo")
    }.render(sb, "")
    println(sb.toString())
}