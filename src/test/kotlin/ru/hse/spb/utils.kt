package ru.hse.spb

import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.tree.ParseTree
import org.junit.Assert.fail
import ru.hse.spb.parser.FunLexer
import ru.hse.spb.parser.FunParser

class BaseTest {

    companion object {
        fun getFileFromText(
                text: String
        ): ParseTree {
            val lexer = FunLexer(CharStreams.fromString(text))
            val parser = FunParser(BufferedTokenStream(lexer))
            lexer.removeErrorListeners()
            lexer.addErrorListener(object : BaseErrorListener() {
                override fun syntaxError(recognizer: Recognizer<*, *>?,
                                         offendingSymbol: Any?,
                                         line: Int,
                                         charPositionInLine: Int,
                                         msg: String?,
                                         e: RecognitionException?) {
                    fail("line $line:$charPositionInLine $msg")
                }
            })
            parser.removeErrorListeners()
            parser.addErrorListener(object : BaseErrorListener() {
                override fun syntaxError(recognizer: Recognizer<*, *>?,
                                         offendingSymbol: Any?,
                                         line: Int,
                                         charPositionInLine: Int,
                                         msg: String?,
                                         e: RecognitionException?) {
                    fail("line $line:$charPositionInLine $msg")
                }
            })

            return parser.file()
        }

        fun getFileFromTextExpectingError(
                text: String
        ) {
            var passed = false
            val lexer = FunLexer(CharStreams.fromString(text))
            val parser = FunParser(BufferedTokenStream(lexer))
            lexer.removeErrorListeners()
            lexer.addErrorListener(object : BaseErrorListener() {
                override fun syntaxError(recognizer: Recognizer<*, *>?,
                                         offendingSymbol: Any?,
                                         line: Int,
                                         charPositionInLine: Int,
                                         msg: String?,
                                         e: RecognitionException?) {
                    passed = true
                }
            })
            parser.removeErrorListeners()
            parser.addErrorListener(object : BaseErrorListener() {
                override fun syntaxError(recognizer: Recognizer<*, *>?,
                                         offendingSymbol: Any?,
                                         line: Int,
                                         charPositionInLine: Int,
                                         msg: String?,
                                         e: RecognitionException?) {
                    passed = true
                }
            })

            parser.file()
            if (!passed) {
                fail("No error found while parsing")
            }
        }

    }
}

fun checkVariableStatement(statement: ParseTree, name: String) {
    assert(statement is FunParser.StatementContext)
    val variable = statement.getChild(0)
    assert(variable is FunParser.VariableContext)
    assert(variable.getChild(1).text == name)
}
