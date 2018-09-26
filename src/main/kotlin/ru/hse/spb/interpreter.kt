package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import ru.hse.spb.parser.FunLexer
import ru.hse.spb.parser.FunParser

fun run(text : String) : Int {
    val lexer = FunLexer(CharStreams.fromString(text))
    val parser = FunParser(BufferedTokenStream(lexer))
    val file = parser.file()
    val visitor = InterpretVisitor()
    return visitor.visit(file)
}

fun interpretate(text : String) {
    val lexer = FunLexer(CharStreams.fromString(text))
    val parser = FunParser(BufferedTokenStream(lexer))
    val file = parser.file()
    val visitor = InterpretVisitor()

    try {
        visitor.visit(file)
    } catch (e : InterpretationException) {
        print(e.message())
    }
}

fun main(args: Array<String>) {
    interpretate("return x")
}