package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import ru.hse.spb.parser.FunLexer
import ru.hse.spb.parser.FunParser

fun getGreeting(): String {
    val words = mutableListOf<String>()
    words.add("Hello,")

    words.add("world!")

    return words.joinToString(separator = " ")
}

fun main(args: Array<String>) {
    val lexer = FunLexer(CharStreams.fromString("1"))
    val parser = FunParser(BufferedTokenStream(lexer))
}