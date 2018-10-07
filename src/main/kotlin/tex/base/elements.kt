package tex.base

import java.lang.StringBuilder

interface Element {

    fun render(builder : StringBuilder, indent : String)

}

class TextElement(val text: String) : Element {

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$text\n")
    }

}