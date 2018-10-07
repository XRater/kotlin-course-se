package tex.base

class DocumentClass(
        argument : String,
        vararg attributes: String
) : FunctionTag("documentClass", argument, *attributes)

class Usepackage(
        argument : String,
        vararg attributes: String
) : FunctionTag("userpackage", argument, *attributes)

fun renderAttributes(attributes: Array<out String>) =
    if (attributes.isEmpty()) "" else attributes.joinToString(", ", "{", "}")