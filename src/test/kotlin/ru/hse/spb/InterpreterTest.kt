package ru.hse.spb

import org.junit.Assert.assertEquals
import org.junit.Test

class InterpreterTest {

    @Test
    fun testEmpty() {
        assert(run("") == 0)
    }

    @Test
    fun testReturn() {
        assert(run("return 1") == 1)
        assert(run("return 0") == 0)
        assert(run("return -2") == -2)
    }

    @Test
    fun testTwoReturns() {
        assertEquals(1, run(
                "return 1\n" +
                "return 2"
        ))
    }

    @Test
    fun testReturnFromWhile() {
        assertEquals(0, run(
                "while (1) {\n" +
                "   var x = 1\n" +
                "   return 1\n" +
                "}"
        ))
    }

    @Test
    fun testBlock() {
        assertEquals(0, run("var x = 1"))
    }

    @Test
    fun testBlockWithBraces() {
        assertEquals(0, run(
                "if (1) {\n" +
                "   var x = 1\n" +
                "}"
        ))
    }

    @Test
    fun testReturnVariable() {
        assertEquals(1, run(
                "var x = 1\n" +
                "return x"
        ))
    }

    @Test(expected = NotDeclaredVariableException::class)
    fun testNotDeclaredVariable() {
        run("return x")
    }

    @Test(expected = NotInitializedVariableException::class)
    fun testNotInitializedVariable() {
        run("var x\n" +
            "return x")
    }

    @Test(expected = DoubleVariableDeclarationException::class)
    fun testDoubleVariableDeclaration() {
        run("var x\n" +
            "var x")
    }

    @Test(expected = DoubleVariableDeclarationException::class)
    fun testDoubleVariableDeclarationWithInit() {
        run("var x\n" +
            "var x = 1")
    }

    @Test
    fun testBinary() {
        assertEquals(3, run("return 1 + 2"))
        assertEquals(2, run("return 1 * 2"))
        assertEquals(-1, run("return 1 - 2"))
        assertEquals(0, run("return 1 / 2"))
        assertEquals(1, run("return 1 % 2"))
        assertEquals(1, run("return 1 || 2"))
        assertEquals(1, run("return 1 && 2"))
        assertEquals(1, run("return 0 || 2"))
        assertEquals(0, run("return 0 && 2"))
        assertEquals(1, run("return 2 || 0"))
        assertEquals(0, run("return 2 && 0"))
        assertEquals(0, run("return 0 || 0"))
        assertEquals(0, run("return 0 && 0"))

        assertEquals(0, run("return 1 == 2"))
        assertEquals(1, run("return 1 == 1"))
        assertEquals(1, run("return 1 != 2"))
        assertEquals(0, run("return 1 != 1"))

        assertEquals(0, run("return 1 > 2"))
        assertEquals(0, run("return 1 > 1"))
        assertEquals(1, run("return 1 > -1"))
        assertEquals(1, run("return 1 < 2"))
        assertEquals(0, run("return 1 < 1"))
        assertEquals(0, run("return 1 < -1"))

        assertEquals(0, run("return 1 >= 2"))
        assertEquals(1, run("return 1 >= 1"))
        assertEquals(1, run("return 1 >= -1"))
        assertEquals(1, run("return 1 <= 2"))
        assertEquals(1, run("return 1 <= 1"))
        assertEquals(0, run("return 1 <= -1"))
    }

    @Test
    fun testBinaryPriorityMulAdd() {
        assertEquals(7, run("return 1 + 2 * 3"))
        assertEquals(-5, run("return 1 - 2 * 3"))
        assertEquals(4, run("return 4 + 2 / 3"))
        assertEquals(8, run("return 8 - 2 / 3"))
        assertEquals(5, run("return 4 + 4 % 3"))
        assertEquals(7, run("return 8 - 4 % 3"))

        assertEquals(7, run("return 2 * 3 + 1"))
        assertEquals(5, run("return 2 * 3 - 1"))
        assertEquals(4, run("return 2 / 3 + 4"))
        assertEquals(-8, run("return 2 / 3 - 8"))
        assertEquals(5, run("return 4 % 3 + 4"))
        assertEquals(-7, run("return 4 % 3 - 8"))
    }

    @Suppress("SimplifyBooleanWithConstants")
    @Test
    fun testBinaryPriorityAddComp() {
        assertEquals(1, run("return 8 + 2 >= 3"))
        assertEquals(1, run("return 8 - 2 >= 3"))
        assertEquals(0, run("return 8 + 2 <= 3"))
        assertEquals(0, run("return 8 - 2 <= 3"))
        assertEquals(1, run("return 8 + 2 > 3"))
        assertEquals(1, run("return 8 - 2 > 3"))
        assertEquals(0, run("return 8 + 2 < 3"))
        assertEquals(0, run("return 8 - 2 < 3"))

        assertEquals(0, run("return 2 >= 3 + 8"))
        assertEquals(1, run("return 2 >= 3 - 8"))
        assertEquals(1, run("return 2 <= 3 + 8"))
        assertEquals(0, run("return 2 <= 3 - 8"))
        assertEquals(0, run("return 2 >= 3 + 8"))
        assertEquals(1, run("return 2 >= 3 - 8"))
        assertEquals(1, run("return 2 <= 3 + 8"))
        assertEquals(0, run("return 2 <= 3 - 8"))
    }

    @Test
    fun testBinaryPriorityCompEq() {
        assertEquals(0, run("return 10 > 0 == 11"))
        assertEquals(0, run("return 12 > 11 != 1"))
        assertEquals(0, run("return 11 == 0 < 10"))
        assertEquals(0, run("return 1 != 11 < 12"))
    }

    @Test
    fun testBinaryPriorityEqLogical() {
        assertEquals(0, run("return 0 == 11 && 0"))
        assertEquals(0, run("return 1 == 11 || 0"))
        assertEquals(0, run("return 0 && 11 == 0"))
        assertEquals(0, run("return 0 && 11 == 1"))
    }

    @Test
    fun testBinaryPriorityBrackets() {
        assertEquals(7, run("return 1 + (2 * 3)"))
        assertEquals(9, run("return (1 + 2) * 3"))
    }

    @Test
    fun testBinaryChain() {
        assertEquals(6, run("return 1 + 2 + 3"))
        assertEquals(24, run("return 2 * 3 * 4"))
        assertEquals(2, run("return 2 % 3 % 4"))
        assertEquals(0, run("return 2 > 3 > 4"))
        assertEquals(1, run("return 2 < 3 < 4"))
        assertEquals(1, run("return 2 == 3 == 0"))
        assertEquals(1, run("return 2 || 3 || 0"))
    }
}