package tex

import tex.base.*
import java.lang.StringBuilder

class Document : PairedTag("document") {

    fun documentClass(argument: String) = children.add(DocumentClass(argument))

    fun usepackage(argument: String, vararg attributes: String) = children.add(Usepackage(argument, *attributes))

    fun math(init: TexMath.() -> Unit) = initTag(TexMath(), init)

    fun itemize(init: Itemize.() -> Unit) {
        val itemize = initTag(Itemize(), init)
        itemize.attributes = attributes.toList()
    }

    fun enumerate(vararg attributes: String, init: Enumerate.() -> Unit) {
        val enumerate = initTag(Enumerate(), init)
        enumerate.attributes = attributes.toList()
    }

    fun frame(frameTitle: String, vararg attributes: String, init: Frame.() -> Unit) {
        val frame = initTag(Frame(), init)
        frame.children.add(FunctionTag("frametitle", frameTitle))
        frame.attributes = attributes.toList()
    }

    fun customTag(name: String, vararg attributes: String, init: CustomTag.() -> Unit) {
        val customTag = initTag(CustomTag(name), init)
        customTag.attributes = attributes.toList()
    }
}

class DocumentClass(
        argument: String
) : FunctionTag("documentClass", argument)

class Usepackage(
        argument: String,
        vararg attributes: String
) : FunctionTag("userpackage", argument, *attributes)

abstract class Listed(name: String) : PairedTag(name) {

    fun item(vararg attributes: String, init: Item.() -> Unit) {
        val item = initTag(Item(), init)
        item.attributes = attributes.toList()
    }

}

class TexMath : PairedTag("math")

class Enumerate : Listed("itemize")

class Itemize : Listed("enumerate")

class Item : IdentifierTag("item")

class Frame : PairedTag("frame")

class CustomTag(name: String) : PairedTag(name)

fun document(init: Document.() -> Unit): Document {
    val document = Document()
    document.init()
    return document
}

fun main(args: Array<String>) {
    val sb = StringBuilder()
    document {
        documentClass("foo")
        usepackage("foo", "1", "2")

        +"sdsf"
        math {
            +"12 + 3"
            +"sdfs"
        }
        itemize {
            item("1") {
                +"hello"
                +"mymy"
            }
            item { +"good bye" }
        }
        frame("", "agr1" to "arg2") {
            +"sdfs"
        }
        customTag("pyglist", "language" to "kotlin") {
            +"sdfsf"
            +"sdfs"
        }

    }.render(sb, "")
    println(sb.toString())
}