package tex.base

import java.lang.StringBuilder

@TexMarker
abstract class IdentifierTag(val name: String) : TagWithText() {

    var attributes : List<String> = listOf()

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\$name${renderAttributes(attributes)}\n")
        for (child in children) {
            child.render(builder, "$indent  ")
        }
    }

}

@TexMarker
abstract class PairedTag(val name: String) : TagWithText() {

    var attributes : List<String> = listOf()

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\begin{$name}${renderAttributes(attributes)}\n")
        for (child in children) {
            child.render(builder, "$indent  ")
        }
        builder.append("$indent\\end{$name}\n")
    }

    protected fun <T : Element> initTag(tag: T, init: T.() -> Unit): T {
        tag.init()
        children.add(tag)
        return tag
    }

}