package tex

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class TexDSLTest {

    @Test
    fun testDocument() {
        val sb = StringBuilder()
        document {  }.render(sb, "")
        val expected = """
            \begin{document}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testWithText() {
        val sb = StringBuilder()
        document {
            +"Hello"
        }.render(sb, "")
        val expected = """
            \begin{document}
                Hello
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testWithTwoLines() {
        val sb = StringBuilder()
        document {
            +"Hello"
            +"1"
        }.render(sb, "")
        val expected = """
            \begin{document}
                Hello
                1
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testMath() {
        val sb = StringBuilder()
        document {
            math {}
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{math}
                \end{math}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testMathWithText() {
        val sb = StringBuilder()
        document {
            math {
                +"Text"
            }
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{math}
                    Text
                \end{math}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testFrame() {
        val sb = StringBuilder()
        document {
            frame("myName") { }
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{frame}
                    \frametitle{myName}
                \end{frame}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testFrameWithText() {
        val sb = StringBuilder()
        document {
            frame("myName") {
                +"Hello"
            }
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{frame}
                    \frametitle{myName}
                    Hello
                \end{frame}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testCustomTag() {
        val sb = StringBuilder()
        document {
            customTag("myName") {
                +"Hello"
            }
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{myName}
                    Hello
                \end{myName}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testCustomTagWithParam() {
        val sb = StringBuilder()
        document {
            customTag("myName", "arg") {
                +"Hello"
            }
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{myName}[arg]
                    Hello
                \end{myName}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testCustomTagWithTwoParams() {
        val sb = StringBuilder()
        document {
            customTag("myName", "arg1", "arg2") {
                +"Hello"
            }
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{myName}[arg1, arg2]
                    Hello
                \end{myName}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testCustomTagWithTwoParamsAndTo() {
        val sb = StringBuilder()
        document {
            customTag("myName", "arg1" to "arg2") {
                +"Hello"
            }
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{myName}[arg1=arg2]
                    Hello
                \end{myName}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testItemize() {
        val sb = StringBuilder()
        document {
            itemize {}
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{itemize}
                \end{itemize}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testOneItemize() {
        val sb = StringBuilder()
        document {
            itemize {
                item {}
            }
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{itemize}
                    \item
                \end{itemize}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testItemizeTwoItems() {
        val sb = StringBuilder()
        document {
            itemize {
                item {}
                item {}
            }
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{itemize}
                    \item
                    \item
                \end{itemize}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testItemizeParams() {
        val sb = StringBuilder()
        document {
            itemize("hello") {
                item {}
                item {}
            }
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{itemize}[hello]
                    \item
                    \item
                \end{itemize}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testEnumerate() {
        val sb = StringBuilder()
        document {
            enumerate {}
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{enumerate}
                \end{enumerate}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testOneEnumerate() {
        val sb = StringBuilder()
        document {
            enumerate {
                item {}
            }
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{enumerate}
                    \item
                \end{enumerate}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testEnumerateTwoItems() {
        val sb = StringBuilder()
        document {
            enumerate {
                item {}
                item {}
            }
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{enumerate}
                    \item
                    \item
                \end{enumerate}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testEnumerateParams() {
        val sb = StringBuilder()
        document {
            enumerate("hello") {
                item {}
                item {}
            }
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{enumerate}[hello]
                    \item
                    \item
                \end{enumerate}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testDocumentUse() {
        val sb = StringBuilder()
        document {
            usepackage("hello")
        }.render(sb, "")
        val expected = """
            \usepackage{hello}
            \begin{document}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testDocumentUseArg() {
        val sb = StringBuilder()
        document {
            usepackage("hello", "arg")
        }.render(sb, "")
        val expected = """
            \usepackage[arg]{hello}
            \begin{document}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testDocumentUseTwoArg() {
        val sb = StringBuilder()
        document {
            usepackage("hello", "arg", "arg2")
        }.render(sb, "")
        val expected = """
            \usepackage[arg, arg2]{hello}
            \begin{document}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testDocumentDocumentClass() {
        val sb = StringBuilder()
        document {
            documentClass("hello")
        }.render(sb, "")
        val expected = """
            \documentclass{hello}
            \begin{document}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testAlignCenter() {
        val sb = StringBuilder()
        document {
            alignment(Alignment.TYPE.CENTER) {}
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{center}
                \end{center}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testAlignRight() {
        val sb = StringBuilder()
        document {
            alignment(Alignment.TYPE.RIGHT) {}
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{flushright}
                \end{flushright}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

    @Test
    fun testAlignLeft() {
        val sb = StringBuilder()
        document {
            alignment(Alignment.TYPE.LEFT) {}
        }.render(sb, "")
        val expected = """
            \begin{document}
                \begin{flushleft}
                \end{flushleft}
            \end{document}
        """.trimIndent().plus("\n")
        assertEquals(expected, sb.toString())
    }

}