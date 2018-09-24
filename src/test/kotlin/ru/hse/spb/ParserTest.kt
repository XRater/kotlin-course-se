package ru.hse.spb

import org.junit.Test
import ru.hse.spb.BaseTest.Companion.getFileFromText

class ParserTest {

    private val operators = arrayOf("+", "-", "*", "/", "%",
            ">", "<", ">=", "<=", "==", "!=", "||", "&&")

    @Test
    fun testEmpty() {
        val block = getFileFromText("").getChild(0)
        assert(block.childCount == 0)
    }

    /* Variable */

    @Test
    fun testVariable() {
        val block = getFileFromText("var a").getChild(0)
        assert(block.childCount == 1)
        checkVariableStatement(block.getChild(0), "a")
    }

    @Test
    fun testLiteral() {
        getFileFromText("144")
        getFileFromText("-123")
        getFileFromText("0")
    }

    @Test
    fun testIdentifier() {
        getFileFromText("a")
        getFileFromText("a b")
    }

    @Test
    fun testVariablePlaceholder() {
        val block = getFileFromText("var _").getChild(0)
        assert(block.childCount == 1)
        checkVariableStatement(block.getChild(0), "_")
    }

    @Test
    fun testVariableBigLetters() {
        val block = getFileFromText("var Ab").getChild(0)
        assert(block.childCount == 1)
        checkVariableStatement(block.getChild(0), "Ab")
    }

    @Test
    fun testVariableNumber() {
        val block = getFileFromText("var Ab").getChild(0)
        assert(block.childCount == 1)
        checkVariableStatement(block.getChild(0), "Ab")
    }

    @Test
    fun testVariableCombined() {
        val block = getFileFromText("var _A10b").getChild(0)
        assert(block.childCount == 1)
        checkVariableStatement(block.getChild(0), "_A10b")
    }

    @Test
    fun testTwoVariable() {
        val block = getFileFromText(
                "var x\n" +
                "var y").getChild(0)
        assert(block.childCount == 2)
        checkVariableStatement(block.getChild(0), "x")
        checkVariableStatement(block.getChild(1), "y")
    }

    @Test
    fun testTwoVariableWithBlankLine() {
        val block = getFileFromText(
                "var x\n" +
                "\n" +
                "var y").getChild(0)
        assert(block.childCount == 2)
        checkVariableStatement(block.getChild(0), "x")
        checkVariableStatement(block.getChild(1), "y")
    }

    @Test
    fun testVariableAssignment() {
        getFileFromText("var x = 1")
        getFileFromText("var y = y")
    }

    @Test
    fun testVariableAssignmentBinary() {
        getFileFromText("var x = 1 + 3")
        getFileFromText("var y = (a + t * 17)")
    }

    @Test
    fun testVariableAssignmentFunctionCall() {
        getFileFromText("var x = foo()")
        getFileFromText("var x = foo(1)")
        getFileFromText("var x = foo(a, x, x)")
    }

    /*Binary Expression*/

    @Test
    fun testBinary() {
        for (op in operators) {
            getFileFromText("1 $op 2")
            getFileFromText("a $op 2")
            getFileFromText("1 $op b")
            getFileFromText("a $op b")
        }
    }

    @Test
    fun testBinaryConsequent() {
        for (op in operators) {
            getFileFromText("1 $op 2 $op 3 $op 4")
            getFileFromText("(1) $op 2 $op 3 $op 4")
            getFileFromText("1 $op (2 $op (3)) $op (4)")
            getFileFromText("1 $op 2 $op (3 $op 4)")
            getFileFromText("1 $op (2 $op 3) $op 4")
            getFileFromText("1 $op ((2 $op 3) $op 4)")
            getFileFromText("1 $op (2 $op 3 $op 4)")
            getFileFromText("(1 $op 2) $op 3 $op 4")
            getFileFromText("((1 $op 2) $op 3) $op 4")
            getFileFromText("(1 $op 2 $op 3) $op 4")
            getFileFromText("(1 $op 2) $op (3 $op 4)")
            getFileFromText("((1 $op 2) $op (3 $op 4))")
            getFileFromText("(((1 $op 2) $op 3) $op 4)")
        }
    }

    @Test
    fun testBinaryExpressionCombined() {
        getFileFromText("1 + 2 * 3 % 4")
        getFileFromText("1 == 2 || 3 % 4")
        getFileFromText("1 != (2 || 3) % 4")
        getFileFromText("1 == (2 || 3) <= 4")
        getFileFromText("1 - 2 - (3 && 4)")
        getFileFromText("1 * 2 > (3 < 4)")
        getFileFromText("1 >= 2 + (3 % 4)")
        getFileFromText("(1 + 2) * 3 != 4"  )
    }

    @Test
    fun testBinaryFunctionCall() {
        getFileFromText("1 + foo()")
        getFileFromText("(foo()) + 2")
        getFileFromText("(foo()) + foo(x, y)")
        getFileFromText("1 * foo() * foo(3)")
    }

    /* Assignment */

    @Test
    fun testAssignmentBase() {
        getFileFromText("a = b")
        getFileFromText("a = 1")
        getFileFromText("a = a")
        getFileFromText("a = (3)")
    }

    @Test
    fun testAssignmentBinary() {
        getFileFromText("a = 1 + 2")
        getFileFromText("a = b - c")
        getFileFromText("a = (1 + 4 * 7)")
    }

    @Test
    fun testAssignmentFunctionCall() {
        getFileFromText("a = foo(x)")
        getFileFromText("a = foo(r, 38)")
        getFileFromText("a = a()")
    }

    /* Function call */

    @Test
    fun testFunctionCall() {
        getFileFromText("foo()")
        getFileFromText("foo(1, 2, 3)")
        getFileFromText("foo(a)")
        getFileFromText("foo(a + b, foo())")
    }

    /* Function */

    @Test
    fun testFunction() {
        getFileFromText("fun foo() {}")
        getFileFromText("fun foo(x) {}")
        getFileFromText("fun foo(x, y) {}")
    }

    @Test
    fun testFunctionWithBody() {
        getFileFromText("fun foo() {" +
                        "   var a = 1 + 2" +
                        "   x = 1" +
                        "}")
    }

    @Test
    fun testFunctionWithReturn() {
        getFileFromText("fun foo() {" +
                "   return a" +
                "}")
    }

    @Test
    fun testFunctionWithInnerFunction() {
        getFileFromText("fun foo() {" +
                        "   fun bar() {}" +
                        "}")
    }

    @Test
    fun testFunctionWithInnerWhile() {
        getFileFromText("fun foo() {" +
                "   while (1) {}" +
                "}")
    }

    @Test
    fun testFunctionWithInnerIf() {
        getFileFromText("fun foo() {" +
                "   if (1) {} else {}" +
                "}")
    }

    @Test
    fun testWhile() {
        getFileFromText("while (1) {}")
    }

    @Test
    fun testIf() {
        getFileFromText("if (1) {}")
        getFileFromText("if (1) { return 1 }")
        getFileFromText("if (1) {} else {}")
    }

    @Test
    fun testReturn() {
        getFileFromText("if (1) { return 1 }")
        getFileFromText("if (1) { return 1 + 2 }")
        getFileFromText("if (1) { return a }")
        getFileFromText("if (1) { return foo(a) }")
        getFileFromText("return 0")
        getFileFromText("return 1")
        getFileFromText("return -1")
    }

}