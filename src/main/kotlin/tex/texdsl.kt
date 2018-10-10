package tex

import tex.base.*
import java.lang.StringBuilder

class Document : PairedTag("document") {

    private val headers = arrayListOf<Header>()

    override fun render(builder: StringBuilder, indent: String) {
        renderList(builder, indent, headers)
        super.render(builder, indent)
    }

    fun documentClass(argument: String) = headers.add(DocumentClass(argument))

    fun usepackage(argument: String, vararg attributes: String) = headers.add(Usepackage(argument, *attributes))

    fun math(init: TexMath.() -> Unit) = initTag(TexMath(), init)

    fun itemize(vararg attributes: String, init: Itemize.() -> Unit) {
        val itemize = initTag(Itemize(), init)
        itemize.attributes = attributes.toList()
    }

    fun enumerate(vararg attributes: String, init: Enumerate.() -> Unit) {
        val enumerate = initTag(Enumerate(), init)
        enumerate.attributes = attributes.toList()
    }

    fun frame(frameTitle: String, vararg attributes: String, init: Frame.() -> Unit) {
        val frame = initTag(Frame(), init)
        frame.children.add(0, FunctionTag("frametitle", frameTitle))
        frame.attributes = attributes.toList()
    }

    fun customTag(name: String, vararg attributes: String, init: CustomTag.() -> Unit) {
        val customTag = initTag(CustomTag(name), init)
        customTag.attributes = attributes.toList()
    }

    fun alignment(name : Alignment.TYPE, init : Alignment.() -> Unit) = initTag(Alignment(name), init)

}

class DocumentClass(
        argument: String
) : FunctionTag("documentclass", argument), Header

class Usepackage(
        argument: String,
        vararg attributes: String
) : FunctionTag("usepackage", argument, *attributes), Header

abstract class Listed(name: String) : PairedTag(name) {

    fun item(vararg attributes: String, init: Item.() -> Unit) {
        val item = initTag(Item(), init)
        item.attributes = attributes.toList()
    }

}

class TexMath : PairedTag("math")

class Enumerate : Listed("enumerate")

class Itemize : Listed("itemize")

class Item : IdentifierTag("item")

class Frame : PairedTag("frame")

class CustomTag(name: String) : PairedTag(name)

class Alignment(type : Alignment.TYPE) : PairedTag(type.type) {

    enum class TYPE(val type : String) {
        CENTER("center"),
        LEFT("flushleft"),
        RIGHT("flushright")
    }
}

fun document(init: Document.() -> Unit): Document {
    val document = Document()
    document.init()
    return document
}

infix fun String.to(value : String) : String {
    return "$this=$value"
}