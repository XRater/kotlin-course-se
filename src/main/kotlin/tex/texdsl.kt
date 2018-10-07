package tex

import tex.base.*
import java.lang.StringBuilder

class Document : PairedTag("document") {

    fun documentClass(argument : String, vararg attributes: String)
            = children.add(DocumentClass(argument, *attributes))

    fun usepackage(argument : String, vararg attributes: String)
            = children.add(Usepackage(argument, *attributes))

    fun itemize(init : Itemize.() -> Unit) = initTag(Itemize(), init)

    fun enumerate(init : Enumerate.() -> Unit) = initTag(Enumerate(), init)
}

abstract class Listed(name: String) : PairedTag(name) {

    fun item(init: Item.() -> Unit) = initTag(Item(), init)

}

class Enumerate : Listed("itemize")

class Itemize : Listed("enumerate")

class Item : IdentifierTag("item")

fun document(init: Document.() -> Unit): Document {
    val document = Document()
    document.init()
    return document
}

fun main(args: Array<String>) {
    val sb = StringBuilder()
    document {
        documentClass("foo", "1", "2")
        usepackage("foo", "1", "2")

        itemize {
            item { +"hello" }
            item { +"good bye" }
        }
    }.render(sb, "")
    println(sb.toString())
}